package com.jecheverriaf.proyectocarrito.service;

import com.jecheverriaf.proyectocarrito.dto.ProductoDTO;
import com.jecheverriaf.proyectocarrito.entity.Producto;
import com.jecheverriaf.proyectocarrito.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


// service de productos, por ahora solo sirve para listar el catalogo
@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;

    // findAll ya viene con JpaRepository, no lo tuve que escribir
    // convierto cada producto a DTO para no mandar la entidad directo a la vista
    public List<ProductoDTO> listarTodos() {
        return productoRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }
    // pasa los datos de la entidad al DTO, es private porque solo se usa aqui adentro
    private ProductoDTO toDTO(Producto producto) {
        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getCodigo(),
                producto.getImagen(),
                producto.getPrecio());
    }
}
