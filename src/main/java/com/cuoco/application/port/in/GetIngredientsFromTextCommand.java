package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.Ingredient;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GetIngredientsFromTextCommand {

    List<Ingredient> execute(Command command);

    @Data
    @Builder
    class Command {
        private final String text;
        private final String source;
    }
}