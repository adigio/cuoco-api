package com.cuoco.application.port.out;

public interface UserExistsByEmailRepository {
    Boolean execute(String email);
}
