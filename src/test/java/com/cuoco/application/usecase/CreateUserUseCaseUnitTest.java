package com.cuoco.application.usecase;

import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.in.CreateUserCommand;
import com.cuoco.application.port.out.*;
import com.cuoco.application.usecase.model.*;
import com.cuoco.shared.model.ErrorDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateUserUseCase - Unit Tests")
class CreateUserUseCaseUnitTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CreateUserRepository createUserRepository;

    @Mock
    private UserExistsByEmailRepository userExistsByEmailRepository;

    @Mock
    private GetPlanByIdRepository getPlanByIdRepository;

    @Mock
    private GetDietByIdRepository getDietByIdRepository;

    @Mock
    private GetCookLevelByIdRepository getCookLevelByIdRepository;

    @Mock
    private FindDietaryNeedsByDescriptionRepository findDietaryNeedsByDescriptionRepository;

    @Mock
    private FindAllergiesByDescriptionRepository findAllergiesByDescriptionRepository;

    private CreateUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateUserUseCase(
                passwordEncoder,
                createUserRepository,
                userExistsByEmailRepository,
                getPlanByIdRepository,
                getDietByIdRepository,
                getCookLevelByIdRepository,
                findDietaryNeedsByDescriptionRepository,
                findAllergiesByDescriptionRepository
        );
    }

    @Test
    @DisplayName("Test 1: Should create user successfully with valid data")
    void shouldCreateUserSuccessfullyWithValidData() {
        // Given
        CreateUserCommand.Command command = new CreateUserCommand.Command(
                "John Doe", "john@example.com", "password123",
                LocalDate.now(), "Basic", true,
                "Beginner", "Balanced",
                Collections.emptyList(), Collections.emptyList()
        );

        Plan plan = new Plan(1, "Basic");
        Diet diet = new Diet(1, "Balanced");
        CookLevel cookLevel = new CookLevel(1, "Beginner");

        User expectedUser = new User(1L, "John Doe", "john@example.com", null, plan, true,
                new UserPreferences(cookLevel, diet), LocalDateTime.now(),
                Collections.emptyList(), Collections.emptyList());

        when(userExistsByEmailRepository.execute("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(getPlanByIdRepository.execute(1)).thenReturn(plan);
        when(getDietByIdRepository.execute(1)).thenReturn(diet);
        when(getCookLevelByIdRepository.execute(1)).thenReturn(cookLevel);
        when(createUserRepository.execute(any(User.class))).thenReturn(expectedUser);

        // When
        User result = useCase.execute(command);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertNull(result.getPassword()); // Password is cleared
        assertTrue(result.getActive());

        verify(userExistsByEmailRepository, times(1)).execute("john@example.com");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(createUserRepository, times(1)).execute(any(User.class));
    }

    @Test
    @DisplayName("Test 2: Should throw BadRequestException when email already exists")
    void shouldThrowBadRequestExceptionWhenEmailAlreadyExists() {
        // Given
        CreateUserCommand.Command command = new CreateUserCommand.Command(
                "Jane Doe", "jane@example.com", "pass",
                LocalDate.now(), "Plan", true, "Level", "Diet",
                Collections.emptyList(), Collections.emptyList()
        );

        when(userExistsByEmailRepository.execute("jane@example.com")).thenReturn(true);

        // When & Then
        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            useCase.execute(command);
        });

        assertEquals(ErrorDescription.DUPLICATED.getValue(), ex.getDescription());
        verify(userExistsByEmailRepository, times(1)).execute("jane@example.com");
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(createUserRepository);
    }

    @Test
    @DisplayName("Test 3: Should create user with dietary needs successfully")
    void shouldCreateUserWithDietaryNeedsSuccessfully() {
        // Given
        List<String> dietaryNeedDescriptions = Arrays.asList("Low sodium", "High protein");
        CreateUserCommand.Command command = new CreateUserCommand.Command(
                "Jane Doe",
                "jane@example.com",
                "password123",
                LocalDate.now(),
                "Premium",
                true,
                "Intermediate",
                "Keto",
                dietaryNeedDescriptions,
                Collections.emptyList()
        );

        Plan plan = new Plan(1, "Basic");
        Diet diet = new Diet(1, "Balanced");
        CookLevel cookLevel = new CookLevel(1, "Beginner");

        List<DietaryNeed> dietaryNeeds = Arrays.asList(
                new DietaryNeed(1, "Low sodium"),
                new DietaryNeed(2, "High protein")
        );

        User expectedUser = new User(
                2L, "Jane Doe", "jane@example.com", null, plan, true,
                new UserPreferences(cookLevel, diet), LocalDateTime.now(),
                dietaryNeeds, Collections.emptyList()
        );

        when(userExistsByEmailRepository.execute("jane@example.com")).thenReturn(false);
        when(findDietaryNeedsByDescriptionRepository.execute(dietaryNeedDescriptions)).thenReturn(dietaryNeeds);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(getPlanByIdRepository.execute(1)).thenReturn(plan);
        when(getDietByIdRepository.execute(1)).thenReturn(diet);
        when(getCookLevelByIdRepository.execute(1)).thenReturn(cookLevel);
        when(createUserRepository.execute(any(User.class))).thenReturn(expectedUser);

        // When
        User result = useCase.execute(command);

        // Then
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Jane Doe", result.getName());
        assertEquals(2, result.getDietaryNeeds().size());
        assertTrue(result.getDietaryNeeds().stream().anyMatch(need -> "Low sodium".equals(need.getDescription())));

        verify(findDietaryNeedsByDescriptionRepository, times(1)).execute(dietaryNeedDescriptions);
        verify(userExistsByEmailRepository, times(1)).execute("jane@example.com");
        verify(createUserRepository, times(1)).execute(any(User.class));
    }

    @Test
    @DisplayName("Test 4: Should throw BadRequestException when dietary need does not exist")
    void shouldThrowWhenDietaryNeedDoesNotExist() {
        // Given
        List<String> inputNeeds = Arrays.asList("Unknown", "Another Unknown");
        List<DietaryNeed> foundNeeds = Arrays.asList(new DietaryNeed(1, "Unknown")); // Only 1 found out of 2

        CreateUserCommand.Command command = new CreateUserCommand.Command(
                "Invalid", "fail@example.com", "pass", LocalDate.now(),
                "Plan", true, "Level", "Diet", inputNeeds, Collections.emptyList()
        );

        when(userExistsByEmailRepository.execute("fail@example.com")).thenReturn(false);
        when(findDietaryNeedsByDescriptionRepository.execute(inputNeeds)).thenReturn(foundNeeds);

        // When & Then
        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            useCase.execute(command);
        });

        assertEquals(ErrorDescription.PREFERENCES_NOT_EXISTS.getValue(), ex.getDescription());
        verify(findDietaryNeedsByDescriptionRepository, times(1)).execute(inputNeeds);
        verifyNoInteractions(createUserRepository);
    }

    @Test
    @DisplayName("Test 5: Should throw BadRequestException when allergy does not exist")
    void shouldThrowWhenAllergyDoesNotExist() {
        // Given
        List<String> allergies = Arrays.asList("Non-existent allergy", "Another fake allergy");
        List<Allergy> foundAllergies = Arrays.asList(new Allergy(1, "Non-existent allergy")); // Only 1 found out of 2

        CreateUserCommand.Command command = new CreateUserCommand.Command(
                "Allergic", "allergy@example.com", "pass", LocalDate.now(),
                "Plan", true, "Level", "Diet", Collections.emptyList(), allergies
        );

        when(userExistsByEmailRepository.execute("allergy@example.com")).thenReturn(false);
        when(findAllergiesByDescriptionRepository.execute(allergies)).thenReturn(foundAllergies);

        // When & Then
        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            useCase.execute(command);
        });

        assertEquals(ErrorDescription.ALLERGIES_NOT_EXISTS.getValue(), ex.getDescription());
        verify(findAllergiesByDescriptionRepository, times(1)).execute(allergies);
        verifyNoInteractions(createUserRepository);
    }
}