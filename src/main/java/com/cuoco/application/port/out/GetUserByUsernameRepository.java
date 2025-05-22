package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.User;

public interface GetUserByUsernameRepository {
    User execute(String username);
}
