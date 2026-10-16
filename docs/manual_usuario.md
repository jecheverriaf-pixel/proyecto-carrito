# Manual de usuario — CarritoGT

## Requisitos antes de iniciar

1. Tener MySQL/MariaDB corriendo, con la base `cc` ya creada.
2. Tener las credenciales configuradas en `config/application.properties` (ver `README.md`, en la parte de configuración).
3. Tener instalado el JDK 21.

## Iniciar el entorno

```powershell
.\mvnw.cmd spring-boot:run
```

Espera a que en la consola salga `Started CarritoComprasApplication` y `Tomcat started on port 8080`.
Después abre esto en el navegador:

```
http://localhost:8080
```

## Usar el carrito de compras

1. **Ver el catálogo**: cuando entras a `http://localhost:8080` aparecen las tarjetas de los productos
   disponibles (nombre, imagen y precio).
2. **Agregar un producto al carrito**: en la tarjeta del producto, cambia la cantidad si quieres (por
   defecto es 1) y presiona el botón de agregar. Sale un mensaje de confirmación ("Producto agregado al
   carrito") y el número del ícono del carrito (arriba a la derecha) cambia sin que se recargue la página.
3. **Ver el carrito**: haz clic en el ícono del carrito para ir a `/carrito`. Ahí ves los productos que
   agregaste, con la cantidad y el subtotal de cada línea.
4. **Eliminar un producto del carrito**: en la vista del carrito, presiona "Eliminar" en la línea del
   producto. La página se actualiza y el producto ya no aparece en la lista.
5. **Vaciar el carrito**: en la vista `/carrito` está el botón "Vaciar carrito", que borra todas las líneas de una vez.

No hace falta iniciar sesión. La primera vez que entras al sitio se te asigna solo un identificador anónimo
(que se guarda en la cookie de sesión del navegador), y tu carrito queda ligado a ese identificador mientras
la sesión siga activa.

## Problemas comunes

| Síntoma | Causa probable | Solución |
|---|---|---|
| La app no arranca / sale error de conexión a MySQL | Las credenciales están mal o MySQL está apagado | Revisar `config/application.properties` y que el servicio de MySQL esté activo |
| La página carga sin estilos | No hay internet (Tailwind/Flowbite se cargan por CDN) | Revisar la conexión a internet |
| Al agregar un producto no pasa nada | JavaScript está deshabilitado | El formulario igual funciona como un POST normal (recarga la página) |
| "Unknown database 'cc'" | No se creó la base de datos | Ejecutar `CREATE DATABASE cc CHARACTER SET utf8mb4;` |
