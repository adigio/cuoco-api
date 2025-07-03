package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.User;

public interface GetUserByIdRepository {
    User execute(Long id);
}
