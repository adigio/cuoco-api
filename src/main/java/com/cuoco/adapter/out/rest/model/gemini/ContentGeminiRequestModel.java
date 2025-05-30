package com.cuoco.adapter.out.rest.model.gemini;

import java.util.List;

public class ContentGeminiRequestModel {
    private List<PartGeminiRequestModel> parts;

    public ContentGeminiRequestModel(List<PartGeminiRequestModel> parts) {
        this.parts = parts;
    }

    public List<PartGeminiRequestModel> getParts() {
        return parts;
    }

    public void setParts(List<PartGeminiRequestModel> parts) {
        this.parts = parts;
    }
}
