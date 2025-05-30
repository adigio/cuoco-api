package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.User;

public interface CreateUserRepository {
    User execute(User user);
}
