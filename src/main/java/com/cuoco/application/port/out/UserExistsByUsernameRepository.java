package com.cuoco.application.port.out;

public interface UserExistsByUsernameRepository {
    Boolean execute(String username);
}
