package com.cuoco.adapter.out.rest.model.gemini;

public class GenerationConfigurationGeminiRequestModel {
    private Double temperature;

    public GenerationConfigurationGeminiRequestModel(Double temperature) {
        this.temperature = temperature;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
}
