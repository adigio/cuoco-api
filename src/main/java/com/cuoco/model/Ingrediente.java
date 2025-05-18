package com.cuoco.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ingredientes")
@Getter
@Setter
@AllArgsConstructor
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    public Ingrediente(String nombre) {
        this.nombre = nombre;
    }

    public Ingrediente() {

        
    }
}
