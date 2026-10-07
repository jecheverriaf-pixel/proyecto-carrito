// todo va aqui
// asi las variables y funciones no quedan desordenadas
(function () {
    //las clases de tailwind para cada tipo de mensaje, son los mismos  colores que usa el layout
    var ESTILOS_TOAST={
        exito: 'bg-green-50 text-green-800 border border-green-200',
        error: 'bg-red-50 text-red-800 border border-red-200',
        info: 'bg-blue-50 text-blue-800 border border-blue-200',
        aviso: 'bg-yellow-50 text-yellow-800 border border-yellow-200'
    };

    //crear el mensaje y meterlo en el contenedor
    //uso textcontet
    function mostrarToast(mensaje, tipo) {
        var contenedor = document.getElementById('toast-container');
        if (!contenedor) return;

        var toast = document.createElement('div')
        toast.className = 'flex items-center rounded-lg p-4 text-sm shadow transition-opacity duration-300 ' + (ESTILOS_TOAST[tipo] || ESTILOS_TOAST.exito);
        toast.setAttribute('role', 'alert');
        toast.textContent = mensaje;
        contenedor.appendChild(toast);
        programarCierre(toast);
    }

        //tiewmpo para que se desaparezca
        function programarCierre(toast) {
            setTimeout(function () {
                toast.classList.add('opacity-0');
                setTimeout(function () { toast.remove();
                }, 300);

            }, 2500);

        }
        //cambia el numerito tojo del boton del carrito
        //si llega a 0 le pongo la clase hidden
        function actualizarBadgeCarrito(total){
            var badge = document.getElementById('cart-badge');
            if(!badge) return;

            badge.textContent = total;
            badge.classList.toggle('hidden', total === 0)
        }

        //manda el producto  sin recargar la pagina
        function agregarAlCarrito(idProducto, cantidad, boton) {
            //desactiva el boton mientras hay una espera para evitar clickeos varias vecess

            var textoOriginal = boton.textContent;
            boton.disabled = true;
            boton.textContent = 'Agregando......';


            //Post a carritoApicontroller mando json
            fetch('/api/carrito/items', {
                method: "POST",
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({idProducto: idProducto, cantidad: cantidad})
            })
                .then(function (respuesta) {
                    //si hay error que lo capture el catch
                    if (!respuesta.ok) {
                        throw new Error('No se pudo agregar el producto');
                    }
                    return respuesta.json();
                })
                .then(function (datos) {
                    //resumen de carrito
                    actualizarBadgeCarrito(datos.totalItems);
                    mostrarToast('Producto agregando al carrito', 'exito');
                })
                .catch(function () {
                    mostrarToast('No se  pudo agregar el producto', 'error');
                })
                .finally(function () {
                    //se regresa al boton
                    boton.disabled = false;
                    boton.textContent = textoOriginal;
                });
        }

            //hay que esperar que cargar el html antes de buscar elementos
            document.addEventListener('DOMContentLoaded', function () {
                //a los form le cambio el ubmit
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
