package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.UserCalendarsHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllUserCalendarsByUserIdHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetUserCalendarByUserIdRepository;
import com.cuoco.application.usecase.model.Calendar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class GetAllUserRecipesCalendarByUserByUserIdRepositoryAdapter implements GetUserCalendarByUserIdRepository {

    private final GetAllUserCalendarsByUserIdHibernateRepositoryAdapter getAllUserCalendarsByUserIdHibernateRepositoryAdapter;

    public GetAllUserRecipesCalendarByUserByUserIdRepositoryAdapter(
            GetAllUserCalendarsByUserIdHibernateRepositoryAdapter getAllUserCalendarsByUserIdHibernateRepositoryAdapter
    ) {
        this.getAllUserCalendarsByUserIdHibernateRepositoryAdapter = getAllUserCalendarsByUserIdHibernateRepositoryAdapter;
    }

    @Override
    public List<Calendar> execute(Long userId) {
        log.info("Executing get all calendars for user with ID {} in database", userId);
        List<UserCalendarsHibernateModel> response = getAllUserCalendarsByUserIdHibernateRepositoryAdapter.findAllByUserId(userId);
        return response.stream().map(UserCalendarsHibernateModel::toDomain).toList();
    }
}
