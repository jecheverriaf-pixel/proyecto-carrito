package com.jecheverriaf.proyectocarrito.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// lo que mando al catalogo por cada producto
// uso DTO para mandar solo lo que la vista necesita y no la entidad de la bd directo
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
