package com.cuoco.shared.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorDescription {

    RECIPE_NOT_EXISTS("El ID de la receta ingresada no existe"),
    MEAL_PREP_NOT_EXISTS("El ID del meal prep ingresado no existe"),
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

    PRO_FEATURE("Esta funcionalidad solo es para usuarios PRO"),
    USER_NOT_EXISTS("El usuario ingresado no existe"),
    USER_DUPLICATED("El usuario ya existe"),
    NO_AUTH_TOKEN("El token no esta presente"),
    INVALID_CREDENTIALS("Las credenciales no son válidas"),
    EXPIRED_CREDENTIALS("El token ha expirado"),

    INVALID_AUDIO_FILE_EXTENSION("La extensión del archivo de audio no es valida"),
    AUDIO_FILE_IS_REQUIRED("El archivo de audio no esta presente y es requerido"),
    AUDIO_FILE_PROCESSING_ERROR("Error procesando el archivo de audio"),

    INVALID_PAYMENT_PLAN("El plan ingresado no es válido para el pago"),

    FORBIDDEN("No está autorizado a usar esta función"),
    UNAUTHORIZED("Es requerido autenticarse"),
    UNEXPECTED_ERROR("An unexpected error occurred: "),
    UNHANDLED("Ha ocurrido un error inesperado"),
    NOT_AVAILABLE("El servicio no esta disponible"),
    NOT_FOUND("Recurso no encontrado"),
    VALIDATION_ERROR("Error de validacion"),
    DUPLICATED("El recurso ya existe");

    private final String value;
}