# Guía paso a paso: cómo construí CarritoGT (Spring Boot)

> Hice esta guía para poder reproducir el proyecto desde cero entendiendo cada capa, no solo copiarla. Me
>, donde el catedrático puede pedir que
> explique o modifique cualquier parte en vivo.

## 0. Crear el proyecto base (Spring Initializr)

Entrar a https://start.spring.io (o usar el asistente del IDE) con esta configuración:

| Campo | Valor |
|---|---|
| Project | Maven |
| Language | Java |
| Spring Boot | 4.1.x |
| Group | `localhost.jecheverria` (o el tuyo) |
| Artifact | `carrito-compras` |
| Java | 21 |
| Dependencies | **Spring Web**, **Spring Data JPA**, **Thymeleaf**, **MySQL Driver**, **Spring Boot DevTools**, **Lombok**, **H2 Database** (scope test) |

Descargar el zip, descomprimirlo y abrirlo en el IDE. Hay que revisar que `pom.xml` tenga esas 7
dependencias, porque es lo primero que el catedrático puede pedir que explique ("¿por qué elegiste cada
una?").

**Por qué usé cada dependencia**: Web = controladores MVC/REST; Data JPA = repositorios sin SQL manual;
Thymeleaf = motor de vistas server-side (reemplaza Twig); MySQL Driver = conector JDBC; DevTools = recarga
en caliente en desarrollo; Lombok = para no escribir getters/setters/constructores a mano (`@Data`); H2 =
base en memoria solo para pruebas, nunca en producción.

## 1. Base de datos primero

```sql
CREATE DATABASE IF NOT EXISTS cc CHARACTER SET utf8mb4;
```

Crear `src/main/resources/schema.sql` (las 2 tablas: `producto`, `shopping_cart`) y `data.sql` (4-5
productos). Los dos tienen que ser **idempotentes** (`CREATE TABLE IF NOT EXISTS`,
`INSERT ... WHERE NOT EXISTS`), así se pueden correr en cada arranque sin duplicar datos ni fallar.

## 2. `application.properties`

Configurar la conexión. Esto hay que saber explicarlo: Spring Boot arma solo un `DataSource` con HikariCP
con estas propiedades, o sea que no se escribe código de conexión:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cc?useUnicode=true&characterEncoding=utf8
spring.datasource.username=root
spring.datasource.password=tu-password
spring.jpa.hibernate.ddl-auto=none
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.sql.init.mode=always
```

Sobre el `naming.physical-strategy`: sin esto, Hibernate convierte `idProducto` a `id_producto` y falla,
porque esa columna no existe.

> En este proyecto la contraseña real ya no está en este archivo (ver `README.md`, sección de
> configuración). La pasé a `config/application.properties` (que Git ignora) para no dejarla expuesta en el
> repositorio.

## 3. Capa Entity (`entity/`)

Crear `Producto.java` e `ItemCarrito.java` con las anotaciones `@Entity @Table(name=...)`, con
`@Id @GeneratedValue`, `@Column(name=...)` mapeando 1:1 las columnas reales, y la relación `@ManyToOne` de
`ItemCarrito` hacia `Producto`. Para lo demás usé `@Data @NoArgsConstructor @AllArgsConstructor` de Lombok.

## 4. Capa Repository (`repository/`)

Son interfaces que extienden `JpaRepository<Entidad, Long>`. Solo agregué métodos propios donde hacían
falta: `findByCodUsuario` con `@Query ... JOIN FETCH` para evitar el problema N+1, y las *derived queries*
`findByCodUsuarioAndProducto_Id` y `deleteByCodUsuario`, que Spring genera solo a partir del nombre del
método.

## 5. Capa DTO (`dto/`)

Crear `ProductoDTO`, `ItemCarritoDTO`, `AgregarItemRequest`, `AgregarItemApiRequest`, `CarritoResumenDTO`,
`FlashMensaje`. Hay que tener claro **por qué existen**: nunca expongo la entidad JPA directamente a la
vista o a la API, así no se acopla la persistencia con la presentación y se evitan problemas de
serialización lazy.

## 6. Capa Service (`service/`)

`ProductoService` (listar), `CarritoService` (agregar/listar/eliminar/vaciar/totalUnidades) y
`UsuarioSesionService` (genera un UUID en la sesión, sin login real). Aquí va **toda** la lógica de negocio.
El "upsert" (buscar el item; si existe, sumar la cantidad confiando en el *dirty checking*; si no, crearlo)
es lo que más me van a preguntar en la defensa, así que tengo que tenerlo bien explicado.

También va `exception/RecursoNoEncontradoException` con `@ResponseStatus(HttpStatus.NOT_FOUND)`.

## 7. Capa Controller (`controller/`)

- `HomeController` → `GET /` (catálogo).
- `CarritoController` → `GET /carrito`, `POST /carrito/agregar`, `GET /carrito/{id}/eliminar`,
  `GET /carrito/vaciar` (todo HTML/Thymeleaf).
- `CarritoApiController` (`@RestController`) → `POST /api/carrito/items` (JSON puro, lo usa `fetch()`).
- `GlobalModelAttributes` (`@ControllerAdvice`) → pone el contador del carrito en el navbar de todas las
  páginas.

Separé `CarritoController` de `CarritoApiController` porque uno devuelve vistas HTML (sirve como respaldo
cuando no hay JS) y el otro devuelve JSON. Los dos usan el mismo `CarritoService`, entonces no se duplica
la lógica.

## 8. Vistas (`templates/`) y estáticos (`static/`)

Plantillas Thymeleaf con fragmentos reutilizables (`fragments/layout.html` + `th:replace`), CSS/JS y las 4
imágenes de producto. El `carrito-app.js` intercepta el `submit` del formulario y hace `fetch()` a
`/api/carrito/items` en vez de recargar la página.

## 9. Pruebas (`src/test/java/...`)

- Unitarias con **Mockito** (`@ExtendWith(MockitoExtension.class)`, con mocks de los repositorios) para
  `CarritoService` y `ProductoService`.
- `@DataJpaTest` + H2 para probar la query `JOIN FETCH` real sin necesitar MySQL.
- `@SpringBootTest` para comprobar que el contexto de Spring carga sin errores.

## 10. Verificar que corre

```powershell
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

Abrir `http://localhost:8080`.

## 11. Git desde el minuto uno

```powershell
git init
git add pom.xml mvnw mvnw.cmd .mvn .gitignore
git commit -m "chore: estructura inicial del proyecto"
# ... y un commit por cada capa que agregues, según avances de verdad
```

---

