package com.cuoco.application.port.out;

public interface DeleteUserMealPrepRepository {
    void execute(Long userId, Long mealPrepId);
}
