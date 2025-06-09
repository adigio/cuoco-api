package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.Ingredient;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public interface GetIngredientsFromVoiceCommand {

    List<Ingredient> execute(Command command);

    CompletableFuture<List<Ingredient>> executeAsync(Command command);

    @Data
    class Command {
        private final String audioBase64;
        private final String format;
        private final String language;

        public Command(String audioBase64, String format, String language) {
            this.audioBase64 = audioBase64;
            this.format = format;
            this.language = language;
        }

    }
}