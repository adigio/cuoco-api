package com.cuoco.adapter.out.rest.model.gemini;

import lombok.Getter;

import java.util.List;

@Getter
public class ContentGeminiRequestModel {
    private List<PartGeminiRequestModel> parts;

    public ContentGeminiRequestModel(List<PartGeminiRequestModel> parts) {
        this.parts = parts;
    }

    public void setParts(List<PartGeminiRequestModel> parts) {
        this.parts = parts;
    }
}
