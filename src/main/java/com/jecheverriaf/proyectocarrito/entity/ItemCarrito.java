package com.jecheverriaf.proyectocarrito.entity;


//import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// esta entidad es la tabla shopping_cart
// cada fila es "este usuario tiene X unidades de este producto"
// @Data me genera getters, setters, toString, equals y hashCode
// @NoArgsConstructor es obligatorio para JPA, primero crea el objeto vacio y luego le pone los datos
@Entity
@Table(name = "shopping_cart")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ItemCarrito {
    // llave primaria, la genera la base de datos con AUTO_INCREMENT, por eso es null hasta que se guarda
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idShoppingCart")
    private Long id;

    // muchos items pueden apuntar al mismo producto

    // @JoinColumn es la columna de la llave foranea

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idProducto")
    private Producto producto;

    // el id del visitante que sale de la sesion
    @Column(name = "codUsuario")
    private String codUsuario;

    @Column(name = "cantidad")
    private Integer cantidad;


}
