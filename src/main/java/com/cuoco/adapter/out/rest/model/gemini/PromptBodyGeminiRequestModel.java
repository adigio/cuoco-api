package com.cuoco.adapter.out.rest.model.gemini;

import java.util.List;

public class PromptBodyGeminiRequestModel {
    private List<ContentGeminiRequestModel> contents;
    private GenerationConfigurationGeminiRequestModel generationConfig;

    public PromptBodyGeminiRequestModel(List<ContentGeminiRequestModel> contents, GenerationConfigurationGeminiRequestModel generationConfig) {
        this.contents = contents;
        this.generationConfig = generationConfig;
    }

    public List<ContentGeminiRequestModel> getContents() {
        return contents;
    }

    public void setContents(List<ContentGeminiRequestModel> contents) {
        this.contents = contents;
    }

    public GenerationConfigurationGeminiRequestModel getGenerationConfig() {
        return generationConfig;
    }

    public void setGenerationConfig(GenerationConfigurationGeminiRequestModel generationConfig) {
        this.generationConfig = generationConfig;
    }
}
