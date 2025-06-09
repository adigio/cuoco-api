package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.Ingredient;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GetIngredientsFromTextCommand {

    List<Ingredient> execute(Command command);

    @Getter
    class Command {
        private final String text;
        private final String source;

        public Command(String text, String source) {
            this.text = text;
            this.source = source;
        }

    }
}