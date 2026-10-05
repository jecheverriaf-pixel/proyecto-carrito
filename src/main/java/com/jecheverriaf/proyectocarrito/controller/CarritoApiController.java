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
// este es el controller de la api, por ser @RestController todo lo que retorna se va como json
// lo usa el javascript (fetch) para agregar al carrito sin recargar la pagina
@RestController
@RequiredArgsConstructor
public class CarritoApiController {
    private final CarritoService carritoService;
    private final UsuarioSesionService usuarioSesionService;


    // recibe el json {idProducto, cantidad}, @RequestBody lo convierte al objeto
    // agrego el producto y devuelvo cuantas unidades lleva el carrito para actualizar el numerito del boton
    @PostMapping("/api/carrito/items")
    public CarritoRestumenDTO agregarItem(@RequestBody AgregarItemApiRequest request, HttpSession session) {
        String codUsuario = usuarioSesionService.getCodUsuario(session);
        carritoService.agregar(codUsuario, request.getIdProducto(), request.getCantidad());

        return new CarritoRestumenDTO(carritoService.totalUnidades(codUsuario));
    }
}
