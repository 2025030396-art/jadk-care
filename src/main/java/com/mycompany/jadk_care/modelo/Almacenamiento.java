package com.mycompany.jadk_care.modelo;

public class Almacenamiento {
    private int idAlmacenamiento;
    private String idEquipo;
    private String tipo;
    private String capacidad;
    private String uso;



    // Constructor without idAlmacenamiento (for inserts)
    public Almacenamiento(String idEquipo, String tipo, String capacidad, String uso) {
        this.idEquipo = idEquipo;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.uso = uso;
    }

    // Complete constructor
    public Almacenamiento(int idAlmacenamiento, String idEquipo, String tipo, String capacidad, String uso) {
        this.idAlmacenamiento = idAlmacenamiento;
        this.idEquipo = idEquipo;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.uso = uso;
    }

    // Getters and Setters
    public int getIdAlmacenamiento() {
        return idAlmacenamiento;
    }

    public void setIdAlmacenamiento(int idAlmacenamiento) {
        this.idAlmacenamiento = idAlmacenamiento;
    }

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(String capacidad) {
        this.capacidad = capacidad;
    }

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }
}
