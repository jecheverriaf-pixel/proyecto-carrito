package com.jecheverriaf.proyectocarrito.dto;

import lombok.Data;

@Data
// lo que llega del formulario normal del catalogo
// se llaman "id" y "cantidad" porque asi son los name de los input del form
public class AgregarItemRequest {
    private Long id;
    private Integer cantidad;
}
