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

// controller del carrito, este devuelve paginas html (no json)
// @RequiredArgsConstructor me genera el constructor con los campos final y por ahi spring me inyecta los services
@Controller
@RequiredArgsConstructor
public class CarritoController {
    private final CarritoService carritoService;
    private final UsuarioSesionService usuarioSesionService;


    // muestra la pagina del carrito
    // saco el codigo del usuario de la sesion, pido sus productos y los mando a la vista
    @GetMapping("/carrito")
    public String index(Model model, HttpSession session) {
        String codUsuario = usuarioSesionService.getCodUsuario(session);
        model.addAttribute("productos", carritoService.listar(codUsuario));
        return "carrito/index";
    }
    // este se usa cuando el form se manda normal (sin javascript)
    // @ModelAttribute llena el request con los campos del form, por eso los name del input tienen que ser "id" y "cantidad"
    @PostMapping("/carrito/agregar")
    public String agregar(@ModelAttribute AgregarItemRequest request, HttpSession session, RedirectAttributes redirectAttributes) {

        String codUsuario = usuarioSesionService.getCodUsuario(session);
        carritoService.agregar(codUsuario, request.getId(),  request.getCantidad());


        // el flash attribute aguanta el redirect y solo se muestra una vez, despues se borra so
        redirectAttributes.addFlashAttribute("flashMensajes", java.util.List.of(new FlashMensaje("Producto agregado", FlashMensaje.EXITO, "2")));
        return "redirect:/";
    }
    // elimina un solo producto del carrito, el id viene en la url
    @GetMapping("/carrito/{id}/eliminar")
    public String eliminar(@PathVariable Long id,HttpSession session) {
        String codUsuario = usuarioSesionService.getCodUsuario(session);
        carritoService.eliminarItem(codUsuario,id);
        return "redirect:/carrito";
    }
    // borra todo el carrito del usuario de esta sesion
    @GetMapping("/carrito/vaciar")
    public String vaciar(HttpSession session) {
        String codUsuario = usuarioSesionService.getCodUsuario(session);
        carritoService.vaciar(codUsuario);
        return "redirect:/carrito";
    }

}
