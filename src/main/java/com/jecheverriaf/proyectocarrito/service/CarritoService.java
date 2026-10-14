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

// el controller solo llama a estos metodos, no habla directo con la base de datos
@Service
@RequiredArgsConstructor
public class CarritoService {
    private final ItemCarritoRepository itemCarritoRepository;
    private final ProductoRepository productoRepository;

    // traigo los items del usuario y cada uno lo convierto a DTO para mandarlo a la vista
    public List<ItemCarritoDTO> listar(String codUsuario) {
        return itemCarritoRepository.findByCodUsuario(codUsuario).stream()
                .map(this::toDTO)
                .toList();
    }
    // suma todas las cantidades del carrito, esto es lo que sale en el numerito del boton
    public int totalUnidades(String codUsuario) {
        return itemCarritoRepository.findByCodUsuario(codUsuario).stream()
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
    }

    // agrega un producto al carrito
    // si el usuario ya tenia ese producto solo le sumo la cantidad, si no creo una fila nueva
    // @Transactional: si algo falla a medias se regresa todo
    @Transactional
    public void agregar(String codUsuario, Long idProducto, Integer cantidad) {
        itemCarritoRepository.findByCodUsuarioAndProducto_Id(codUsuario, idProducto)
                .ifPresentOrElse(
                        // ya existe: solo cambio la cantidad, no hace falta save
                        // hibernate ve que el objeto cambio y hace el UPDATE solo al terminar la transaccion
                        item -> item.setCantidad(item.getCantidad() + cantidad),
                        // no existe: busco el producto, si no esta lanzo la excepcion
                        () -> {
                            Producto producto = productoRepository.findById(idProducto)
                                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: " + idProducto));

                            // aqui si aaguardo porque es un objeto nuevo y hibernate no lo conoce
                            ItemCarrito nuevo = new ItemCarrito();
                            nuevo.setProducto(producto);
                            nuevo.setCantidad(cantidad);
                            nuevo.setCodUsuario(codUsuario);
                            itemCarritoRepository.save(nuevo);
                        });
    }
    // quita un item del carrito, primero reviso que exista para mandar excepcion
    @Transactional
    public void eliminarItem(String codUsuario, Long idShoppingCart) {
        if (!itemCarritoRepository.existsByIdAndCodUsuario(
                idShoppingCart, codUsuario)) {
            throw new RecursoNoEncontradoException("Item de carrito no encontrado: " + idShoppingCart);
        }
        itemCarritoRepository.deleteById(idShoppingCart);
    }
    // borra todo lo del usuario
    // el deleteBy necesita si o si una transaccion, sin el @Transactional truena
    @Transactional
    public void vaciar(String codUsuario){ itemCarritoRepository.deleteByCodUsuario(codUsuario);}

    // convierte la entidad a DTO y aqui mismo calculo el subtotal (precio * cantidad)
    // el subtotal no se guarda en la base de  datos porque se puede calcular, asi nunca queda desactualizado
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