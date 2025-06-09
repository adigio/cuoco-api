package com.cuoco.adapter.in.controller.model;

import lombok.Getter;

@Getter
public class IngredientRequest {

    private String name;
    private String source;
    private boolean confirmed;

    public IngredientRequest(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}


