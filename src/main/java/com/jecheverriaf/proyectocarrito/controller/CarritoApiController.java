package com.jecheverriaf.proyectocarrito.controller;


import com.jecheverriaf.proyectocarrito.dto.AgregarItemApiRequest;
import com.jecheverriaf.proyectocarrito.dto.CarritoRestumenDTO;
import com.jecheverriaf.proyectocarrito.service.CarritoService;
import com.jecheverriaf.proyectocarrito.service.UsuarioSesionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CarritoApiController {
    private final CarritoService carritoService;
    private final UsuarioSesionService usuarioSesionService;

    @PostMapping("/api/carrito/items")
    public CarritoRestumenDTO agregarItem(@RequestBody AgregarItemApiRequest request, HttpSession session) {
        String codUsuario = usuarioSesionService.getCodUsuario(session);
        carritoService.agregar(codUsuario, request.getIdProducto(), request.getCantidad());

        return new CarritoRestumenDTO(carritoService.totalUnidades(codUsuario));
    }
}
