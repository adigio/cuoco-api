
## Requerimientos

Preferentemente utilizar IntelliJ IDEA, ya que el siguiente readme está basado en ese IDE.

Instalar y configurar Java 17 (Se puede usar la JDK de Oracle o OpenJDK. Si necesitan diferentes versiones de Java por otros proyectos, pueden usar un gestor de SDKs como [SDKman](https://sdkman.io/))

MySQL

Gradle para la consola (Opcional)

## Configuración

- Abrir el editor de base de datos y crear un nuevo schema que se llame `planicocina`.

- Clonar el repositorio

    ```bash
    git clone https://github.com/adigio/planicocina-api
    ```

- Abrir el proyecto con el IDE y luego el archivo build.gradle. Es probable que el IDE inicie la instalación de dependencias automaticamente. En caso de no ejecutar, nos aparecerá el elefante de Gradle con la opción `Sync Gradle Changes`

  (En caso de que no lo haga, verificar la versión de Java en el proyecto dirigiendonos a: `Files > Project Structure` y cambiando SDK por la version 17 correspondiente)

- Dentro del proyecto ir a:

  `src/main/resources/application.yml`

  Configurar el username y password correspondiente a nuestra base de datos

- En el mismo application.yml podemos cambiar la propiedad `jpa.hibernate.update` que veremos como `update` a `create`. El mismo creará las tablas a partir de las entidades en la carpeta Model.

## Iniciar el proyecto

Si utilizamos la version profesional del IDE es probable que la configuración la tome automaticamente en la parte superior del ide, para lo que solo debemos ejecutar.

Si la configuración no aparece, debemos hacer lo siguiente:

- Ir a Edit Configurations
- En el + agregamos un nuevo build que sea Gradle
- En `Run` agregamos la opcion `bootRun`
- En Gradle Project debe aparecer planicocina-api
- Ejecutar Run (preferentemente Debug)

Si tenemos un error de Java al tratar de ejecutar, es posible que debamos configurar dentro de

`Files > Settings > Buil, Execution, Deploymen > Build Tools > Gradle`

y cambiar Gradle JVM por Java 17

### Pruebas sobre el ambiente

Si todo fue correcto y levanto sin errores, podemos crear un usuario para probar el funcionamiento. Para lo cual usaremos Postman ejecutando los siguientes endpoints:

- [POST] localhost:8080/auth/register

    ```json
    body del request

    {
        "username": "plani",
        "password": "cocina123"
    }
    ````

- Si devuelve `Usuario registrado correctamente` entonces hacemos login con

  [POST] localhost:8080/auth/login

    ```json
    body del request

    {
        "username": "plani",
        "password": "cocina123"
    }
    ````

- Esto nos dara la confirmacion que funcionó tanto el guardado como el login. Tambien devolverá un token que debemos copiar. Para probar que el token funciona, podemos hacer la prueba con:

  [GET] localhost:8080/test

  Pero cambiando en la parte de Auth (o Headers) por

    ```json
        Authorization: Bearer <nuestro-token>
    ````

### Estructura de carpetas

Se utiliza una clásica estructura MVC:

```
├── config                      // Configuraciones propias de Spring
├── controller                  // Controladores REST
│   └── DTO                     // Los DTOs son los contenedores de info que
|                                  recibiremos y/o devolveremos
├── model                       // Modelos conectados a la DB con Hibernate
├── repository                  // Repositorios JPA* y JDBC**
├── service                     // Servicios (Interfaces)
│   └── impl                    // Implementaciones de los servicios
└── MiAppApplication.java       // Main app
```

    * JPA es una herramienta de Spring que nos da herramientas para facilitar las consultas a base de datos, extiende como una interface y nos puede reducir el trabajo de consultas, abrir sesiones y otros
    ** JDBC es el conector de Spring con la base de datos, nos sirve para ejecutar scripts en texto plano, nos servirá para consultas muy complejas o donde necesitaremos priorizar la performance