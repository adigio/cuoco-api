package com.cuoco.shared.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageConstants {

    MAIN_IMAGE_NAME("main"),
    STEP_INFIX("_step_"),
    MAIN_TYPE("main"),
    STEP_TYPE("step"),

    MAX_STEPS_SIZE_INT("3"),
    MAX_INGREDIENTS_SIZE_INT("5"),
    INSTRUCTIONS_SPLIT_PATTERN("\\d+\\.|\\n|\\r\\n|;"),

    MIME_TYPE_BASE("image/"),

    JPEG_FORMAT("jpeg"),
    PNG_FORMAT("png"),
    WEBP_FORMAT("webp");

    private final String value;

}
