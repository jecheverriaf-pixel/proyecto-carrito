// todo va dentro de esta funcion que se ejecuta sola (IIFE)
// asi mis variables y funciones no quedan sueltas en el scope global de la pagina
(function () {
    // las clases de tailwind para cada tipo de mensaje, son los mismos colores que usa el layout
    var ESTILOS_TOAST = {
        exito: 'bg-green-50 text-green-800 border border-green-200',
        error: 'bg-red-50 text-red-800 border border-red-200',
        info: 'bg-blue-50 text-blue-800 border border-blue-200',
        aviso: 'bg-yellow-50 text-yellow-800 border border-yellow-200'
    };

    // crea el mensajito (toast) y lo mete en el contenedor de arriba a la derecha
    // uso textContent y no innerHTML para que no se pueda meter html en el mensaje
    function mostrarToast(mensaje, tipo) {
        var contenedor = document.getElementById('toast-container');
        if (!contenedor) return;

        var toast = document.createElement('div');
        toast.className = 'flex items-center rounded-lg p-4 text-sm shadow transition-opacity duration-300 ' + (ESTILOS_TOAST[tipo] || ESTILOS_TOAST.exito);
        toast.setAttribute('role', 'alert');
        toast.textContent = mensaje;
        contenedor.appendChild(toast);
        programarCierre(toast);
    }

    // a los 2.5 segundos lo hago transparente y 300ms despues lo quito del todo
    function programarCierre(toast) {
        setTimeout(function () {
            toast.classList.add('opacity-0');
            setTimeout(function () { toast.remove(); }, 300);
        }, 2500);
    }

    // cambia el numerito rojo del boton del carrito
    // si llega a 0 le pongo la clase hidden para que no se vea
    function actualizarBadgeCarrito(total) {
        var badge = document.getElementById('cart-badge');
        if (!badge) return;

        badge.textContent = total;
        badge.classList.toggle('hidden', total === 0);
    }

    // manda el producto a la api con fetch, sin recargar la pagina
    function agregarAlCarrito(idProducto, cantidad, boton) {
        // desactivo el boton mientras espera para que no le den click varias veces
        var textoOriginal = boton.textContent;
        boton.disabled = true;
        boton.textContent = 'Agregando...';

        // POST a CarritoApiController, mando los datos como json
        fetch('/api/carrito/items', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ idProducto: idProducto, cantidad: cantidad })
        })
            .then(function (respuesta) {
                // si el servidor respondio error (ej. 404) me voy directo al catch
                if (!respuesta.ok) {
                    throw new Error('No se pudo agregar el producto');
                }
                return respuesta.json();
            })
            .then(function (datos) {
                // datos es el CarritoResumenDTO: {totalItems: X}
                actualizarBadgeCarrito(datos.totalItems);
                mostrarToast('Producto agregado al carrito', 'exito');
            })
            .catch(function () {
                mostrarToast('No se pudo agregar el producto', 'error');
            })
            .finally(function () {
                // pase lo que pase regreso el boton como estaba
                boton.disabled = false;
                boton.textContent = textoOriginal;
            });
    }

    // espero a que cargue todo el html antes de buscar los elementos
    document.addEventListener('DOMContentLoaded', function () {
        // a cada form del catalogo le cambio el submit normal por el fetch
        document.querySelectorAll('[data-carrito-form]').forEach(function (formulario) {
            formulario.addEventListener('submit', function (evento) {
                // preventDefault para que el form no se mande normal y no recargue
                evento.preventDefault();

                // saco los valores del form por el name de cada input
                var datosFormulario = new FormData(formulario);
                var idProducto = datosFormulario.get('id');
                var cantidad = parseInt(datosFormulario.get('cantidad'), 10) || 1;
                var boton = formulario.querySelector('button[type="submit"]');

                agregarAlCarrito(idProducto, cantidad, boton);
            });
        });

        // los links de eliminar y vaciar tienen data-confirm, antes de seguir pregunto si esta seguro
        // si le da cancelar hago preventDefault y no pasa nada
        document.querySelectorAll('[data-confirm]').forEach(function (enlace) {
            enlace.addEventListener('click', function (evento) {
                if (!confirm(enlace.getAttribute('data-confirm'))) {
                    evento.preventDefault();
                }
            });
        });

        // los mensajes que vienen del servidor (flash) tambien se quitan solos
        document.querySelectorAll('[data-flash="true"]').forEach(programarCierre);
    });
})();
