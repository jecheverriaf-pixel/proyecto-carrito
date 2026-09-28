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

@Controller
@RequiredArgsConstructor
public class GlobalModelAttributes {
    private final CarritoService carritoService;
    private final UsuarioSesionService usuarioSesionService;

    @ModelAttribute
    public void agregarFlashMensajesPorDefecto(Model model) {
        if(!model.containsAttribute("flashMensajes")) {
            model.addAttribute("flashMensajes", List.<FlashMensaje>of());
        }
    }
    @ModelAttribute("totalCarrito")
    public int agregarTotalCarrito(HttpSession session) {
        String codUsuario = usuarioSesionService.getCodUsuario(session);
        return carritoService.totalUnidades(codUsuario);
    }
}
