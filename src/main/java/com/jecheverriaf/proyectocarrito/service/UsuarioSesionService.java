package com.jecheverriaf.proyectocarrito.service;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioSesionService {
    private static final String ATRIBUTO_COD_USUARIO= "codUsr";

    public String getCodUsuario(HttpSession session) {
        Object codUsuario = session.getAttribute(ATRIBUTO_COD_USUARIO);
        if (codUsuario == null) {
            codUsuario = UUID.randomUUID().toString();
            session.setAttribute(ATRIBUTO_COD_USUARIO, codUsuario);
        }
        return (String) codUsuario;
    }
}
