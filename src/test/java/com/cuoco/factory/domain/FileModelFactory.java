package com.cuoco.factory.domain;

import com.cuoco.application.usecase.model.File;

public class FileModelFactory {

    public static File create() {
        return File.builder()
                .fileName("test.png")
                .format("image/png")
                .mimeType("image/png")
                .fileBase64("base64string")
                .build();
    }

    public static File create(String fileName, String format, String base64) {
        return File.builder()
                .fileName(fileName != null ? fileName : "test.png")
                .format(format != null ? format: "image/png")
                .mimeType("image/png")
                .fileBase64(base64 != null ? base64 : "base64string")
                .build();
    }

}
