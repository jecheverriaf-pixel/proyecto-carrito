# Contrato de API — CarritoGT

Todas las rutas son relativas a `http://localhost:8080`. Para identificar al usuario no hace falta login:
uso una cookie de sesión (`JSESSIONID`) que el navegador manda solo en cada petición.

## POST `/api/carrito/items`

Agrega un producto al carrito (o le suma a la cantidad si ya estaba) sin recargar la página. Es el único
endpoint que pensé para usarse como API JSON pura; las demás rutas devuelven HTML generado en el servidor.

**Request**

```http
POST /api/carrito/items HTTP/1.1
Content-Type: application/json
Cookie: JSESSIONID=...

{
  "idProducto": 1,
  "cantidad": 2
}
```

**Response — 200 OK**

```json
{
  "totalItems": 2
}
```

`totalItems` es la suma de las unidades de todos los productos que hay en el carrito del usuario. La uso
para actualizar el badge del navbar.

**Response — 404 Not Found** (si `idProducto` no existe)

```json
{
  "timestamp": "2026-08-27T10:00:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "path": "/api/carrito/items"
}
```

---

## GET `/`

Catálogo de productos. Devuelve HTML (Thymeleaf), no JSON. Por dentro usa `ProductoService.listarTodos()`,
que devuelve una lista de `ProductoDTO { id, nombre, codigo, imagen, precio }`.

## GET `/carrito`

Muestra lo que hay en el carrito del usuario actual. Devuelve HTML. Cada línea es un `ItemCarritoDTO`:

```json
{
  "idShoppingCart": 5,
  "idProducto": 1,
  "nombre": "FinePix Pro2 3D Camera",
  "codigo": "3DcAM01",
  "imagen": "images/product/camera.jpg",
  "precio": 671.21,
  "cantidad": 2,
  "subTotal": 1342.42
}
```

## POST `/carrito/agregar`

Es el respaldo sin JavaScript del mismo flujo de `/api/carrito/items` (progressive enhancement). Recibe un
formulario (`application/x-www-form-urlencoded`) con `id` y `cantidad`, y redirige (`302`) a `/` con un
mensaje flash.

## GET `/carrito/{id}/eliminar`

Elimina la línea del carrito que tiene `idShoppingCart = {id}`. Redirige (`302`) a `/carrito`.

- **404** si el `id` no existe (`RecursoNoEncontradoException`).

## GET `/carrito/vaciar`

Elimina todas las líneas del carrito del usuario actual. Redirige (`302`) a `/carrito`.

---

## Manejo de errores

Todas las excepciones que no se capturan se mandan a `/error` (`BasicErrorController` de Spring Boot):

- Si el cliente acepta HTML (un navegador normal) → se muestra `templates/error/404.html` o `500.html`.
- Si el cliente pide JSON (ej. `Accept: application/json`, como cuando se llama a `/api/carrito/items`) →
  responde con el cuerpo JSON de error estándar (`status`, `error`, `path`, `timestamp`).

A `RecursoNoEncontradoException` le puse la anotación `@ResponseStatus(HttpStatus.NOT_FOUND)`, así que
cualquier servicio que la lance produce solo una respuesta `404`, sin tener que escribir un
`@ExceptionHandler` aparte.
