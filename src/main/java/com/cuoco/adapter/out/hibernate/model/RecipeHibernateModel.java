package com.cuoco.adapter.out.hibernate.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class RecipeHibernateModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String titulo;
    
    @Column(columnDefinition = "TEXT")
    private String contenido;
    
    @ManyToMany
    @JoinTable(
        name = "receta_ingrediente",
        joinColumns = @JoinColumn(name = "receta_id"),
        inverseJoinColumns = @JoinColumn(name = "ingrediente_id")
    )
    private List<IngredientHibernateModel> ingredientes;
    
    private Date fechaCreacion;
    
    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public List<IngredientHibernateModel> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<IngredientHibernateModel> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
} 