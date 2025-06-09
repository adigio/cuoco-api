package com.cuoco.adapter.in.controller.model;

import lombok.Getter;

@Getter
public class TextRequest {
    // Getters and setters
    private String text;
    private String source; // "texto", "voz", etc.

    public TextRequest() {}

    public TextRequest(String text, String source) {
        this.text = text;
        this.source = source;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "TextRequest{" +
                "text='" + text + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}