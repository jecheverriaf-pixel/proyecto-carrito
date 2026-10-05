package com.jecheverriaf.proyectocarrito.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// lo que mando a la vista del carrito por cada producto
// junta datos del item y del producto en un solo objeto, y trae el subtotal ya calculado
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
