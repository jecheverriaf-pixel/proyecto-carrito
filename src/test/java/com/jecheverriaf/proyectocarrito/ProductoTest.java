package com.jecheverriaf.proyectocarrito;

import com.jecheverriaf.proyectocarrito.entity.Producto;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductoTest {

    @Test
    public void deberiaGenerarElToStringCorrecto() {
        // 1. Crear el objeto directamente en Java (sin BD, sin Spring)
        Producto producto = new Producto(1L, "Teclado", "TEC01", "img.jpg", 25.0);

        // 2. Llamar al toString()
        String resultado = producto.toString();

        // 3. Verificar que el texto devuelto contenga los datos del producto
        assertThat(resultado).contains("Teclado", "TEC01", "25.0");
    }
}