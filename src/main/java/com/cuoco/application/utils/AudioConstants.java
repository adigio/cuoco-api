package com.cuoco.application.utils;

import java.util.List;

public final class AudioConstants {

    private AudioConstants() {}

    public static final String MP3 = "mp3";
    public static final String WAV = "wav";
    public static final String OGG = "ogg";
    public static final String AAC = "aac";
    public static final String FLAC = "flac";
    public static final String M4A = "m4a";

    public static final List<String> SUPPORTED_EXTENSIONS = List.of(
            MP3, WAV, OGG, AAC, FLAC, M4A
    );
}