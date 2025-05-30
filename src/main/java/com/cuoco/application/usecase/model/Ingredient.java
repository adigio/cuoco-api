package com.cuoco.application.usecase.model;

public class Ingredient {

    private String name;
    private String source;
    private boolean confirmed;

    public Ingredient(String name, String source, boolean confirmed) {
        this.name = name;
        this.source = source;
        this.confirmed = confirmed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
