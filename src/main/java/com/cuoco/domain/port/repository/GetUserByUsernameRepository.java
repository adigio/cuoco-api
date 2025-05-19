package com.cuoco.domain.port.repository;

import com.cuoco.domain.model.User;

public interface GetUserByUsernameRepository {
    User execute(String username);
}
