package com.mycompany.jadk_care.modelo;

import java.sql.Date;

public class Equipo extends DispositivoElectronico implements Mantenible {
    private String idEquipo; 
    private String laboratorio;
    private int numPuesto;
    private String estadoActual;
    private Date fechaUltimoMan;
    private Date fechaProxMan;
    private Especificacion especificaciones; 


    public Equipo(String idEquipo, String laboratorio, int numPuesto, String estadoActual, 
                  Date fechaUltimoMan, Date fechaProxMan, Especificacion especificaciones) {
        super("Computadora de Laboratorio"); // Llama al constructor de la clase abstracta Padre
        this.idEquipo = idEquipo;
        this.laboratorio = laboratorio;
        this.numPuesto = numPuesto;
        this.estadoActual = estadoActual;
        this.fechaUltimoMan = fechaUltimoMan;
        this.fechaProxMan = fechaProxMan;
        this.especificaciones = especificaciones;
    }

    public String getIdEquipo() {
        return idEquipo; 
    }
    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo; 
    }

    public String getLaboratorio() {
        return laboratorio; 
    }
    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio; 
    }

    public int getNumPuesto() {
        return numPuesto; 
    }
    public void setNumPuesto(int numPuesto) {
        this.numPuesto = numPuesto; 
    }

    public String getEstadoActual() {
        return estadoActual; 
    }
    public void setEstadoActual(String estadoActual) {
        this.estadoActual = estadoActual; 
    }

    public Date getFechaUltimoMan() {
        return fechaUltimoMan; 
    }
    public void setFechaUltimoMan(Date fechaUltimoMan) {
        this.fechaUltimoMan = fechaUltimoMan; 
    }

    public Date getFechaProxMan() {
        return fechaProxMan; 
    }
    public void setFechaProxMan(Date fechaProxMan) {
        this.fechaProxMan = fechaProxMan; 
    }

    public Especificacion getEspecificaciones() {
        return especificaciones; 
    }
    public void setEspecificaciones(Especificacion especificaciones) {
        this.especificaciones = especificaciones; 
    }

    // ==========================================
    // IMPLEMENTACIÓN DE MÉTODOS DE LA CLASE PADRE (ABSTRACTA)
    // ==========================================
    @Override
    public String getIdentificadorUnico() {
        return "ID_SISTEMA_" + this.idEquipo;
    }

    // ==========================================
    // IMPLEMENTACIÓN DE MÉTODOS DE LA INTERFAZ
    // ==========================================
    @Override
    public String obtenerEstatusMantenimiento() {
        return this.estadoActual;
    }

    @Override
    public void actualizarEstatus(String nuevoEstatus) {
        this.estadoActual = nuevoEstatus;
    }
}
