package com.cuoco.adapter.in.controller.model;

public class AuthRequest {
    private String username;
    private String password;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getNivelCocina() {
        return nivelCocina;
    }

    public void setNivelCocina(String nivelCocina) {
        this.nivelCocina = nivelCocina;
    }

    private String plan;
    private String nivelCocina;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
