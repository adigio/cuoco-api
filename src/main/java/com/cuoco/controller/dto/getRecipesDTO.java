package com.cuoco.controller.dto;

import com.cuoco.model.Filtros;
import com.cuoco.model.Ingrediente;

import java.util.List;

public class getRecipesDTO {

    private List<Ingrediente> ingredientes;

    private Filtros filtros;

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public Filtros getFiltros() {
        return filtros;
    }

    public void setFiltros(Filtros filtros) {
        this.filtros = filtros;
    }
}
