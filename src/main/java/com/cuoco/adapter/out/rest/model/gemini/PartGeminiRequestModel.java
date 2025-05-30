package com.cuoco.adapter.out.rest.model.gemini;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartGeminiRequestModel {
    private InlineDataGeminiRequestModel inlineData;
    private String text;

    public PartGeminiRequestModel(InlineDataGeminiRequestModel inlineData, String text) {
        this.inlineData = inlineData;
        this.text = text;
    }

    public InlineDataGeminiRequestModel getInlineData() {
        return inlineData;
    }

    public void setInlineData(InlineDataGeminiRequestModel inlineData) {
        this.inlineData = inlineData;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
