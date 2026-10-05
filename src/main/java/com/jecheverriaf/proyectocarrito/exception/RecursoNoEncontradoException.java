package com.jecheverriaf.proyectocarrito.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
// excepcion propia para cuando no encuentro un producto o un item
// con @ResponseStatus spring responde 404 y muestra la pagina templates/error/404.html
// extiende RuntimeException para no tener que poner throws en todos lados

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String message) {
        super(message);
    }
}
