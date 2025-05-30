package com.cuoco.adapter.out.rest.model.gemini;

public class InlineDataGeminiRequestModel {
    private String mimeType;
    private String data;

    public InlineDataGeminiRequestModel(String mimeType, String data) {
        this.mimeType = mimeType;
        this.data = data;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
