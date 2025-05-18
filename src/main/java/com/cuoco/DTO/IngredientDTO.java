package com.cuoco.DTO;


import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode

public  class IngredientDTO {
        private String nombre;
        private String fuente;
        private boolean confirmado;

        public IngredientDTO() {
        }

        public IngredientDTO(String nombre, String fuente, boolean confirmado) {
            this.nombre = nombre;
            this.fuente = fuente;
            this.confirmado = confirmado;
        }

    }


