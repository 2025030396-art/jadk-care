package com.mycompany.jadk_care.database;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.GlobalMemory;

public class LectorHardware {

    private static final SystemInfo si = new SystemInfo();
    private static final HardwareAbstractionLayer hal = si.getHardware();

    // 1. Leer el modelo exacto del Procesador (CPU)
    public static String obtenerModeloCPU() {
        CentralProcessor processor = hal.getProcessor();
        return processor.getProcessorIdentifier().getName();
    }

    // 2. Leer la marca/modelo de la placa madre (Motherboard)
    public static String obtenerMotherboard() {
        ComputerSystem system = hal.getComputerSystem();
        return system.getManufacturer() + " " + system.getModel();
    }

    // 3. Leer la memoria RAM Total instalada (Convertida a GB)
    public static String obtenerRamTotal() {
        GlobalMemory memory = hal.getMemory();
        long totalBytes = memory.getTotal();
        double totalGB = (double) totalBytes / (1024 * 1024 * 1024);
        return String.format("%.2f GB", totalGB);
    }
}
