package com.cuoco.model;

import java.util.List;

public class Filtros {

    private String tiempo;

    private String dificultad;

    private List tipos;

    private String dieta;

    private int personas;

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public List getTipos() {
        return tipos;
    }

    public void setTipos(List tipos) {
        this.tipos = tipos;
    }

    public String getDieta() {
        return dieta;
    }

    public void setDieta(String dieta) {
        this.dieta = dieta;
    }

    public int getPersonas() {
        return personas;
    }

    public void setPersonas(int personas) {
        this.personas = personas;
    }
}
