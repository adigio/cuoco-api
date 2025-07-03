package com.cuoco.application.port.out;

public interface ExistsUserByEmailRepository {
    Boolean execute(String email);
}
