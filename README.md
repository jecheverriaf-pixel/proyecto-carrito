# CarritoGT — API REST con Spring Boot (Fase 2)

Este proyecto es la migración de una tienda en línea con carrito de compras. La versión original estaba
hecha en PHP 7 orientado a objetos sin frameworks (`carrito-compra-basico`) y yo la pasé a una API REST
en capas con **Java 21 + Spring Boot**. Los datos se guardan en **MySQL** y el frontend lo hice con
**Thymeleaf**, que además consume la API con `fetch()` para no tener que recargar la página.

Si quieren ver el detalle técnico completo (por qué separé las capas, cómo funciona el "upsert" del
carrito, cómo monté la VM en VirtualBox, etc.) está en [`DOCUMENTACION.md`](./DOCUMENTACION.md).

## Ver la aplicación en línea

La app está desplegada en una instancia de **AWS EC2** (capa gratuita) y se puede abrir desde cualquier
navegador de dos formas:

| Acceso | Dirección |
|---|---|
| Subdominio DuckDNS | http://carrito-proyecto-javier.duckdns.org:8080 |
| IP pública de la instancia AWS | http://18.221.45.3:8080 |

Algunas cosas a tomar en cuenta:

- Hay que poner el puerto **`:8080`**, porque la app no está publicada en el puerto 80. DuckDNS solo
  traduce el nombre a la IP, no cambia el puerto.
- Para no salirme de la capa gratuita apago la instancia cuando no la estoy usando, así que si no carga
  puede ser que esté detenida.
- Cada vez que la instancia se detiene y se vuelve a encender, AWS le da **otra IP pública**. Cuando eso
  pasa actualizo la IP en DuckDNS, por eso el subdominio es la forma más segura de entrar; la IP de la
  tabla puede quedar desactualizada.

Los pasos que seguí para el despliegue están en [`docs/DESPLIEGUE_AWS.md`](./docs/DESPLIEGUE_AWS.md).

## Tecnologías que usé

| Capa | Tecnología |
|---|---|
| Backend | Java 21, Spring Boot 4.1 (Web MVC, Data JPA, Thymeleaf) |
| Persistencia | MySQL / MariaDB (Hibernate/JPA con HikariCP) |
| Frontend | Thymeleaf (server-side) + JavaScript puro (`fetch`) + Tailwind/Flowbite por CDN |
| Pruebas | JUnit 5, Mockito, `@DataJpaTest` con H2 en memoria |
| Build | Maven (con el wrapper `mvnw` / `mvnw.cmd`) |
| Nube | AWS EC2 (Amazon Linux 2023) + DuckDNS |

## Qué se necesita para correr cada versión

**Versión PHP (Fase 1 — `carrito-compra-basico`)**
- PHP 7, Apache y MySQL/MariaDB (LAMP o XAMPP).
- Los pasos están en el README del repositorio [`carrito-compra-basico`](https://github.com/mvelasquezo/carrito-compra-basico).

**Versión Spring Boot (Fase 2 — este repositorio)**
- JDK 21.
- MySQL o MariaDB corriendo en la máquina local (o en la VM).
- No hace falta instalar Maven, porque el proyecto ya trae el wrapper (`./mvnw`).

## Cómo instalarlo y levantarlo

1. Crear la base de datos:
   ```sql
   CREATE DATABASE IF NOT EXISTS cc CHARACTER SET utf8mb4;
   ```
2. Configurar las credenciales (lo explico en la siguiente sección; **no** se toca el archivo que está en
   el repositorio).
3. Compilar y correr:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```
   Ojo: es `spring-boot:run` con dos puntos. Con guion (`spring-boot-run`) Maven da error.
4. Abrir en el navegador: `http://localhost:8080`

No hay que crear las tablas a mano: al arrancar, la app ejecuta `schema.sql` y `data.sql`
(`spring.sql.init.mode=always`), así que se crean `producto` y `shopping_cart` y se cargan los 4
productos de prueba.

## Configuración que hay que cambiar

Saqué la contraseña real de MySQL de `src/main/resources/application.properties` por seguridad. Ahora la
app lee las credenciales de una de estas dos opciones:

- Las variables de entorno `DB_URL`, `DB_USERNAME` y `DB_PASSWORD`, **o**
- Un archivo `config/application.properties` en la raíz del proyecto. Esa carpeta no se sube a Git y como
  plantilla está [`application.properties.example`](./application.properties.example). Spring Boot lee
  ese archivo solo, además del que está en `src/main/resources`.

Pasos:
```powershell
mkdir config
copy application.properties.example config\application.properties
# luego editar config\application.properties con la contraseña de tu MySQL local
```

## Endpoints

**Vistas HTML**

| Método | Ruta | Qué hace |
|---|---|---|
| GET | `/` | Muestra el catálogo de productos |
| GET | `/carrito` | Muestra lo que hay en el carrito |
| POST | `/carrito/agregar` | Agrega un producto con el formulario y regresa a `/` (funciona sin JS) |
| GET | `/carrito/{id}/eliminar` | Quita una línea del carrito |
| GET | `/carrito/vaciar` | Vacía todo el carrito |

**API REST (JSON)**

| Método | Ruta | Qué hace |
|---|---|---|
| GET | `/api/productos` | Devuelve el catálogo |
| GET | `/api/carrito` | Devuelve los items del usuario de la sesión con su subtotal |
| POST | `/api/carrito/items` | Agrega un producto y responde `{"totalItems": N}` |
| DELETE | `/api/carrito/items/{id}` | Elimina un item (solo si es del usuario de la sesión) y responde `{"totalItems": N}` |
| DELETE | `/api/carrito` | Vacía el carrito y responde `{"totalItems": 0}` |

Los ejemplos de request/response y los códigos de error están en
[`docs/CONTRATO_API.md`](./docs/contrato_api.md). Todos los probé con Postman (colección `PruebasCarrito`).

## Problemas conocidos y limitaciones

- Tailwind y Flowbite se cargan por CDN, entonces si no hay internet la app sí abre pero se ve sin estilos.
- No hay login: a cada visitante se le asigna un identificador anónimo (UUID) que se guarda en la sesión,
  igual que en la versión PHP. Agregar usuarios o roles queda fuera del alcance del proyecto.
- El repositorio de esta Fase 2 lo inicialicé cuando ya llevaba bastante avance, por eso los primeros
  commits agrupan el código por capa y no reflejan el trabajo día a día. De ahí en adelante los commits
  ya van siendo incrementales, conforme a la bitácora.

#
