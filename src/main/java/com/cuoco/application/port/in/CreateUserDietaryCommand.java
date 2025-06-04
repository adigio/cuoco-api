package com.cuoco.application.port.in;


import com.cuoco.application.usecase.model.UserDietaryNeeds;
import lombok.Data;
import lombok.ToString;

public interface CreateUserDietaryCommand {

    UserDietaryNeeds execute(Command command);

    @Data
    @ToString
    class Command {
        private final UserDietaryNeeds userDietaryNeeds;

        public Command(UserDietaryNeeds userDietaryNeeds) {
            this.userDietaryNeeds = userDietaryNeeds;
        }

    }
}
