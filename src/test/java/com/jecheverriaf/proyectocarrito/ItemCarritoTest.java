package com.jecheverriaf.proyectocarrito;

import com.jecheverriaf.proyectocarrito.entity.ItemCarrito;
import com.jecheverriaf.proyectocarrito.entity.Producto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ItemCarritoTest {
    @Test
    public void calcularSubtotal(){
        Producto producto = new Producto(1L, "Camara", "CAM01", "img.jpg", 671.00);
        ItemCarrito itemCarrito = new ItemCarrito(1L, producto, "usuario", 2);

        Double subtotal = itemCarrito.getProducto().getPrecio() * itemCarrito.getCantidad();

        assertThat(subtotal).isEqualTo(1342.00);
        assertThat(itemCarrito.getProducto().getNombre()).isEqualTo("Camara");
        assertThat(itemCarrito.getCodUsuario()).isEqualTo("usuario");

    }
}
