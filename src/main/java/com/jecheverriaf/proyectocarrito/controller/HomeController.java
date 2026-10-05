package com.jecheverriaf.proyectocarrito.controller;


import com.jecheverriaf.proyectocarrito.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ProductoService productoService;
    @GetMapping("/")
    // cuando entran a "/" mando todos los productos a la vista con el nombre "productos"
    // en el html los recorro con th:each
    public String index(Model  model) {
        model.addAttribute("productos", productoService.listarTodos());
        return "home/index";// esto se conecta con el templates/home/index.html
    }
}
