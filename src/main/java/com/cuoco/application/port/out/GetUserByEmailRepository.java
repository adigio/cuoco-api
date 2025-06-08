package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.User;

public interface GetUserByEmailRepository {
    User execute(String username);
}
