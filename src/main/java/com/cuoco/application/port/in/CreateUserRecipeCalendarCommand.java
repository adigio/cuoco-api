package com.cuoco.application.port.in;

import lombok.Builder;
import lombok.Data;

import java.util.List;

public interface CreateUserRecipeCalendarCommand {
    void execute(CreateUserRecipeCalendarCommand.Command command);

    @Data
    @Builder
    class Command {

        private List<CalendarCommand> calendarCommands;

        @Data
        @Builder
        public static class CalendarCommand {
            private final Integer dayId;
            private final List<CalendarRecipeCommand> calendarRecipeCommands;
        }

        @Data
        @Builder
        public static class CalendarRecipeCommand {
            private final Long recipeId;
            private final Integer mealtypeId;
        }
    }
}
