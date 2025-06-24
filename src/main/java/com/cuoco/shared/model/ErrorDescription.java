package com.cuoco.shared.model;

public enum ErrorDescription {

    ALLERGIES_NOT_EXISTS("Uno o mas valores de allergies no existen"),
    DIETARY_NEEDS_NOT_EXISTS("Uno o mas valores de dietary-needs no existen"),
    PREFERENCES_NOT_EXISTS("Las preferencias ingresadas no existen"),
    PLAN_NOT_EXISTS("El plan ingresado no existe"),
    COOK_LEVEL_NOT_EXISTS("El nivel de dificulad ingresado no existe"),
    DIET_NOT_EXISTS("La dieta ingresada no existe"),
    MEAL_CATEGORY_NOT_EXISTS("La categoria de la receta ingresada no existe"),
    MEAL_TYPE_NOT_EXISTS("El tipo de receta ingresado no existe"),
    PREPARATION_TIME_NOT_EXISTS("El tiempo de preparacion ingresado no existe"),

    INGREDIENTS_EMPTY("Es necesario por lo menos un ingrediente para continuar"),

    USER_NOT_EXISTS("El usuario ingresado no existe"),
    USER_DUPLICATED("El usuario ya existe"),
    INVALID_CREDENTIALS("Las credenciales no son válidas"),
    INVALID_TOKEN("El token no es valido"),
    EXPIRED_TOKEN("El token ha expirado"),

    INVALID_AUDIO_FILE_EXTENSION("La extensión del archivo de audio no es valida"),
    AUDIO_FILE_IS_REQUIRED("El archivo de audio no esta presente y es requerido"),
    AUDIO_FILE_PROCESSING_ERROR("Error procesando el archivo de audio"),

    UNAUTHORIZED("El token no esta presente"),
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