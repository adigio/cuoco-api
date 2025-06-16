package com.cuoco.shared;

import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.adapter.exception.UnprocessableException;
import jakarta.servlet.UnavailableException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class FileReader {

    public static String execute(String path) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            return Files.readString(Paths.get(Objects.requireNonNull(contextClassLoader.getResource(path)).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new NotAvailableException("No se pudo leer el archivo: " + path);
        }
    }
}