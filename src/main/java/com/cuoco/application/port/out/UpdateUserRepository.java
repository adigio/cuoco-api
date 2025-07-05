package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.User;

public interface UpdateUserRepository {
    void execute(User user);

}
