package com.cuoco.shared.model;

public enum ErrorDescription {

    ALLERGIES_NOT_EXISTS("Las alergias ingresadas no existen."),
    PREFERENCES_NOT_EXISTS("Las preferencias ingresadas no existen."),
    PLAN_NOT_EXISTS("El plan ingresado no existe."),
    COOK_LEVEL_NOT_EXISTS("El nivel de dificulad ingresado no existe."),
    DIET_NOT_EXISTS("La dieta ingresada no existe."),
    UNEXPECTED_ERROR("An unexpected error occurred: "),
    UNHANDLED("Ha ocurrido un error inesperado."),
    NOT_AVAILABLE("El servicio no esta disponible."),
    NOT_FOUND("Recurso no encontrado."),
    DUPLICATED("El recurso ya existe.");

    private final String value;

    ErrorDescription(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}