package com.cuoco.adapter.out.rest.model.gemini.voice;

public class AudioMimeTypeMapper {

    /**
     * Mapear formatos de archivo a MIME types para Gemini
     */
    public static String getMimeType(String format) {
        if (format == null) {
            return "audio/mp3";
        }

        return switch (format.toLowerCase()) {
            case "mp3" -> "audio/mp3";
            case "wav" -> "audio/wav";
            case "ogg" -> "audio/ogg";
            case "aac" -> "audio/aac";
            case "flac" -> "audio/flac";
            case "m4a" -> "audio/mp4";
            default -> "audio/" + format;
        };
    }
}