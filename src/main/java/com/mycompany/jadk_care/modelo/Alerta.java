package com.mycompany.jadk_care.modelo;

import java.sql.Timestamp;

public class Alerta {
    private int idAlerta;
    private String idEquipo;
    private String titulo;
    private String gravedad; // 'Baja', 'Media', 'Alta'
    private Timestamp fechaAlerta;
    private String estatus; // 'Pendiente', 'Solucionada'

    public Alerta(int idAlerta, String idEquipo, String titulo, String gravedad, Timestamp fechaAlerta,
            String estatus) {
        this.idAlerta = idAlerta;
        this.idEquipo = idEquipo;
        this.titulo = titulo;
        this.gravedad = gravedad;
        this.fechaAlerta = fechaAlerta;
        this.estatus = estatus;
    }

    // Getters y Setters
    public int getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(int idAlerta) {
        this.idAlerta = idAlerta;
    }

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGravedad() {
        return gravedad;
    }

    public void setGravedad(String gravedad) {
        this.gravedad = gravedad;
    }

    public Timestamp getFechaAlerta() {
        return fechaAlerta;
    }

    public void setFechaAlerta(Timestamp fechaAlerta) {
        this.fechaAlerta = fechaAlerta;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}
