package com.cuoco.application.port.in;


import com.cuoco.application.usecase.model.DietaryNeeds;
import lombok.Data;
import lombok.ToString;

public interface CreateUserDietaryCommand {

    DietaryNeeds execute(Command command);

    @Data
    @ToString
    class Command {
        private final DietaryNeeds dietaryNeeds;

        public Command(DietaryNeeds dietaryNeeds) {
            this.dietaryNeeds = dietaryNeeds;
        }

    }
}
