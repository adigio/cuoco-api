package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.User;

import java.util.List;

public interface SaveUserRecipeCalendarCommand {
    Boolean execute(SaveUserRecipeCalendarCommand.Command command);

    class Command {

        private List<CalendarCommand> calendarCommands;

        public Command(List<CalendarCommand> calendarCommands) {
            this.calendarCommands = calendarCommands;
        }

        public List<CalendarCommand> getCalendarCommands() {
            return calendarCommands;
        }

        public void setCalendarCommands(List<CalendarCommand> calendarCommands) {
            this.calendarCommands = calendarCommands;
        }

        public static class CalendarCommand {
            private final int dayId;
            private final Long recipeId;
            private final int mealtypeId;

            public CalendarCommand(int dayId, Long recipeId, int mealtypeId) {
                this.dayId = dayId;
                this.recipeId = recipeId;
                this.mealtypeId = mealtypeId;
            }

            public int getDayId() {
                return dayId;
            }

            public Long getRecipeId() {
                return recipeId;
            }

            public int getMealtypeId() {
                return mealtypeId;
            }
        }

    }
}
