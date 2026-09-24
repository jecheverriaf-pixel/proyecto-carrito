package com.jecheverriaf.proyectocarrito.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashMensaje {

    public static final String EXITO = "exito";
    public static final String INFO = "info";
    public static final String AVISO = "aviso";
    public static final String ERROR = "error";

    private String cuerpo;
    private String tipo;
    private String alt;
}
