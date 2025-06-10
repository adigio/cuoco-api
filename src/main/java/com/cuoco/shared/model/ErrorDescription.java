package com.cuoco.shared.model;

public enum ErrorDescription {

    ALLERGIES_NOT_EXISTS("Uno o mas valores de allergies no existen"),
    DIETARY_NEEDS_NOT_EXISTS("Uno o mas valores de dietary-needs no existen"),
    PREFERENCES_NOT_EXISTS("Las preferencias ingresadas no existen"),
    PLAN_NOT_EXISTS("El plan ingresado no existe"),
    COOK_LEVEL_NOT_EXISTS("El nivel de dificulad ingresado no existe"),
    DIET_NOT_EXISTS("La dieta ingresada no existe"),
    USER_NOT_EXISTS("El usuario ingresado no existe"),
    USER_DUPLICATED("El usuario ya existe"),
    UNEXPECTED_ERROR("An unexpected error occurred: "),
    UNHANDLED("Ha ocurrido un error inesperado"),
    NOT_AVAILABLE("El servicio no esta disponible"),
    NOT_FOUND("Recurso no encontrado"),
    VALIDATION_ERROR("Error de validacion"),
    DUPLICATED("El recurso ya existe");

    private final String value;

    ErrorDescription(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}