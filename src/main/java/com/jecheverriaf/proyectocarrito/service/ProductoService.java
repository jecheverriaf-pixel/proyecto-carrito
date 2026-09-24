package com.jecheverriaf.proyectocarrito.service;

import com.jecheverriaf.proyectocarrito.dto.ProductoDTO;
import com.jecheverriaf.proyectocarrito.entity.Producto;
import com.jecheverriaf.proyectocarrito.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;

    public List<ProductoDTO> listarTodos() {
        return productoRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }
    private ProductoDTO toDTO(Producto producto) {
        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getCodigo(),
                producto.getImagen(),
                producto.getPrecio());
    }
}
