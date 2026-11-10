# Arquitectura MVC de Ockly

**Estilo:** Modelo–Vista–Controlador (MVC) con separación adicional Controller → Service → Repository. Esta elección responde a la nueva actividad de Spring Boot; no reemplaza el análisis conceptual anterior de Tuberías y Filtros.

```mermaid
flowchart LR
    U[Usuario / Navegador] -->|GET y POST HTTP| C[Controller<br/>Inicio, Categoria y Producto]
    C -->|invoca casos de uso| S[Service<br/>Validación operativa y reglas]
    S -->|consultas y persistencia| R[Repository<br/>Spring Data JPA]
    R -->|JDBC / SQL| DB[(MySQL<br/>ockly_db)]
    C -->|Model| V[Vista<br/>Thymeleaf HTML]
    V -->|respuesta HTML| U
    M[Model / Entidades<br/>Categoria y Producto] -.->|mapeo JPA| R
    M -.->|objetos de dominio| S
```

La **Vista** contiene plantillas en `src/main/resources/templates`; el **Controlador** procesa las rutas HTTP y entrega datos a la vista; el **Modelo** define las dos entidades relacionadas. `service` concentra reglas de negocio; `repository` aísla el acceso a MySQL.

## Relación implementada

```mermaid
erDiagram
    CATEGORIAS ||--o{ PRODUCTOS : contiene
    CATEGORIAS {
       BIGINT id PK
       VARCHAR nombre
       VARCHAR descripcion
    }
    PRODUCTOS {
       BIGINT id PK
       VARCHAR nombre
       VARCHAR descripcion
       DECIMAL precio
       INT stock
       BIGINT categoria_id FK
    }
```

Una categoría tiene cero o muchos productos. Cada producto pertenece obligatoriamente a una categoría. La base de datos restringe borrar categorías que tengan productos asociados.

## Estructura de paquetes

- `controller/`: controladores MVC y endpoints HTTP.
- `model/`: entidades JPA `Categoria` y `Producto`.
- `dto/`: formulario `ProductoForm` con validaciones y `categoriaId`.
- `service/`: operaciones CRUD y verificación de la regla de borrado.
- `repository/`: interfaces `JpaRepository` para persistencia.
- `templates/`: pantallas Thymeleaf; `static/`: estilos CSS.
- `database/`: script SQL de creación de la base de datos.
- `src/test/`: pruebas de integración con H2 en perfil aislado.
