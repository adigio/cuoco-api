package com.cuoco.service.excepciones;

public class IngredienteNoDetectadoException extends RuntimeException {
    public IngredienteNoDetectadoException(String mensaje) {
        super(mensaje);
    }

}
