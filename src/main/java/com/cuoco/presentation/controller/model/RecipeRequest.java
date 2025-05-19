package com.cuoco.presentation.controller.model;

import com.cuoco.infrastructure.repository.hibernate.model.IngredientHibernateModel;

import java.util.List;

public class RecipeRequest {

    private List<Ingredient> ingredients;

    private List<IngredientHibernateModel> ingredientes;

    private FilterRequest filtros;

    public List<IngredientHibernateModel> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<IngredientHibernateModel> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public FilterRequest getFiltros() {
        return filtros;
    }

    public void setFiltros(FilterRequest filtros) {
        this.filtros = filtros;
    }
}
