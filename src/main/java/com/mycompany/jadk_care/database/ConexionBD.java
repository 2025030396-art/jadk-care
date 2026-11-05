package com.mycompany.jadk_care.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Leemos las variables de entorno individuales de Railway
    private static final String HOST = System.getenv("MYSQLHOST") != null ? System.getenv("MYSQLHOST") : "localhost";
    private static final String PORT = System.getenv("MYSQLPORT") != null ? System.getenv("MYSQLPORT") : "3306";
    // Si estamos en Railway, la base de datos se llama "railway". Si estamos en local, se llama "jadk_care"
    private static final String DB_NAME = !HOST.equals("localhost") ? "railway" : "jadk_care";
    
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;
    private static final String USER = System.getenv("MYSQLUSER") != null ? System.getenv("MYSQLUSER") : "root"; 
    private static final String PASSWORD = System.getenv("MYSQLPASSWORD") != null ? System.getenv("MYSQLPASSWORD") : "mataabuelas9262"; 

    public static Connection obtenerConexion() {
        Connection conexion = null;
        try {
            // Registrar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("¡Conexión exitosa a JADK Care BD!");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el driver de MySQL.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos.");
            e.printStackTrace();
        }
        return conexion;
    }
}
