package com.mycompany.jadk_care.modelo;

public interface Mantenible {
    // Método abstracto de interfaz
    String obtenerEstatusMantenimiento();
    void actualizarEstatus(String nuevoEstatus);
}
