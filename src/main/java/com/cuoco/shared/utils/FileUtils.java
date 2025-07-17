package com.cuoco.shared.utils;

import java.util.List;
import java.util.Map;

public class FileUtils {

    public static final List<String> SUPPORTED_AUDIO_EXTENSIONS = List.of(
            AudioConstants.MP3,
            AudioConstants.WAV,
            AudioConstants.OGG,
            AudioConstants.AAC,
            AudioConstants.FLAC,
            AudioConstants.M4A
    );

    public static String getAudioMimeType(String format) {

        if (format == null) {
            return AudioConstants.MIMETYPE_BASE + AudioConstants.MP3;
        }

        return switch (format.toLowerCase()) {
            case AudioConstants.MP3 -> AudioConstants.MIMETYPE_BASE + AudioConstants.MP3;
            case AudioConstants.WAV -> AudioConstants.MIMETYPE_BASE + AudioConstants.WAV;
            case AudioConstants.OGG -> AudioConstants.MIMETYPE_BASE + AudioConstants.OGG;
            case AudioConstants.AAC -> AudioConstants.MIMETYPE_BASE + AudioConstants.AAC;
            case AudioConstants.FLAC -> AudioConstants.MIMETYPE_BASE + AudioConstants.FLAC;
            case AudioConstants.M4A -> AudioConstants.MIMETYPE_BASE + AudioConstants.M4A;
            default -> AudioConstants.MIMETYPE_BASE + format;
        };
    }

    public static String getImageFormat(String mimeType) {

        if (mimeType == null) {
            return null;
        }

        Map<String, String> formatMap = Map.of(
                ImageConstants.MIME_TYPE_BASE.getValue() + ImageConstants.JPEG_FORMAT.getValue(), ImageConstants.JPEG_FORMAT.getValue(),
                ImageConstants.MIME_TYPE_BASE.getValue() + ImageConstants.WEBP_FORMAT.getValue(), ImageConstants.WEBP_FORMAT.getValue()
        );

        String format = formatMap.getOrDefault(mimeType, ImageConstants.PNG_FORMAT.getValue());
        return Constants.DOT.getValue() + format;
    }
}
