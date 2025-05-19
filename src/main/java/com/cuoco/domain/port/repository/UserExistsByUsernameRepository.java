package com.cuoco.domain.port.repository;

public interface UserExistsByUsernameRepository {
    Boolean execute(String username);
}
