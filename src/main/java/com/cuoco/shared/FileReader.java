package com.cuoco.shared;

import com.cuoco.adapter.exception.NotAvailableException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReader {

    public static String execute(String path) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            // Try to get resource
            URL resource = contextClassLoader.getResource(path);
            if (resource == null) {
                // Try with class loader
                resource = FileReader.class.getClassLoader().getResource(path);
            }
            
            if (resource != null) {
                return Files.readString(Paths.get(resource.toURI()));
            } else {
                throw new NotAvailableException("No se pudo encontrar el archivo: " + path);
            }
        } catch (IOException | URISyntaxException e) {
            throw new NotAvailableException("No se pudo leer el archivo: " + path + " - " + e.getMessage());
        }
    }
}