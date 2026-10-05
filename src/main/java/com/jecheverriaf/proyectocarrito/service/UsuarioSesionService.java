package com.jecheverriaf.proyectocarrito.service;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

// como no hay login con esto identifico a cada usuario
// le genero un codigo y lo guardo en su sesion asi cada quien tiene su propio carrito
@Service
@RequiredArgsConstructor
public class UsuarioSesionService {
    // nombre con el que aguardo el codigo en la sesion lo dejo en constante para no escribirlo a mano varias veces
    private static final String ATRIBUTO_COD_USUARIO= "codUsr";

    // si el usuario ya tiene codigo en la sesion lo devuelvo
    // si no tiene (primera vez que entra) se creo un id random y lo aguardo

    public String getCodUsuario(HttpSession session) {
        Object codUsuario = session.getAttribute(ATRIBUTO_COD_USUARIO);
        if (codUsuario == null) {
            codUsuario = UUID.randomUUID().toString();
            session.setAttribute(ATRIBUTO_COD_USUARIO, codUsuario);
        }
        return (String) codUsuario;
    }
}
