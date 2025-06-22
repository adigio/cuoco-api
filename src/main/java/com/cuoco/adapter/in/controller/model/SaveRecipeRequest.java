package com.cuoco.adapter.in.controller.model;

import lombok.Data;

@Data
public class SaveRecipeRequest {

    private String mail;
    private String titleRecipe;

    public SaveRecipeRequest(String mail, String titleRecipe) {
        this.mail = mail;
        this.titleRecipe = titleRecipe;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTitleRecipe() {
        return titleRecipe;
    }

    public void setTitleRecipe(String titleRecipe) {
        this.titleRecipe = titleRecipe;
    }


    //todo modificar esto cuando tengamos el request
}
