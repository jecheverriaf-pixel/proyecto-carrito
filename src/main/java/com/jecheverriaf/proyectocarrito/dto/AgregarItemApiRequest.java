package com.jecheverriaf.proyectocarrito.dto;


import lombok.Data;

@Data
public class AgregarItemApiRequest {
    private Long idPòducto;
    private Integer cantidad;
}
