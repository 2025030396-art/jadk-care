package com.mycompany.jadk_care.servicios;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mycompany.jadk_care.dao.EquipoDAO;
import com.mycompany.jadk_care.dao.AlertaDAO;
import com.mycompany.jadk_care.modelo.Equipo;

import java.util.List;
import java.util.Random;

@Service
public class SimuladorTelemetriaService {

    private final EquipoDAO equipoDAO = new EquipoDAO();
    private final AlertaDAO alertaDAO = new AlertaDAO();
    private final Random random = new Random();

    @Scheduled(fixedDelay = 15000) // Se ejecuta cada 15 segundos
    public void simularDesgaste() {
        List<Equipo> equipos = equipoDAO.obtenerTodosEquipos();
        if (equipos == null || equipos.isEmpty()) {
            return;
        }

        // Seleccionamos un equipo al azar
        Equipo equipo = equipos.get(random.nextInt(equipos.size()));
        String estadoActual = equipo.getEstadoActual();

        // Si es verde, tiene un 50% de probabilidad de pasar a amarillo
        if ("green".equals(estadoActual) && random.nextBoolean()) {
            equipoDAO.actualizarEstadoEquipo(equipo.getIdEquipo(), "yellow");
            
            String[] alertasPreventivas = {
                "Temperatura en reposo elevada",
                "Horas de encendido al límite (Power-on Hours)",
                "Uso de CPU constantemente alto en reposo"
            };
            String titulo = alertasPreventivas[random.nextInt(alertasPreventivas.length)];
            alertaDAO.generarAlertaAutomatica(equipo.getIdEquipo(), titulo, "Media");
            System.out.println("[SIMULADOR] Equipo " + equipo.getIdEquipo() + " degradado a Mantenimiento (Amarillo).");
        } 
        // Si es amarillo, tiene un 30% de probabilidad de pasar a rojo
        else if ("yellow".equals(estadoActual) && random.nextInt(100) < 30) {
            equipoDAO.actualizarEstadoEquipo(equipo.getIdEquipo(), "red");
            
            String[] alertasCriticas = {
                "Picos de temperatura críticos (Thermal Throttling)",
                "Errores de Memoria (BSOD detectado)",
                "Sectores defectuosos en el disco (Emergencia)"
            };
            String titulo = alertasCriticas[random.nextInt(alertasCriticas.length)];
            alertaDAO.generarAlertaAutomatica(equipo.getIdEquipo(), titulo, "Alta");
            System.out.println("[SIMULADOR] Equipo " + equipo.getIdEquipo() + " degradado a Crítico (Rojo).");
        }
    }
}
