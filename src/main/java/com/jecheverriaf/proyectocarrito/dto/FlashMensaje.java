package com.jecheverriaf.proyectocarrito.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashMensaje {

    // los tipos de mensaje en constantes para no escribir el texto a mano y equivocarme
    // static porque son de la clase, no de cada objeto
    public static final String EXITO = "exito";
    public static final String INFO = "info";
    public static final String AVISO = "aviso";
    public static final String ERROR = "templates/error";

    private String cuerpo; // el texto que se muestra
    private String tipo; //con eso el layout escoge el color
    private String alt;// no se utilizara en el layout
}
