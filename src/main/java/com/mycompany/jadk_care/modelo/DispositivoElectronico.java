package com.mycompany.jadk_care.modelo;

public abstract class DispositivoElectronico {

    // Atributos protegidos (visibles para clases hijas)
    protected String fechaRegistroSistema;
    protected String tipoDispositivo;

    public DispositivoElectronico(String tipoDispositivo) {
        this.tipoDispositivo = tipoDispositivo;
        this.fechaRegistroSistema = java.time.LocalDate.now().toString();
    }

    // Método abstracto que las hijas deben implementar (Polimorfismo/Abstracción)
    public abstract String getIdentificadorUnico();

    // Métodos getter/setter de los atributos protegidos
    public String getFechaRegistroSistema() {
        return fechaRegistroSistema;
    }

    public void setFechaRegistroSistema(String fechaRegistroSistema) {
        this.fechaRegistroSistema = fechaRegistroSistema;
    }

    public String getTipoDispositivo() {
        return tipoDispositivo;
    }

    public void setTipoDispositivo(String tipoDispositivo) {
        this.tipoDispositivo = tipoDispositivo;
    }
}
