package com.jecheverriaf.proyectocarrito.repository;

import com.jecheverriaf.proyectocarrito.entity.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jecheverriaf.proyectocarrito.entity.ItemCarrito;

import java.util.List;
import java.util.Optional;

// repositorio de los items del carrito
// al extender JpaRepository<ItemCarrito, Long> ya tengo save, findById, deleteById, existsById, etc
// es una interfaz, spring crea la implementacion solo

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {

    // esta consulta es (usa nombres de clases, no de tablas)
    // el JOIN FETCH trae los items con su producto en una sola consulta, asi evito sobrecarga

    @Query("SELECT ic FROM ItemCarrito ic JOIN FETCH ic.producto WHERE ic.codUsuario = :codUsuario")
    List<ItemCarrito> findByCodUsuario(@Param("codUsuario") String codUsuario);

    // spring arma el query leyendo el nombre del metodo: codUsuario = ? AND producto.id = ?
    // el _ es para entrar al id del producto. devuelve Optional porque puede que no exista
    Optional<ItemCarrito> findByCodUsuarioAndProducto_Id(String codUsuario, Long idProducto);

    boolean existsByIdAndCodUsuario(Long id, String codUsuario);
    // borra todos los items de ese usuario, este necesita estar dentro de una transaccion
     void deleteByCodUsuario(String codUsuario);
}
