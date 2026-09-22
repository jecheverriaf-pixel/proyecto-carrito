package com.jecheverriaf.proyectocarrito.repository;

import com.jecheverriaf.proyectocarrito.entity.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jecheverriaf.proyectocarrito.entity.ItemCarrito;

import java.util.List;
import java.util.Optional;

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {

    @Query("SELECT ic FROM ItemCarrito ic JOIN FETCH ic.prodducto WHERE ic.codUsuario = :codUsuario")
    List<ItemCarrito> findByCodUsuario(@Param("codUsuario") String codUsuario);

    Optional<ItemCarrito> findByCodUsuarioAndProducto_Id(String codUsuario, Long idProducto);
     void deleteByCodUsuario(String codUsuario);
}
