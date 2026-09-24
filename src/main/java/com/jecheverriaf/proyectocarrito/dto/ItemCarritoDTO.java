package com.jecheverriaf.proyectocarrito.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCarritoDTO {
    private long idShoppingCart;
    private Long idProducto;
    private String nombre;
    private String codigo;
    private String imagen;
    private Double precio;
    private Integer cantidad;
    private Double subTotal;
}
