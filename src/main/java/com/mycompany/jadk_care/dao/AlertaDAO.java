package com.mycompany.jadk_care.dao;

import com.mycompany.jadk_care.database.ConexionBD;
import com.mycompany.jadk_care.modelo.Alerta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

public class AlertaDAO {

    public boolean generarAlertaAutomatica(String idEquipo, String titulo, String gravedad) {
        String sql = "INSERT INTO alertas (id_equipo, titulo, gravedad, estado_alerta) VALUES (?, ?, ?, 'Pendiente')";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, idEquipo);
            ps.setString(2, titulo);
            ps.setString(3, gravedad);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al generar alerta en la base de datos.");
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Alerta> obtenerAlertasPendientes() {
        ArrayList<Alerta> lista = new ArrayList<>();
        String sql = "SELECT * FROM alertas WHERE estado_alerta = 'Pendiente' ORDER BY fecha_alerta DESC";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Alerta alerta = new Alerta(
                    rs.getInt("id_alerta"),
                    rs.getString("id_equipo"),
                    rs.getString("titulo"),
                    rs.getString("gravedad"),
                    rs.getTimestamp("fecha_alerta"),
                    rs.getString("estado_alerta") 
                );
                lista.add(alerta);
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar las alertas.");
            e.printStackTrace();
        }
        return lista;
    }

    public boolean resolverAlertaYCrearBitacora(String idEquipo, String tipoMantenimiento, String observaciones, String tecnicoNombre) {
        String sqlSelectUser = "SELECT id_usuario FROM usuarios WHERE nombre = ?";
        String sqlUpdateAlerta = "UPDATE alertas SET estado_alerta = 'Resuelta' WHERE id_equipo = ? AND estado_alerta = 'Pendiente'";
        String sqlInsertBitacora = "INSERT INTO bitacora_mantenimiento (id_equipo, id_usuario, tipo_mantenimiento, observaciones, fecha_realizacion) VALUES (?, ?, ?, ?, CURDATE())";
        String sqlUpdateEquipo = "UPDATE equipos SET estado_actual = 'green', fecha_ultimo_man = CURDATE(), fecha_prox_man = DATE_ADD(CURDATE(), INTERVAL 1 MONTH) WHERE id_equipo = ?";

        Connection con = null;
        try {
            con = ConexionBD.obtenerConexion();
            con.setAutoCommit(false);

            // 1. Obtener ID del usuario/técnico
            int idUsuario = -1;
            try (PreparedStatement psUser = con.prepareStatement(sqlSelectUser)) {
                psUser.setString(1, tecnicoNombre);
                try (ResultSet rs = psUser.executeQuery()) {
                    if (rs.next()) {
                        idUsuario = rs.getInt("id_usuario");
                    }
                }
            }

            // Si el técnico no existe, lo creamos
            if (idUsuario == -1) {
                String sqlInsertUser = "INSERT INTO usuarios (nombre, correo, contrasena, rol) VALUES (?, ?, ?, ?)";
                try (PreparedStatement psInsertUser = con.prepareStatement(sqlInsertUser, Statement.RETURN_GENERATED_KEYS)) {
                    psInsertUser.setString(1, tecnicoNombre);
                    
                    // Asignamos un correo de técnico en lugar de @jadk.com para que no se vuelva administrador por error
                    psInsertUser.setString(2, tecnicoNombre.replaceAll(" ", "").toLowerCase() + "@tecnico.com");
                    
                    // Hasheamos la contraseña por defecto "1234"
                    String passwordHashed = BCrypt.hashpw("1234", BCrypt.gensalt());
                    psInsertUser.setString(3, passwordHashed);
                    
                    psInsertUser.setString(4, "Técnico");
                    psInsertUser.executeUpdate();
                    try (ResultSet rsKeys = psInsertUser.getGeneratedKeys()) {
                        if (rsKeys.next()) {
                            idUsuario = rsKeys.getInt(1);
                        }
                    }
                }
            }
            
            // Fallback de emergencia
            if (idUsuario == -1) idUsuario = 2; // Diego Hernandez de base

            // 2. Marcar alertas pendientes del equipo como resueltas
            try (PreparedStatement psAlerta = con.prepareStatement(sqlUpdateAlerta)) {
                psAlerta.setString(1, idEquipo);
                psAlerta.executeUpdate();
            }

            // 3. Crear registro en bitácora de mantenimiento
            try (PreparedStatement psBitacora = con.prepareStatement(sqlInsertBitacora)) {
                psBitacora.setString(1, idEquipo);
                psBitacora.setInt(2, idUsuario);
                psBitacora.setString(3, tipoMantenimiento);
                psBitacora.setString(4, observaciones);
                psBitacora.executeUpdate();
            }

            // 4. Actualizar estado del equipo a operativo (green)
            try (PreparedStatement psEquipo = con.prepareStatement(sqlUpdateEquipo)) {
                psEquipo.setString(1, idEquipo);
                psEquipo.executeUpdate();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error en la transacción al resolver alerta y crear bitácora. Aplicando Rollback...");
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int obtenerTotalMantenimientos() {
        String sql = "SELECT COUNT(*) FROM bitacora_mantenimiento";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}