package com.jecheverriaf.proyectocarrito.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    private Long idProducto;
    private String nombre;
    private String codigo;
    private String imagen;
    private Double precio;

}
