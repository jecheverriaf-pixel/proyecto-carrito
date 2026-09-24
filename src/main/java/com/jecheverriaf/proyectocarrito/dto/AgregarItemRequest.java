package com.jecheverriaf.proyectocarrito.dto;

import lombok.Data;

@Data
public class AgregarItemRequest {
    private Long id;
    private Integer cantidad;
}
