package com.jecheverriaf.proyectocarrito.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// lo que devuelve la api despues de agregar, se va como json {"totalItems": 5}
// con eso el javascript actualiza el numerito del carrito
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoRestumenDTO {
    private int totalItems;

}
