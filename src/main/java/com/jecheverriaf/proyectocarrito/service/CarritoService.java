package com.jecheverriaf.proyectocarrito.service;


import com.jecheverriaf.proyectocarrito.dto.ItemCarritoDTO;
import com.jecheverriaf.proyectocarrito.entity.ItemCarrito;
import com.jecheverriaf.proyectocarrito.entity.Producto;
import com.jecheverriaf.proyectocarrito.exception.RecursoNoEncontradoException;
import com.jecheverriaf.proyectocarrito.repository.ItemCarritoRepository;
import com.jecheverriaf.proyectocarrito.repository.ProductoRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarritoService {
    private final ItemCarritoRepository itemCarritoRepository;
    private final ProductoRepository productoRepository;

    public List<ItemCarritoDTO> listar(String codUsuario) {
        return itemCarritoRepository.findByCodUsuario(codUsuario).stream()
                .map(this::toDTO)
                .toList();
    }

    public int totalUnidades(String codUsuario) {
        return itemCarritoRepository.findByCodUsuario(codUsuario).stream()
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
    }


    @Transactional
    public void agregar(String codUsuario, Long idProducto, Integer cantidad) {
        itemCarritoRepository.findByCodUsuarioAndProducto_Id(codUsuario, idProducto)
                .ifPresentOrElse(
                        item -> item.setCantidad(item.getCantidad() + cantidad),
                        () -> {
                            Producto producto = productoRepository.findById(idProducto)
                                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: " + idProducto));

                            ItemCarrito nuevo = new ItemCarrito();
                            nuevo.setProducto(producto);
                            nuevo.setCantidad(cantidad);
                            nuevo.setCodUsuario(codUsuario);
                            itemCarritoRepository.save(nuevo);
                        });
    }
    @Transactional
    public void eliminarItem(Long idShoppingCart) {
        if (!itemCarritoRepository.existsById(idShoppingCart)) {
            throw new RecursoNoEncontradoException("Item de carrito no encontrado: " + idShoppingCart);
        }
        itemCarritoRepository.deleteById(idShoppingCart);
    }
    @Transactional
    public void vacia(String codUsuario){ itemCarritoRepository.deleteByCodUsuario(codUsuario);}

    private ItemCarritoDTO toDTO(ItemCarrito item){
        Producto producto = item.getProducto();
        double subTotal = producto.getPrecio() * item.getCantidad();

        return new ItemCarritoDTO(
                item.getId(),
                producto.getId(),
                producto.getNombre(),
                producto.getCodigo(),
                producto.getImagen(),
                producto.getPrecio(),
                item.getCantidad(),
                subTotal);
    }
}