package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.User;

public interface UpdateUserActiveRepository {
    void execute(User user);
}
