# Ockly | Sistema administrativo de papelería

Actividad de Arquitectura de Software · Institución Universitaria Pascual Bravo · 2026.

**Integrantes:** Sebastián Pineda, Melissa Betancur Osorio y Miguel Ángel Noreña Cano.

Aplicación web MVC con **Spring Boot + Thymeleaf + JPA + MySQL**. Permite gestionar **categorías** y **productos** con una asociación **uno a muchos**.

> **Alcance:** este repositorio corresponde a la actividad específica de MVC con Spring Boot del docente. Aunque en el taller de estilos se exploró Tuberías y Filtros, para esta actividad se aplica **MVC**, tal como lo exige su enunciado.

## Funcionalidades

- Crear, listar, consultar individualmente, editar y eliminar **categorías**.
- Crear, listar, consultar individualmente, editar y eliminar **productos**.
- Asociar cada producto con una categoría existente (clave foránea relacional).
- Validar nombre, precio positivo, stock no negativo y categoría obligatoria.
- Impedir borrar una categoría que tenga productos relacionados.
- Navegar mediante formularios y tablas web con diseño responsivo.
- Guardar los cambios de forma persistente en **MySQL**, sin datos ficticios en la interfaz.

## Requisitos de instalación

| Programa | Versión o uso |
|---|---|
| Java JDK | 17 o 21 |
| Apache Maven | 3.6.3 o posterior |
| Spring Boot | 3.5.16 (definida en `pom.xml`) |
| MySQL | MySQL 8, o MariaDB de XAMPP reciente |
| XAMPP | Si se utiliza MySQL local y phpMyAdmin |
| IDE | Visual Studio Code con Extension Pack for Java, IntelliJ IDEA o Eclipse |
| Navegador | Chrome, Firefox, Edge u otro moderno |

## 1. Preparar base de datos (obligatorio)

1. En XAMPP, inicia **MySQL** (Apache opcional si quieres abrir phpMyAdmin).
2. Abre `http://localhost/phpmyadmin` y entra a la pestaña **SQL**.
3. Ejecuta **todo el contenido de** [`database/schema.sql`](database/schema.sql). Esto crea `ockly_db` y las tablas `categorias` y `productos` con clave foránea.
4. El script no inserta registros: se crean mediante los formularios de la aplicación.
5. En una instalación MySQL independiente, también puedes ejecutar `mysql -u root -p < database/schema.sql` desde la terminal.

**Parámetros por defecto** de [`application.properties`](src/main/resources/application.properties):

| Propiedad | Valor inicial |
|---|---|
| Motor | MySQL |
| Base de datos | `ockly_db` |
| Puerto de MySQL | `3306` |
| Usuario | `root` |
| Contraseña | Vacía por defecto; usa `DB_PASSWORD` si tu MySQL exige una |
| Puerto web | `8080` |

Si MySQL tiene otra contraseña, define variables de entorno antes de arrancar. No escribas claves reales en GitHub:

**PowerShell (Windows):**
```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="tu_clave_local"
mvn spring-boot:run
```

**Bash (Linux/macOS):**
```bash
export DB_USERNAME=root
export DB_PASSWORD='tu_clave_local'
mvn spring-boot:run
```

Para un puerto/servidor distinto, usa `DB_URL`, por ejemplo:
`jdbc:mysql://localhost:3307/ockly_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Bogota`

## 2. Ejecutar

1. Descarga o clona el código y abre la carpeta que contiene `pom.xml`.
2. Abre una terminal y comprueba `java -version` y `mvn -version`.
3. Verifica que MySQL esté ejecutándose y que hayas creado las tablas con `schema.sql`.
4. Ejecuta:

```bash
mvn clean spring-boot:run
```

5. Abre **http://localhost:8080/**.
6. Primero crea una categoría; después crea un producto que pertenezca a ella.

**Alternativa JAR:**
```bash
mvn clean package
java -jar target/ockly-1.0.0.jar
```

La app **requiere** que MySQL esté disponible. Si falla con `Access denied`, revisa usuario/clave. Si falla con `Unknown database`, ejecuta `database/schema.sql`. Si falla la validación del esquema de Hibernate, asegúrate de haber ejecutado **todo** el script SQL.

## 3. Direcciones URL

| Funcionalidad | URL |
|---|---|
| Inicio / panel | http://localhost:8080/ |
| Listar categorías | http://localhost:8080/categorias |
| Registrar categoría | http://localhost:8080/categorias/nuevo |
| Consultar categoría | http://localhost:8080/categorias/1 |
| Editar categoría | http://localhost:8080/categorias/1/editar |
| Listar productos | http://localhost:8080/productos |
| Registrar producto | http://localhost:8080/productos/nuevo |
| Consultar producto | http://localhost:8080/productos/1 |
| Editar producto | http://localhost:8080/productos/1/editar |

Los identificadores numéricos de ejemplo deben sustituirse por los que realmente existan en la base de datos. El borrado se ejecuta mediante `POST` desde los formularios.

## 4. Guía de uso

### Categorías
1. Ve a **Categorías → Nueva categoría**.
2. Digita un nombre y opcionalmente una descripción; pulsa **Guardar categoría**.
3. Pulsa **Consultar** para ver los productos asociados.
4. Pulsa **Editar** para actualizar y **Eliminar** para borrar (solo si no tiene productos).

### Productos
1. Registra primero al menos una categoría.
2. Ve a **Productos → Nuevo producto** y completa nombre, precio, stock y categoría.
3. Pulsa **Guardar producto** y verifica que aparece en la tabla.
4. Usa **Consultar**, **Editar** y **Eliminar** para gestionar cada registro.
5. Visita la categoría asociada y comprueba la relación entre ambas tablas.

## 5. Arquitectura y estructura

Consulta [`docs/arquitectura.md`](docs/arquitectura.md) para el diagrama MVC y el modelo entidad-relación. Resumen:

```text
src/main/java/co/edu/pascualbravo/ockly/
├── controller/    # Controlador: peticiones web y navegación
├── model/         # Modelo: entidades Categoria y Producto
├── dto/           # Validación de los campos del formulario
├── service/       # Reglas de negocio y operaciones CRUD
├── repository/    # Persistencia JPA hacia MySQL
└── OcklyApplication.java
src/main/resources/
├── templates/     # Vista: HTML Thymeleaf
├── static/css/    # Estilos visuales
└── application.properties
database/schema.sql # DDL MySQL con FK
src/test/          # Pruebas de integración (H2 solo en pruebas)
```

## 6. Pruebas automatizadas

```bash
mvn test
```

Se ejecuta `OcklyMvcIntegrationTest`, que verifica vía MockMvc el CRUD de las dos entidades, la relación uno-a-muchos, las restricciones de eliminación y validaciones. **H2 solo se utiliza en pruebas**; la aplicación normal usa MySQL.

## 7. Evidencias para la entrega

El docente exige capturas **reales** del funcionamiento. Con el sistema ejecutándose y datos de prueba creados, incluye en el documento técnico capturas de:

1. Página principal con cantidades de categorías y productos.
2. Listado de categorías y su formulario de creación/edición.
3. Listado de productos y su formulario con selección de categoría.
4. Detalle de una categoría mostrando sus productos asociados.
5. Consulta SQL de las tablas en phpMyAdmin para evidenciar persistencia.
6. Resultado de `mvn test` o de la compilación.

**Importante:** ninguna captura debe representarse como evidencia real si no fue obtenida desde la aplicación ejecutándose. El documento local de la entrega contiene los apartados de evidencias pendientes de capturar durante la demostración.

## Limitaciones académicas

Este módulo inicial cubre únicamente catálogo de productos y categorías. No implementa usuarios, ventas ni pagos. No hay autenticación porque no se solicita en esta actividad; no exponer públicamente la aplicación sin añadir controles de seguridad.
