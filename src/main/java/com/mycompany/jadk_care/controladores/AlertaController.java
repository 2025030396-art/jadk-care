package com.mycompany.jadk_care.controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import com.mycompany.jadk_care.dao.AlertaDAO;
import com.mycompany.jadk_care.modelo.Alerta;

@RestController
@RequestMapping("/api")
public class AlertaController {

    private final AlertaDAO alertaDAO = new AlertaDAO();

    @GetMapping("/alertas")
    public ResponseEntity<List<Alerta>> listPendingAlerts() {
        return ResponseEntity.ok(alertaDAO.obtenerAlertasPendientes());
    }

    @PostMapping("/alertas/resolver")
    public ResponseEntity<Map<String, Object>> resolveAlert(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        String idEquipo = (String) request.get("idEquipo");
        String tipoMantenimiento = (String) request.get("tipoMantenimiento");
        String observaciones = (String) request.get("observaciones");
        String tecnicoNombre = (String) request.get("tecnicoNombre");

        if (idEquipo == null || tipoMantenimiento == null || tecnicoNombre == null) {
            response.put("success", false);
            response.put("message", "Faltan datos obligatorios.");
            return ResponseEntity.badRequest().body(response);
        }

        boolean success = alertaDAO.resolverAlertaYCrearBitacora(idEquipo, tipoMantenimiento, observaciones, tecnicoNombre);

        if (success) {
            response.put("success", true);
            response.put("message", "Alerta resuelta y bitácora de mantenimiento creada.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Error al resolver la alerta.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/mantenimientos/count")
    public ResponseEntity<Integer> getMantenimientosCount() {
        return ResponseEntity.ok(alertaDAO.obtenerTotalMantenimientos());
    }
}
