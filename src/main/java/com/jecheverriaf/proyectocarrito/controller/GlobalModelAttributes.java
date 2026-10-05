package com.jecheverriaf.proyectocarrito.controller;


import com.jecheverriaf.proyectocarrito.dto.FlashMensaje;
import com.jecheverriaf.proyectocarrito.service.CarritoService;
import com.jecheverriaf.proyectocarrito.service.UsuarioSesionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

// @ControllerAdvice hace que esto aplique a todos los controllers
// los metodos con @ModelAttribute corren antes de cada peticion, asi no repito lo mismo en cada controller
@Controller
@RequiredArgsConstructor
public class GlobalModelAttributes {
    private final CarritoService carritoService;
    private final UsuarioSesionService usuarioSesionService;

    // si no vino ningun mensaje de un redirect pongo una lista vacia
    // asi el layout no truena cuando hace el th:each de los mensajes
    @ModelAttribute
    public void agregarFlashMensajesPorDefecto(Model model) {
        if(!model.containsAttribute("flashMensajes")) {
            model.addAttribute("flashMensajes", List.<FlashMensaje>of());
        }
    }
    // esto es el numerito rojo del boton del carrito que sale en el menu de todas las paginas
    @ModelAttribute("totalCarrito")
    public int agregarTotalCarrito(HttpSession session) {
        String codUsuario = usuarioSesionService.getCodUsuario(session);
        return carritoService.totalUnidades(codUsuario);
    }
}
