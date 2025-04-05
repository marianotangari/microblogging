# Microblogging App
Microblogging App - Java 17 | Spring Boot | Docker Compose | RabbitMQ | Redis

# Configuración para correr la aplicación

## Instalación de Docker

### Windows

[Instalación de Docker Desktop en Windows (con WSL 2)](https://learn.microsoft.com/es-es/windows/wsl/tutorials/wsl-containers)

### MacOS

[Instalación de Docker Desktop en MacOS](https://docs.docker.com/desktop/setup/install/mac-install/)

## Correr Microblogging App

1. Clonar el proyecto desde Github (o usando un IDE como IntelliJ).
2. Navegar en la consola (PowerShell en Windows, Terminal en MacOS) hasta la ubicación la raíz de la carpeta del proyecto microblogging.
3. Ejecutar el comando `docker compose up --build` para construir las imágenes y levantar los contenedores. En lo sucesivo, puede ejecutarse el comando `docker compose up -d` para levantar los contenedores.
4. En Chrome, navegar a `http://localhost:8080/health`. Si la aplicación se encuentra corriendo, se verá el mensaje `"Application is up and running"`.
5. Para detener Docker, desde la raíz de la carpeta del proyecto de microblogging, correr el siguiente comando: `docker compose down`.
6. Para correr el test de integración, con Maven instalado en su máquina local, una vez que la aplicación se encuentre corriendo en Docker, puede ejecutarse el test por consola corriendo el comando `mvn test -Dtest=CreatePostAndGetTimelineIntegrationTest` (esto puede hacerse asimismo en IntelliJ).
7. Si se desea probar los endpoints manualmente, con el proyecto corriendo en Docker, se puede acceder a http://localhost:8080/swagger-ui/index.html y testear los endpoints en la interfaz de Swagger.