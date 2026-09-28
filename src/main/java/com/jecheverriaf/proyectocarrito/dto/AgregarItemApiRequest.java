package com.jecheverriaf.proyectocarrito.dto;


import lombok.Data;

@Data
public class AgregarItemApiRequest {
    private Long idProducto;
    private Integer cantidad;
}
