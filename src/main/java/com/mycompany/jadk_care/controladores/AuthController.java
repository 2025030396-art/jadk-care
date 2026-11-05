package com.mycompany.jadk_care.controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

import com.mycompany.jadk_care.dao.UsuarioDAO;
import com.mycompany.jadk_care.modelo.Usuarios;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        Usuarios usuario = usuarioDAO.iniciarSesion(request.correo(), request.contrasena());

        if (usuario != null) {
            response.put("success", true);
            response.put("usuario", usuario.getNombre());
            response.put("correo", usuario.getCorreo());
            response.put("rol", usuario.getRol());
            response.put("idUsuario", usuario.getIdUsuario());
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Usuario o contraseña incorrectos.");
            return ResponseEntity.status(401).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        String correoInput = request.correo() != null ? request.correo().toLowerCase() : "";
        String rolAsignado = correoInput.endsWith("@jadk.com") ? "Administrador" : "Técnico";

        Usuarios nuevoUsuario = new Usuarios(
            0,
            request.nombre(),
            request.correo(),
            request.contrasena(),
            rolAsignado
        );

        boolean registrado = usuarioDAO.registrarUsuario(nuevoUsuario);

        if (registrado) {
            response.put("success", true);
            response.put("message", "Usuario registrado correctamente.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Error al registrar el usuario. Es posible que el correo ya exista.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody ResetPasswordRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        boolean success = usuarioDAO.actualizarContrasenaPorCorreo(request.correo(), request.nuevaContrasena());
        
        if (success) {
            response.put("success", true);
            response.put("message", "Contraseña restablecida con éxito.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Error al restablecer la contraseña. Verifica que el correo exista.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // DTOs for requests
    public record LoginRequest(String correo, String contrasena) {}
    public record RegisterRequest(String nombre, String correo, String contrasena) {}
    public record ResetPasswordRequest(String correo, String nuevaContrasena) {}
}
