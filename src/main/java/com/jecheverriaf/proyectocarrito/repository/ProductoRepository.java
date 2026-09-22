package com.jecheverriaf.proyectocarrito.repository;

import com.jecheverriaf.proyectocarrito.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
