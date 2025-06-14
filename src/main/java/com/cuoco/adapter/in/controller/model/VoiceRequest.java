package com.cuoco.adapter.in.controller.model;

import lombok.Getter;

@Getter
public class VoiceRequest {
    // Getters and setters
    private String audioBase64; // Audio encoded in base64
    private String format; // "wav", "mp3", etc.
    private String language; // "es-ES", "en-US", etc.

    public VoiceRequest() {}

    public VoiceRequest(String audioBase64, String format, String language) {
        this.audioBase64 = audioBase64;
        this.format = format;
        this.language = language;
    }

    public void setAudioBase64(String audioBase64) {
        this.audioBase64 = audioBase64;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "VoiceRequest{" +
                "format='" + format + '\'' +
                ", language='" + language + '\'' +
                ", audioBase64Length=" + (audioBase64 != null ? audioBase64.length() : 0) +
                '}';
    }
}