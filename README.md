# 🩺 JADK Care

Sistema integral de gestión de mantenimiento, inventario y monitoreo de telemetría de hardware para equipos de cómputo.

## 🚀 Tecnologías Utilizadas

- **Backend:** Java 17, Spring Boot 3.2.5
- **Base de Datos:** MySQL 8.x
- **Monitoreo Hardware:** OSHI (Operating System and Hardware Information)
- **Frontend:** HTML5, CSS3, JavaScript (Fetch API)
- **Reportes:** OpenPDF, JFreeChart
- **Seguridad:** jBCrypt para hashing de contraseñas
- **Gestor de dependencias:** Apache Maven

## 📁 Estructura del Proyecto

- `src/main/java/com/mycompany/jadk_care/`: Código fuente del servidor (Controladores, DAO, Modelos, Base de datos, Servicios).
- `src/main/resources/static/`: Vistas de usuario (`index.html`, `login.html`, `perfil.html`).
- `jadkcare.sql`: Script SQL con la estructura e inicialización de la base de datos.
- `pom.xml`: Configuración de dependencias de Maven.

## ⚙️ Configuración e Instalación Local

1. **Base de Datos:**
   - Crear una base de datos en MySQL llamada `jadk_care`.
   - Ejecutar el script `jadkcare.sql` para crear las tablas y datos iniciales.

2. **Variables de Entorno (Opcional):**
   - `MYSQLHOST` (por defecto: `localhost`)
   - `MYSQLPORT` (por defecto: `3306`)
   - `MYSQLUSER` (por defecto: `root`)
   - `MYSQLPASSWORD` (contraseña de tu MySQL local)

3. **Ejecución:**
   ```bash
   mvn clean spring-boot:run
   ```
   O compilar el paquete JAR:
   ```bash
   mvn clean package
   java -jar target/jadk_care-1.0-SNAPSHOT.jar
   ```

4. **Acceso Web:**
   Abre tu navegador en `http://localhost:8080`

