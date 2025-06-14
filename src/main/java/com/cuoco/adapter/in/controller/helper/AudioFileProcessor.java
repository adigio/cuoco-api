package com.cuoco.adapter.in.controller.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

@Component
public class AudioFileProcessor {

    private static final List<String> SUPPORTED_EXTENSIONS =
            List.of("mp3", "wav", "ogg", "aac", "flac", "m4a");

    public boolean isValidAudioFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();

        if (contentType != null) {
            return contentType.startsWith("audio/");
        }

        if (filename != null && filename.contains(".")) {
            String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
            return SUPPORTED_EXTENSIONS.contains(extension);
        }

        return false;
    }

    public String getAudioFormat(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType != null && contentType.contains("/")) {
            return contentType.substring(contentType.indexOf('/') + 1);
        }

        String filename = file.getOriginalFilename();
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        }

        return "mp3";
    }

    public String convertToBase64(MultipartFile file) throws Exception {
        return Base64.getEncoder().encodeToString(file.getBytes());
    }

    public String getSupportedFormatsMessage() {
        return "Archivo no válido. Formatos soportados: MP3, WAV, OGG, AAC, FLAC, M4A";
    }
}