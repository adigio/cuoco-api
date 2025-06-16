package com.cuoco.application.usecase.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class File {
    private String fileName;
    private String fileBase64;
    private String format;
    private String mimeType;
}
