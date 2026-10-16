# Diccionario de datos — CarritoGT

Usé el esquema que dio el catedrático, sin agregarle nada (2 tablas, como pide el enunciado). Está definido
en [`src/main/resources/schema.sql`](../src/main/resources/schema.sql).

## Tabla `producto`

| Campo | Tipo | Nulo | Clave | Descripción |
|---|---|---|---|---|
| `idProducto` | INT | No | PK, auto-incremental | Identificador único del producto |
| `nombre` | VARCHAR(255) | Sí | — | Nombre comercial del producto |
| `codigo` | VARCHAR(255) | No | — | Código/SKU interno del producto |
| `imagen` | TEXT | Sí | — | URL o ruta relativa de la imagen del producto |
| `precio` | DOUBLE(10,2) | Sí | — | Precio unitario |

## Tabla `shopping_cart`

| Campo | Tipo | Nulo | Clave | Descripción |
|---|---|---|---|---|
| `idShoppingCart` | INT | No | PK, auto-incremental | Identificador único de la línea del carrito |
| `idProducto` | INT | Sí | FK → `producto.idProducto` | Producto que se agregó al carrito |
| `codUsuario` | VARCHAR(128) | Sí | — | Identificador anónimo de sesión (UUID), no hace falta login |
| `cantidad` | INT | Sí | — | Cuántas unidades de ese producto hay en el carrito |

## Relación

- `shopping_cart.idProducto` → `producto.idProducto` (uno a muchos: un mismo producto puede estar en muchas
  líneas de carrito de distintos usuarios; `cc_profk_1` es el nombre de la FK en el script SQL).
- No hay tabla de usuarios: `codUsuario` es un UUID que `UsuarioSesionService` genera en el momento y
  guarda en la sesión HTTP (`HttpSession`), igual que lo hacía la versión PHP original (`App\Auth`).

## Mapeo a entidades JPA

| Tabla | Entidad Java |
|---|---|
| `producto` | `localhost.jecheverria.carritocompras.entity.Producto` |
| `shopping_cart` | `localhost.jecheverria.carritocompras.entity.ItemCarrito` |

En `ItemCarrito` mapeé la FK como una relación `@ManyToOne` hacia `Producto` (ver
[`DOCUMENTACION.md`](../DOCUMENTACION.md), sección 3.2), en lugar de dejar `idProducto` como un entero
suelto.
