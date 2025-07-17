package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.PreparationTimeHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllPreparationTimesHibernateRepositoryAdapter;
import com.cuoco.application.usecase.model.PreparationTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllPreparationTimesDatabaseRepositoryAdapterTest {

    @Mock
    private GetAllPreparationTimesHibernateRepositoryAdapter getAllPreparationTimesHibernateRepositoryAdapter;

    private GetAllPreparationTimesDatabaseRepositoryAdapter getAllPreparationTimesDatabaseRepositoryAdapter;

    @BeforeEach
    void setUp() {
        getAllPreparationTimesDatabaseRepositoryAdapter = new GetAllPreparationTimesDatabaseRepositoryAdapter(
                getAllPreparationTimesHibernateRepositoryAdapter
        );
    }

    @Test
    void shouldGetAllPreparationTimesSuccessfully() {
        // Given
        List<PreparationTimeHibernateModel> expectedPreparationTimes = List.of(
                PreparationTimeHibernateModel.builder().id(1).description("15 minutes").build(),
                PreparationTimeHibernateModel.builder().id(2).description("30 minutes").build()
        );
        when(getAllPreparationTimesHibernateRepositoryAdapter.findAll()).thenReturn(expectedPreparationTimes);

        // When
        List<PreparationTime> result = getAllPreparationTimesDatabaseRepositoryAdapter.execute();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("15 minutes", result.get(0).getDescription());
        assertEquals("30 minutes", result.get(1).getDescription());
    }
} 