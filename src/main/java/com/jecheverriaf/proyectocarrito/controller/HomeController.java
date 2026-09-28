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
    public String index(Model  model) {
        model.addAttribute("productos", productoService.listarTodos());
        return "home/index";
    }
}
