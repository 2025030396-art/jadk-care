package com.mycompany.jadk_care.controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.sql.Date;

import com.mycompany.jadk_care.dao.EquipoDAO;
import com.mycompany.jadk_care.modelo.Equipo;
import com.mycompany.jadk_care.modelo.Especificacion;
import com.mycompany.jadk_care.modelo.Almacenamiento;

@RestController
@RequestMapping("/api/equipos")
public class EquipoController {

    private final EquipoDAO equipoDAO = new EquipoDAO();

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listAll() {
        List<Equipo> list = equipoDAO.obtenerTodosEquipos();
        List<Map<String, Object>> response = new ArrayList<>();
        
        for (Equipo eq : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", eq.getIdEquipo());
            map.put("status", eq.getEstadoActual());
            map.put("laboratorio", eq.getLaboratorio());
            map.put("numPuesto", eq.getNumPuesto());
            map.put("fechaUltimoMan", eq.getFechaUltimoMan() != null ? eq.getFechaUltimoMan().toString() : "");
            map.put("fechaProxMan", eq.getFechaProxMan() != null ? eq.getFechaProxMan().toString() : "");
            
            Especificacion spec = eq.getEspecificaciones();
            if (spec != null) {
                map.put("cpu", spec.getCpuModelo());
                map.put("mobo", spec.getMotherboard());
                map.put("ram", spec.getRamCapacidad());
                map.put("gpu", spec.getGpuModelo());
                map.put("mac", spec.getMacAddress());
                map.put("ip", spec.getIpAddress());
                
                List<Map<String, String>> storages = new ArrayList<>();
                for (Almacenamiento alm : spec.getAlmacenamientos()) {
                    Map<String, String> almItem = new HashMap<>();
                    almItem.put("type", alm.getTipo());
                    almItem.put("cap", alm.getCapacidad());
                    almItem.put("use", alm.getUso());
                    storages.add(almItem);
                }
                map.put("storage", storages);
            }
            response.add(map);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @SuppressWarnings("unchecked")
    public ResponseEntity<Map<String, Object>> saveOrUpdate(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        String id = (String) request.get("id");
        String status = (String) request.get("status");
        if (status == null || status.trim().isEmpty()) {
            status = "green";
        }
        String cpu = (String) request.get("cpu");
        String mobo = (String) request.get("mobo");
        String ram = (String) request.get("ram");
        String gpu = (String) request.get("gpu");
        String mac = (String) request.get("mac");
        String ip = (String) request.get("ip");
        
        String lab = (String) request.get("laboratorio");
        int numPuesto = 12;
        
        if (request.get("numPuesto") != null) {
            try {
                numPuesto = Integer.parseInt(request.get("numPuesto").toString());
            } catch (NumberFormatException e) {
                // Keep default
            }
        } else {
            // Inteligencia del sistema: Extraer el laboratorio y el puesto a partir del ID
            // Ej. "PC-LAB-05" o "PC-LAB1-016"
            if (id != null && id.contains("-")) {
                String[] parts = id.split("-");
                String lastPart = parts[parts.length - 1];
                try {
                    // Extraer el puesto: "05" -> 5, "016" -> 16
                    numPuesto = Integer.parseInt(lastPart);
                    
                    // Extraer el laboratorio si no se mandó uno
                    if (lab == null || lab.trim().isEmpty()) {
                        if (parts.length >= 3) {
                            String labPart = parts[parts.length - 2].toUpperCase(); // Ej. "LAB" o "LAB1"
                            String numLab = labPart.replace("LAB", "").replace("-", "").trim();
                            if (numLab.isEmpty()) {
                                lab = "Laboratorio Principal";
                            } else {
                                lab = "Laboratorio " + numLab;
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    // Si el final no es un número (ej. PC-LAP-REAL), mantenemos los por defecto
                }
            }
        }

        // Fallback final para el laboratorio
        if (lab == null || lab.trim().isEmpty()) {
            lab = "Laboratorio Principal";
        }

        List<Almacenamiento> storageList = new ArrayList<>();
        List<Map<String, String>> storageReq = (List<Map<String, String>>) request.get("storage");
        if (storageReq != null) {
            for (Map<String, String> sMap : storageReq) {
                storageList.add(new Almacenamiento(
                    id,
                    sMap.get("type"),
                    sMap.get("cap"),
                    sMap.get("use")
                ));
            }
        }

        Especificacion spec = new Especificacion(id, cpu, mobo, ram, gpu, storageList, mac, ip);
        
        // Check if it already exists
        List<Equipo> all = equipoDAO.obtenerTodosEquipos();
        boolean exists = false;
        Equipo existingEquipo = null;
        for (Equipo e : all) {
            if (e.getIdEquipo().equalsIgnoreCase(id)) {
                exists = true;
                existingEquipo = e;
                break;
            }
        }

        boolean success;
        Date now = new Date(System.currentTimeMillis());
        Date next = new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000); // 30 days later

        if (exists) {
            Equipo eq = new Equipo(
                id,
                lab,
                numPuesto,
                status,
                existingEquipo.getFechaUltimoMan(),
                existingEquipo.getFechaProxMan(),
                spec
            );
            success = equipoDAO.actualizarEquipoConEspecificaciones(eq);
        } else {
            Equipo eq = new Equipo(
                id,
                lab,
                numPuesto,
                status,
                now,
                next,
                spec
            );
            success = equipoDAO.registrarEquipoConEspecificaciones(eq);
        }

        if (success) {
            response.put("success", true);
            response.put("message", "Equipo guardado correctamente.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Error al guardar el equipo.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        boolean success = equipoDAO.eliminarEquipo(id);
        
        if (success) {
            response.put("success", true);
            response.put("message", "Equipo eliminado correctamente.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Error al eliminar el equipo.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}/mantenimientos")
    public ResponseEntity<List<Map<String, Object>>> getMantenimientos(@PathVariable String id) {
        return ResponseEntity.ok(equipoDAO.obtenerHistorialMantenimiento(id));
    }
}
