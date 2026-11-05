package com.mycompany.jadk_care.modelo;

import java.util.ArrayList;
import java.util.List;

public class Especificacion {
    private String idEquipo;
    private String cpuModelo;
    private String motherboard;
    private String ramCapacidad;
    private String gpuModelo;
    private List<Almacenamiento> almacenamientos = new ArrayList<>();
    private String macAddress;
    private String ipAddress;





    // New constructor using Almacenamiento list
    public Especificacion(String idEquipo, String cpuModelo, String motherboard, String ramCapacidad,
            String gpuModelo, List<Almacenamiento> almacenamientos,
            String macAddress, String ipAddress) {
        this.idEquipo = idEquipo;
        this.cpuModelo = cpuModelo;
        this.motherboard = motherboard;
        this.ramCapacidad = ramCapacidad;
        this.gpuModelo = gpuModelo;
        this.almacenamientos = almacenamientos != null ? almacenamientos : new ArrayList<>();
        this.macAddress = macAddress;
        this.ipAddress = ipAddress;
    }

    // Getters y Setters
    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getCpuModelo() {
        return cpuModelo;
    }

    public void setCpuModelo(String cpuModelo) {
        this.cpuModelo = cpuModelo;
    }

    public String getMotherboard() {
        return motherboard;
    }

    public void setMotherboard(String motherboard) {
        this.motherboard = motherboard;
    }

    public String getRamCapacidad() {
        return ramCapacidad;
    }

    public void setRamCapacidad(String ramCapacidad) {
        this.ramCapacidad = ramCapacidad;
    }

    public String getGpuModelo() {
        return gpuModelo;
    }

    public void setGpuModelo(String gpuModelo) {
        this.gpuModelo = gpuModelo;
    }

    public List<Almacenamiento> getAlmacenamientos() {
        return almacenamientos;
    }

    public void setAlmacenamientos(List<Almacenamiento> almacenamientos) {
        this.almacenamientos = almacenamientos;
    }



    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
