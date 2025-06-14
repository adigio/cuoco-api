package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.Ingredient;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface GetIngredientsFromFileCommand {

    List<Ingredient> execute(Command command);

    Map<String, List<Ingredient>> executeWithSeparation(Command command);

    class Command {

        private final List<MultipartFile> files;

        public Command(List<MultipartFile> files) {
            this.files = files;
        }

        public List<MultipartFile> getFiles() {
            return files;
        }

        @Override
        public String toString() {
            return "Command{" +
                    "files=" + files +
                    '}';
        }
    }
}