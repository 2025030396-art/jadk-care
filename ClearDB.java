import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ClearDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/jadk_care?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String pass = "mataabuelas9262";
        
        try (Connection con = DriverManager.getConnection(url, user, pass);
             Statement stmt = con.createStatement()) {
            
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
            stmt.executeUpdate("TRUNCATE TABLE bitacora_mantenimiento;");
            stmt.executeUpdate("TRUNCATE TABLE alertas;");
            stmt.executeUpdate("TRUNCATE TABLE almacenamientos;");
            stmt.executeUpdate("TRUNCATE TABLE especificaciones;");
            stmt.executeUpdate("TRUNCATE TABLE equipos;");
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 1;");
            
            System.out.println("Base de datos limpiada correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
