package com.jecheverriaf.proyectocarrito;

import com.jecheverriaf.proyectocarrito.exception.RecursoNoEncontradoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class RecursoNoEncontradoExceptionTest {

    @Test
    public void lanzarExcepcionNoEncontrado() {
        assertThatThrownBy(() -> {
            throw new RecursoNoEncontradoException("Producto no encontrado");
        }).isInstanceOf(RecursoNoEncontradoException.class).hasMessage("Producto no encontrado");
    }
    @Test
    public void guardarMensaje(){
        String mensaje = "Producto con ID 98 no encontrado";
        RecursoNoEncontradoException e = new RecursoNoEncontradoException(mensaje);
        assertThat(e.getMessage()).isEqualTo(mensaje);
        assertThat(e).isInstanceOf(RuntimeException.class);
    }
}
