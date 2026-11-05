package com.mycompany.jadk_care.dao;
import com.mycompany.jadk_care.database.ConexionBD;
import com.mycompany.jadk_care.modelo.Usuarios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt; 
public class UsuarioDAO {


public boolean registrarUsuario(Usuarios usuario) {
    String sql = "INSERT INTO usuarios (nombre, correo, contrasena, rol) VALUES (?, ?, ?, ?)";
    
    try (Connection con = ConexionBD.obtenerConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, usuario.getNombre());
        ps.setString(2, usuario.getCorreo());
        
        // --- ¡AQUÍ ENCRIPTAMOS LA CONTRASEÑA! ---
        String contrasenaEncriptada = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt());
        ps.setString(3, contrasenaEncriptada); // Guardamos el hash en MySQL
        
        ps.setString(4, usuario.getRol());
        
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
public Usuarios iniciarSesion(String identificador, String contrasenaPlana) {
    String sql = "SELECT * FROM usuarios WHERE correo = ? OR nombre = ?"; 
    
    try (Connection con = ConexionBD.obtenerConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, identificador);
        ps.setString(2, identificador);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String hashBD = rs.getString("contrasena");
                
                // Compara la contraseña que metió el usuario con el hash de la BD
                if (BCrypt.checkpw(contrasenaPlana, hashBD)) {
                    // Si coinciden, construimos el objeto usuario
                    return new Usuarios(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        hashBD, // Mantenemos el hash
                        rs.getString("rol")
                    );
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null; 
}
    public boolean validarContrasenaPorId(int idUsuario, String contrasenaPlana) {
        String sql = "SELECT contrasena FROM usuarios WHERE id_usuario = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashBD = rs.getString("contrasena");
                    return BCrypt.checkpw(contrasenaPlana, hashBD);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizarUsuario(int idUsuario, String nuevoCorreo, String nuevoNombre, String nuevoRol) {
        String sql = "UPDATE usuarios SET correo = ?, nombre = ?, rol = ? WHERE id_usuario = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoCorreo);
            ps.setString(2, nuevoNombre);
            ps.setString(3, nuevoRol);
            ps.setInt(4, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarContrasena(int idUsuario, String nuevaContrasenaPlana) {
        String sql = "UPDATE usuarios SET contrasena = ? WHERE id_usuario = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String contrasenaEncriptada = BCrypt.hashpw(nuevaContrasenaPlana, BCrypt.gensalt());
            ps.setString(1, contrasenaEncriptada);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarContrasenaPorCorreo(String correo, String nuevaContrasenaPlana) {
        String sql = "UPDATE usuarios SET contrasena = ? WHERE correo = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String contrasenaEncriptada = BCrypt.hashpw(nuevaContrasenaPlana, BCrypt.gensalt());
            ps.setString(1, contrasenaEncriptada);
            ps.setString(2, correo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
