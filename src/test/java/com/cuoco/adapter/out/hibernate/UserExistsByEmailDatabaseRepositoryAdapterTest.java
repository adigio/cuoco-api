package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.repository.UserExistsByEmailHibernateRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserExistsByEmailDatabaseRepositoryAdapterTest {

    @Mock
    private UserExistsByEmailHibernateRepositoryAdapter hibernateRepository;

    private UserExistsByEmailDatabaseRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new UserExistsByEmailDatabaseRepositoryAdapter(hibernateRepository);
    }

    @Test
    void whenEmailExists_thenReturnTrue() {
        String email = "test@example.com";
        when(hibernateRepository.existsByEmail(email)).thenReturn(true);

        Boolean result = adapter.execute(email);

        assertTrue(result);
        verify(hibernateRepository).existsByEmail(email);
    }

    @Test
    void whenEmailDoesNotExist_thenReturnFalse() {
        String email = "notfound@example.com";
        when(hibernateRepository.existsByEmail(email)).thenReturn(false);

        Boolean result = adapter.execute(email);

        assertFalse(result);
        verify(hibernateRepository).existsByEmail(email);
    }
}
