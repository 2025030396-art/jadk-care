package com.mycompany.jadk_care.dao;

import com.mycompany.jadk_care.database.ConexionBD;
import com.mycompany.jadk_care.modelo.Equipo;
import com.mycompany.jadk_care.modelo.Especificacion;
import com.mycompany.jadk_care.modelo.Almacenamiento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EquipoDAO {

    public boolean registrarEquipoConEspecificaciones(Equipo equipo) {
        String sqlEquipo = "INSERT INTO equipos (id_equipo, laboratorio, num_puesto, estado_actual, fecha_ultimo_man, fecha_prox_man) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlEspecificacion = "INSERT INTO especificaciones (id_equipo, cpu_modelo, motherboard, ram_capacidad, gpu_modelo, mac_address, ip_address) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlAlmacenamiento = "INSERT INTO almacenamientos (id_equipo, tipo, capacidad, uso) VALUES (?, ?, ?, ?)";
        
        Connection con = null;
        try {
            con = ConexionBD.obtenerConexion();
            con.setAutoCommit(false);
            
            // 1. Insertar en la tabla Equipos
            try (PreparedStatement psEquipo = con.prepareStatement(sqlEquipo)) {
                psEquipo.setString(1, equipo.getIdEquipo());
                psEquipo.setString(2, equipo.getLaboratorio());
                psEquipo.setInt(3, equipo.getNumPuesto());
                psEquipo.setString(4, equipo.getEstadoActual());
                psEquipo.setDate(5, equipo.getFechaUltimoMan());
                psEquipo.setDate(6, equipo.getFechaProxMan());
                psEquipo.executeUpdate();
            }
            
            // 2. Insertar en la tabla Especificaciones
            Especificacion specs = equipo.getEspecificaciones();
            try (PreparedStatement psSpecs = con.prepareStatement(sqlEspecificacion)) {
                psSpecs.setString(1, equipo.getIdEquipo());
                psSpecs.setString(2, specs.getCpuModelo());
                psSpecs.setString(3, specs.getMotherboard());
                psSpecs.setString(4, specs.getRamCapacidad());
                psSpecs.setString(5, specs.getGpuModelo());
                psSpecs.setString(6, specs.getMacAddress());
                psSpecs.setString(7, specs.getIpAddress());
                psSpecs.executeUpdate();
            }

            // 3. Insertar almacenamientos
            if (specs != null && specs.getAlmacenamientos() != null) {
                for (Almacenamiento storage : specs.getAlmacenamientos()) {
                    try (PreparedStatement psStorage = con.prepareStatement(sqlAlmacenamiento)) {
                        psStorage.setString(1, equipo.getIdEquipo());
                        psStorage.setString(2, storage.getTipo());
                        psStorage.setString(3, storage.getCapacidad());
                        psStorage.setString(4, storage.getUso());
                        psStorage.executeUpdate();
                    }
                }
            }
            
            con.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error en la transacción de alta de equipo. Aplicando Rollback...");
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

    public boolean actualizarEquipoConEspecificaciones(Equipo equipo) {
        String sqlEquipo = "UPDATE equipos SET laboratorio = ?, num_puesto = ?, estado_actual = ?, fecha_ultimo_man = ?, fecha_prox_man = ? WHERE id_equipo = ?";
        String sqlEspecificacion = "UPDATE especificaciones SET cpu_modelo = ?, motherboard = ?, ram_capacidad = ?, gpu_modelo = ?, mac_address = ?, ip_address = ? WHERE id_equipo = ?";
        String sqlDeleteAlmacenamiento = "DELETE FROM almacenamientos WHERE id_equipo = ?";
        String sqlAlmacenamiento = "INSERT INTO almacenamientos (id_equipo, tipo, capacidad, uso) VALUES (?, ?, ?, ?)";
        
        Connection con = null;
        try {
            con = ConexionBD.obtenerConexion();
            con.setAutoCommit(false);
            
            // 1. Actualizar Equipos
            try (PreparedStatement psEquipo = con.prepareStatement(sqlEquipo)) {
                psEquipo.setString(1, equipo.getLaboratorio());
                psEquipo.setInt(2, equipo.getNumPuesto());
                psEquipo.setString(3, equipo.getEstadoActual());
                psEquipo.setDate(4, equipo.getFechaUltimoMan());
                psEquipo.setDate(5, equipo.getFechaProxMan());
                psEquipo.setString(6, equipo.getIdEquipo());
                psEquipo.executeUpdate();
            }
            
            // 2. Actualizar Especificaciones
            Especificacion specs = equipo.getEspecificaciones();
            try (PreparedStatement psSpecs = con.prepareStatement(sqlEspecificacion)) {
                psSpecs.setString(1, specs.getCpuModelo());
                psSpecs.setString(2, specs.getMotherboard());
                psSpecs.setString(3, specs.getRamCapacidad());
                psSpecs.setString(4, specs.getGpuModelo());
                psSpecs.setString(5, specs.getMacAddress());
                psSpecs.setString(6, specs.getIpAddress());
                psSpecs.setString(7, equipo.getIdEquipo());
                psSpecs.executeUpdate();
            }

            // 3. Eliminar almacenamientos viejos
            try (PreparedStatement psDelStorage = con.prepareStatement(sqlDeleteAlmacenamiento)) {
                psDelStorage.setString(1, equipo.getIdEquipo());
                psDelStorage.executeUpdate();
            }

            // 4. Insertar almacenamientos nuevos
            if (specs != null && specs.getAlmacenamientos() != null) {
                for (Almacenamiento storage : specs.getAlmacenamientos()) {
                    try (PreparedStatement psStorage = con.prepareStatement(sqlAlmacenamiento)) {
                        psStorage.setString(1, equipo.getIdEquipo());
                        psStorage.setString(2, storage.getTipo());
                        psStorage.setString(3, storage.getCapacidad());
                        psStorage.setString(4, storage.getUso());
                        psStorage.executeUpdate();
                    }
                }
            }
            
            con.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error en la transacción de modificación de equipo. Aplicando Rollback...");
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

    public ArrayList<Equipo> obtenerTodosEquipos() {
        ArrayList<Equipo> lista = new ArrayList<>();
        String sql = "SELECT e.*, s.cpu_modelo, s.motherboard, s.ram_capacidad, s.gpu_modelo, s.mac_address, s.ip_address " +
                     "FROM equipos e " +
                     "LEFT JOIN especificaciones s ON e.id_equipo = s.id_equipo";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                String idEquipo = rs.getString("id_equipo");
                
                // Obtener almacenamientos del equipo
                List<Almacenamiento> storageList = obtenerAlmacenamientosPorEquipo(con, idEquipo);
                
                Especificacion specs = new Especificacion(
                    idEquipo,
                    rs.getString("cpu_modelo"),
                    rs.getString("motherboard"),
                    rs.getString("ram_capacidad"),
                    rs.getString("gpu_modelo"),
                    storageList,
                    rs.getString("mac_address"),
                    rs.getString("ip_address")
                );
                
                Equipo equipo = new Equipo(
                    idEquipo,
                    rs.getString("laboratorio"),
                    rs.getInt("num_puesto"),
                    rs.getString("estado_actual"),
                    rs.getDate("fecha_ultimo_man"),
                    rs.getDate("fecha_prox_man"),
                    specs
                );
                lista.add(equipo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private List<Almacenamiento> obtenerAlmacenamientosPorEquipo(Connection con, String idEquipo) throws SQLException {
        List<Almacenamiento> list = new ArrayList<>();
        String sql = "SELECT * FROM almacenamientos WHERE id_equipo = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idEquipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Almacenamiento(
                        rs.getInt("id_almacenamiento"),
                        rs.getString("id_equipo"),
                        rs.getString("tipo"),
                        rs.getString("capacidad"),
                        rs.getString("uso")
                    ));
                }
            }
        }
        return list;
    }

    public boolean eliminarEquipo(String idEquipo) {
        String sql = "DELETE FROM equipos WHERE id_equipo = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idEquipo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarEstadoEquipo(String idEquipo, String nuevoEstado) {
        String sql = "UPDATE equipos SET estado_actual = ? WHERE id_equipo = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setString(2, idEquipo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, Object>> obtenerHistorialMantenimiento(String idEquipo) {
        List<Map<String, Object>> historial = new ArrayList<>();
        String sql = "SELECT b.fecha_realizacion, b.tipo_mantenimiento, b.observaciones, u.nombre AS tecnico " +
                     "FROM bitacora_mantenimiento b " +
                     "JOIN usuarios u ON b.id_usuario = u.id_usuario " +
                     "WHERE b.id_equipo = ? ORDER BY b.fecha_realizacion DESC, b.id_mantenimiento DESC";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idEquipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new java.util.HashMap<>();
                    java.sql.Date fecha = rs.getDate("fecha_realizacion");
                    map.put("fecha", fecha != null ? fecha.toString() : "");
                    map.put("tipo", rs.getString("tipo_mantenimiento"));
                    map.put("observaciones", rs.getString("observaciones"));
                    map.put("tecnico", rs.getString("tecnico"));
                    historial.add(map);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historial;
    }
}
