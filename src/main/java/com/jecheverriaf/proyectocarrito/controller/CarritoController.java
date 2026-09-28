package com.jecheverriaf.proyectocarrito.controller;


import com.jecheverriaf.proyectocarrito.dto.AgregarItemRequest;
import com.jecheverriaf.proyectocarrito.dto.FlashMensaje;
import com.jecheverriaf.proyectocarrito.service.CarritoService;
import com.jecheverriaf.proyectocarrito.service.UsuarioSesionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CarritoController {
    private final CarritoService carritoService;
    private final UsuarioSesionService usuarioSesionService;

    @GetMapping("/carrito")
    public String index(Model model, HttpSession session) {
        String codUsuario = usuarioSesionService.getCodUsuario(session);
        model.addAttribute("productos", carritoService.listar(codUsuario));
        return "carrito/index";
    }

    @PostMapping("/carrito/agregar")
    public String agregar(@ModelAttribute AgregarItemRequest request, HttpSession session, RedirectAttributes redirectAttributes) {

        String codUsuario = usuarioSesionService.getCodUsuario(session);
        carritoService.agregar(codUsuario, request.getId(),  request.getCantidad());

        redirectAttributes.addFlashAttribute("flashMensajes", java.util.List.of(new FlashMensaje("Producto agregado", FlashMensaje.EXITO, "2")));
        return "redirect:/";
    }
    @GetMapping("/carrito/{id}/eliminar")
    public String eliminar(@PathVariable Long id) {
        carritoService.eliminarItem(id);
        return "redirect:/carrito";
    }
    @GetMapping("/carrito/vaciar")
    public String vaciar(HttpSession session) {
        String codUsuario = usuarioSesionService.getCodUsuario(session);
        carritoService.vaciar(codUsuario);
        return "redirect:/carrito";
    }

}
