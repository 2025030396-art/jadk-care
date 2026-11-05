package com.mycompany.jadk_care;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import com.mycompany.jadk_care.dao.UsuarioDAO;
import com.mycompany.jadk_care.dao.EquipoDAO;
import com.mycompany.jadk_care.dao.AlertaDAO;
import com.mycompany.jadk_care.modelo.Usuarios;
import com.mycompany.jadk_care.modelo.Equipo;
import com.mycompany.jadk_care.modelo.Especificacion;
import com.mycompany.jadk_care.modelo.Alerta;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.sql.Date;
import java.util.ArrayList;

import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Jadk_care {
    
    public static void main(String[] args) {
        SpringApplication.run(Jadk_care.class, args);
    }

    @Bean
    public CommandLineRunner runSimulacion() {
        return args -> {
            System.out.println("=================================================");
            System.out.println("   SERVIDOR INICIADO: JADK CARE (Spring Boot)    ");
            System.out.println("=================================================\n");
            // La simulación anterior de consola puede ejecutarse aquí si se desea,
            // pero el servidor ya está escuchando peticiones HTTP en el puerto 8080.
        };
    }

    private static String obtenerMacLaptop() {
        try {
            Enumeration<NetworkInterface> redes = NetworkInterface.getNetworkInterfaces();
            while (redes.hasMoreElements()) {
                NetworkInterface red = redes.nextElement();
                byte[] mac = red.getHardwareAddress();
                if (mac != null && mac.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    return sb.toString();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "00-00-00-00-00-00";
    }
}
