package com.jecheverriaf.proyectocarrito.dto;


import lombok.Data;

// lo que mandara el javascript en el fetch, viene como json: {"idProducto": 1, "cantidad": 2}
// los nombres tienen que ser iguales a las llaves del json o llegan null, revisar el como se escribe
@Data
public class AgregarItemApiRequest {
    private Long idProducto;
    private Integer cantidad;
}
