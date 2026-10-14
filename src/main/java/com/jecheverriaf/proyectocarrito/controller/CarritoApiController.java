package com.jecheverriaf.proyectocarrito.controller;


import com.jecheverriaf.proyectocarrito.dto.AgregarItemApiRequest;
import com.jecheverriaf.proyectocarrito.dto.CarritoRestumenDTO;
import com.jecheverriaf.proyectocarrito.dto.ItemCarritoDTO;
import com.jecheverriaf.proyectocarrito.dto.ProductoDTO;
import com.jecheverriaf.proyectocarrito.entity.ItemCarrito;
import com.jecheverriaf.proyectocarrito.service.CarritoService;
import com.jecheverriaf.proyectocarrito.service.ProductoService;
import com.jecheverriaf.proyectocarrito.service.UsuarioSesionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// este es el controller de la api, por ser @RestController todo lo que retorna se va como json
// lo usa el javascript (fetch) para agregar al carrito sin recargar la pagina
@RestController
@RequiredArgsConstructor
public class CarritoApiController {
    private final CarritoService carritoService;
    private final UsuarioSesionService usuarioSesionService;
    private final ProductoService  productoService;


    //ista todos los productos del catalogo como json
    @GetMapping("/api/productos")
    public List<ProductoDTO> productos(){return productoService.listarTodos();}
    //lista los items del carrito del usuario de esta sesion
    @GetMapping("/api/carrito")
    public List<ItemCarritoDTO> carrito(HttpSession session){
        String codUsuario = usuarioSesionService.getCodUsuario(session);
        return carritoService.listar(codUsuario);
    }

    // recibe el json {idProducto, cantidad}, @RequestBody lo convierte al objeto
    // agrego el producto y devuelvo cuantas unidades lleva el carrito para actualizar el numerito del boton
    @PostMapping("/api/carrito/items")
    public CarritoRestumenDTO agregarItem(@RequestBody AgregarItemApiRequest request, HttpSession session) {
        String codUsuario = usuarioSesionService.getCodUsuario(session);
        carritoService.agregar(codUsuario, request.getIdProducto(), request.getCantidad());

        return new CarritoRestumenDTO(carritoService.totalUnidades(codUsuario));
    }
    //eliiminaa un  item del carrito
    @DeleteMapping("/api/carrito/items/{id}")
    public CarritoRestumenDTO eliminar(@PathVariable Long id, HttpSession session){
        String codUsuario = usuarioSesionService.getCodUsuario(session);
        carritoService.eliminarItem(codUsuario, id);

        return new CarritoRestumenDTO(carritoService.totalUnidades(codUsuario));
    }

    // vacia todo el carrito del usuario
    @DeleteMapping("/api/carrito")
    public CarritoRestumenDTO vaciar(HttpSession session) {
        String codUsuario = usuarioSesionService.getCodUsuario(session);
        carritoService.vaciar(codUsuario);

        return new CarritoRestumenDTO(carritoService.totalUnidades(codUsuario));
    }

}
