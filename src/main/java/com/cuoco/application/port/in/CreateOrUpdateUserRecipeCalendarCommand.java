package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.Calendar;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public interface CreateOrUpdateUserRecipeCalendarCommand {
    void execute(Command command);

    @Data
    @Builder
    class Command {
        private List<Calendar> calendars;
    }
}
