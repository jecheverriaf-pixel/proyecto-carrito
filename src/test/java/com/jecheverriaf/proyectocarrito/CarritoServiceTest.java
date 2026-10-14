package com.jecheverriaf.proyectocarrito;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import com.jecheverriaf.proyectocarrito.dto.ItemCarritoDTO;
import com.jecheverriaf.proyectocarrito.entity.ItemCarrito;
import com.jecheverriaf.proyectocarrito.entity.Producto;
import com.jecheverriaf.proyectocarrito.repository.ItemCarritoRepository;
import com.jecheverriaf.proyectocarrito.repository.ProductoRepository;
import com.jecheverriaf.proyectocarrito.service.CarritoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;



@ExtendWith(MockitoExtension.class)

public class CarritoServiceTest {
    @Mock
    private ItemCarritoRepository itemCarritoRepository;

    @Mock
    private ProductoRepository productoRepository;
    private CarritoService carritoService;

    private static final String COD_USUARIO = "usuario";

    @Test
    public void agregarSumaLaCantidadCuandoElItemYaExisteEnElCarrito(){
        carritoService = new CarritoService(itemCarritoRepository, productoRepository);

        Producto producto = new Producto(1L,"Laptop","LPN45","images/product/laptop.jpg", 10232.35);
        ItemCarrito itemExiste = new ItemCarrito(10L, producto,COD_USUARIO, 2);

        when(itemCarritoRepository.findByCodUsuarioAndProducto_Id(COD_USUARIO,1L)).thenReturn(Optional.of(itemExiste));

        carritoService.agregar(COD_USUARIO, 1L,3);

        assertThat(itemExiste.getCantidad()).isEqualTo(5);
        verify(itemCarritoRepository, never()).save(any());
        verify(productoRepository, never()).save(any());

    }
    @Test
    public void agregarUnNUevoItemCuandoNoExisteEnElCarrito(){
        carritoService = new CarritoService(itemCarritoRepository, productoRepository);
        Producto producto = new Producto(2L, "EXP Portable Hard Drive", "USB02",
                "images/product/external-hard-drive.jpg", 215.10);

        when(itemCarritoRepository.findByCodUsuarioAndProducto_Id(COD_USUARIO,2L)).thenReturn(Optional.empty());

        when(productoRepository.findById(2L)).thenReturn(Optional.of(producto));
        carritoService.agregar(COD_USUARIO, 2L,4);
        verify(itemCarritoRepository, times(1)).save(any(ItemCarrito.class));
    }
    @Test
    void vaciarTodosLosItemsDelUsuario() {
        carritoService = new CarritoService(itemCarritoRepository, productoRepository);

        carritoService.vaciar(COD_USUARIO);

        verify(itemCarritoRepository, times(1)).deleteByCodUsuario(COD_USUARIO);
    }

}

