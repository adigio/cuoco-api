package com.cuoco.infrastructure.repository.hibernate.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ingredientes")
@Getter
@Setter
@AllArgsConstructor
public class IngredientHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    private String fuente;

    private boolean confirmado;

    public IngredientHibernateModel(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public void setConfirmado(boolean confirmado) {
        this.confirmado = confirmado;
    }

    public IngredientHibernateModel() {



    }
}
