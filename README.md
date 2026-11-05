# 🩺 JADK Care

Sistema integral de gestión de mantenimiento, inventario y monitoreo de telemetría de hardware para equipos de cómputo.

[![Ver Demo en Vivo (GitHub Pages)](https://img.shields.io/badge/Demo_Web-GitHub_Pages-blue?style=for-the-badge&logo=github)](https://2025030396-art.github.io/jadk-care/)
[![Open in GitHub Codespaces](https://img.shields.io/badge/Codespaces-Ejecutar_Java_en_la_Nube-purple?style=for-the-badge&logo=github)](https://codespaces.new/2025030396-art/jadk-care)

---

## 🌐 1. Probar en Vivo (Sin Instalar Nada)

Puedes probar la aplicación completa directamente en tu navegador desde el siguiente enlace funcional:

👉 **[https://2025030396-art.github.io/jadk-care/](https://2025030396-art.github.io/jadk-care/)**

- **Usuario:** Cualquier correo o `2025030396@upsin.edu.mx`
- **Contraseña:** Cualquier contraseña (o déjalo en blanco y haz clic en *Ingresar*)
- Cuenta con simulación interactiva de inventario, alertas, reportes descargables y modo oscuro/claro.

---

## ☁️ 2. Ejecutar el Backend Java en la Nube (GitHub Codespaces)

Si necesitas ejecutar el backend Java Spring Boot real sin instalar nada en tu computadora:

1. Haz clic en el botón superior **Codespaces** (o en [codespaces.new/2025030396-art/jadk-care](https://codespaces.new/2025030396-art/jadk-care)).
2. GitHub creará una máquina virtual en la nube con Java 17 y Maven listos.
3. En la terminal integrada dentro de GitHub, ejecuta:
   ```bash
   ./mvnw spring-boot:run
   ```
4. GitHub abrirá automáticamente la pestaña con el servidor web en ejecución.

---

## 🚀 3. Tecnologías Utilizadas

- **Backend:** Java 17, Spring Boot 3.2.5
- **Base de Datos:** MySQL 8.x
- **Monitoreo Hardware:** OSHI (Operating System and Hardware Information)
- **Frontend:** HTML5, CSS3, JavaScript (Fetch API + Local Storage Fallback)
- **Reportes:** OpenPDF, JFreeChart
- **Seguridad:** jBCrypt para hashing de contraseñas
- **Gestor de dependencias:** Apache Maven

---

## ⚙️ 4. Configuración e Instalación Local (Opcional)

1. **Base de Datos:**
   - Crear una base de datos en MySQL llamada `jadk_care`.
   - Ejecutar el script `jadkcare.sql`.

2. **Variables de Entorno (Opcional):**
   - `MYSQLHOST` (por defecto: `localhost`)
   - `MYSQLPORT` (por defecto: `3306`)
   - `MYSQLUSER` (por defecto: `root`)
   - `MYSQLPASSWORD` (contraseña de tu MySQL)

3. **Ejecución:**
   ```bash
   mvn clean spring-boot:run
   ```
4. **Acceso Web:**
   Abre tu navegador en `http://localhost:8080`
