package com.mycompany.jadk_care.controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

import com.mycompany.jadk_care.dao.UsuarioDAO;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUsuario(
            @PathVariable int id, 
            @RequestBody UpdateUserRequest request) {
        
        Map<String, Object> response = new HashMap<>();
        boolean success = usuarioDAO.actualizarUsuario(id, request.correo(), request.nombre(), request.rol());

        if (success) {
            response.put("success", true);
            response.put("message", "Usuario actualizado correctamente.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Error al actualizar el usuario.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}/contrasena")
    public ResponseEntity<Map<String, Object>> updateContrasena(
            @PathVariable int id, 
            @RequestBody UpdatePasswordRequest request) {
        
        Map<String, Object> response = new HashMap<>();
        
        boolean oldPasswordValid = usuarioDAO.validarContrasenaPorId(id, request.contrasenaAntigua());
        if (!oldPasswordValid) {
            response.put("success", false);
            response.put("message", "La contraseña antigua es incorrecta.");
            return ResponseEntity.badRequest().body(response);
        }

        boolean success = usuarioDAO.actualizarContrasena(id, request.contrasenaNueva());

        if (success) {
            response.put("success", true);
            response.put("message", "Contraseña actualizada correctamente.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Error al actualizar la contraseña.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    public record UpdateUserRequest(String correo, String nombre, String rol) {}
    public record UpdatePasswordRequest(String contrasenaAntigua, String contrasenaNueva) {}
}
