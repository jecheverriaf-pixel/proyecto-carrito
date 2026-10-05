package com.jecheverriaf.proyectocarrito.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Producto {
    // entidad de la tabla producto, cada atributo es una columna
    // los @Column llevan el nombre tal cual esta en la base de datos (camelCase), por eso en el properties puse la naming strategy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProducto")
    private Long id;

    @Column(name = "nombre")
    private String nombre;
    // nullable = false porque en la tabla codigo es NOT NULL
    @Column(name = "codigo", nullable = false)
    private String codigo;

    // aqui solo guardo la ruta de la imagen
    @Column(name = "imagen")
    private String imagen;

    @Column(name = "precio")
    private Double precio;

}
