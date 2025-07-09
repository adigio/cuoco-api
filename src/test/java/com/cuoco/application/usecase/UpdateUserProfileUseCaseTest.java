package com.cuoco.application.usecase;

import com.cuoco.application.port.in.UpdateUserProfileCommand;
import com.cuoco.application.port.out.GetAllergiesByIdRepository;
import com.cuoco.application.port.out.GetCookLevelByIdRepository;
import com.cuoco.application.port.out.GetDietByIdRepository;
import com.cuoco.application.port.out.GetDietaryNeedsByIdRepository;
import com.cuoco.application.port.out.GetPlanByIdRepository;
import com.cuoco.application.port.out.UpdateUserRepository;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.Diet;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.Plan;
import com.cuoco.application.usecase.model.User;
import com.cuoco.factory.domain.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserProfileUseCaseTest {

    @Mock
    private UpdateUserRepository updateUserRepository;
    @Mock
    private GetPlanByIdRepository getPlanByIdRepository;
    @Mock
    private GetDietByIdRepository getDietByIdRepository;
    @Mock
    private GetCookLevelByIdRepository getCookLevelByIdRepository;
    @Mock
    private GetDietaryNeedsByIdRepository getDietaryNeedsByIdRepository;
    @Mock
    private GetAllergiesByIdRepository getAllergiesByIdRepository;

    private UpdateUserProfileUseCase updateUserProfileUseCase;

    @BeforeEach
    void setUp() {
        updateUserProfileUseCase = new UpdateUserProfileUseCase(
                updateUserRepository,
                getPlanByIdRepository,
                getDietByIdRepository,
                getCookLevelByIdRepository,
                getDietaryNeedsByIdRepository,
                getAllergiesByIdRepository
        );
    }

    @Test
    void shouldUpdateUserProfileSuccessfully() {
        // Given
        String userEmail = "test@example.com";
        String userName = "Updated Name";
        
        UpdateUserProfileCommand.Command command = UpdateUserProfileCommand.Command.builder()
                .userEmail(userEmail)
                .name(userName)
                .planId(1)
                .cookLevelId(1)
                .dietId(1)
                .dietaryNeeds(List.of(1, 2))
                .allergies(List.of(1))
                .build();

        // Mock repository responses
        Plan mockPlan = Plan.builder().id(1).description("Premium").build();
        CookLevel mockCookLevel = CookLevel.builder().id(1).description("Beginner").build();
        Diet mockDiet = Diet.builder().id(1).description("Vegetarian").build();
        List<DietaryNeed> mockDietaryNeeds = List.of(
                DietaryNeed.builder().id(1).description("Low Sodium").build(),
                DietaryNeed.builder().id(2).description("High Protein").build()
        );
        List<Allergy> mockAllergies = List.of(
                Allergy.builder().id(1).description("Nuts").build()
        );

        when(getPlanByIdRepository.execute(1)).thenReturn(mockPlan);
        when(getCookLevelByIdRepository.execute(1)).thenReturn(mockCookLevel);
        when(getDietByIdRepository.execute(1)).thenReturn(mockDiet);
        when(getDietaryNeedsByIdRepository.execute(List.of(1, 2))).thenReturn(mockDietaryNeeds);
        when(getAllergiesByIdRepository.execute(List.of(1))).thenReturn(mockAllergies);

        User expectedUser = UserFactory.create();
        expectedUser.setEmail(userEmail);
        expectedUser.setName(userName);
        when(updateUserRepository.execute(any(User.class))).thenReturn(expectedUser);

        // When
        User result = updateUserProfileUseCase.execute(command);

        // Then
        assertNotNull(result);
        assertEquals(userEmail, result.getEmail());
        assertEquals(userName, result.getName());
        
        verify(updateUserRepository, times(1)).execute(any(User.class));
        verify(getPlanByIdRepository, times(1)).execute(1);
        verify(getCookLevelByIdRepository, times(1)).execute(1);
        verify(getDietByIdRepository, times(1)).execute(1);
        verify(getDietaryNeedsByIdRepository, times(1)).execute(List.of(1, 2));
        verify(getAllergiesByIdRepository, times(1)).execute(List.of(1));
    }

    @Test
    void shouldPassCorrectUserDataToRepository() {
        // Given
        String userEmail = "test@example.com";
        String userName = "Test User";
        
        UpdateUserProfileCommand.Command command = UpdateUserProfileCommand.Command.builder()
                .userEmail(userEmail)
                .name(userName)
                .build();

        User expectedUser = UserFactory.create();
        when(updateUserRepository.execute(any(User.class))).thenReturn(expectedUser);

        // When
        updateUserProfileUseCase.execute(command);

        // Then
        verify(updateUserRepository).execute(argThat(user -> 
            user.getEmail().equals(userEmail) && 
            user.getName().equals(userName)
        ));
    }

    @Test
    void shouldMapAllFieldsFromCommandToUser() {
        // Given
        String userEmail = "test@example.com";
        String userName = "Test User";
        Integer planId = 2;
        Integer cookLevelId = 3;
        Integer dietId = 1;
        List<Integer> dietaryNeeds = List.of(1, 2, 3);
        List<Integer> allergies = List.of(4, 5);
        
        UpdateUserProfileCommand.Command command = UpdateUserProfileCommand.Command.builder()
                .userEmail(userEmail)
                .name(userName)
                .planId(planId)
                .cookLevelId(cookLevelId)
                .dietId(dietId)
                .dietaryNeeds(dietaryNeeds)
                .allergies(allergies)
                .build();

        // Mock all repository responses
        when(getPlanByIdRepository.execute(planId)).thenReturn(Plan.builder().id(planId).build());
        when(getCookLevelByIdRepository.execute(cookLevelId)).thenReturn(CookLevel.builder().id(cookLevelId).build());
        when(getDietByIdRepository.execute(dietId)).thenReturn(Diet.builder().id(dietId).build());
        when(getDietaryNeedsByIdRepository.execute(dietaryNeeds)).thenReturn(List.of(
                DietaryNeed.builder().id(1).build(),
                DietaryNeed.builder().id(2).build(),
                DietaryNeed.builder().id(3).build()
        ));
        when(getAllergiesByIdRepository.execute(allergies)).thenReturn(List.of(
                Allergy.builder().id(4).build(),
                Allergy.builder().id(5).build()
        ));

        User expectedUser = UserFactory.create();
        when(updateUserRepository.execute(any(User.class))).thenReturn(expectedUser);

        // When
        updateUserProfileUseCase.execute(command);

        // Then
        verify(updateUserRepository).execute(argThat(user -> 
            user.getEmail().equals(userEmail) && 
            user.getName().equals(userName) &&
            user.getPlan() != null && user.getPlan().getId().equals(planId) &&
            user.getPreferences() != null && 
            user.getPreferences().getCookLevel() != null && 
            user.getPreferences().getCookLevel().getId().equals(cookLevelId) &&
            user.getPreferences().getDiet() != null && 
            user.getPreferences().getDiet().getId().equals(dietId) &&
            user.getDietaryNeeds() != null && user.getDietaryNeeds().size() == 3 &&
            user.getAllergies() != null && user.getAllergies().size() == 2
        ));
    }

    @Test
    void shouldHandleNullFieldsInCommand() {
        // Given
        String userEmail = "test@example.com";
        
        UpdateUserProfileCommand.Command command = UpdateUserProfileCommand.Command.builder()
                .userEmail(userEmail)
                .name(null)
                .planId(null)
                .cookLevelId(null)
                .dietId(null)
                .dietaryNeeds(null)
                .allergies(null)
                .build();

        User expectedUser = UserFactory.create();
        when(updateUserRepository.execute(any(User.class))).thenReturn(expectedUser);

        // When
        User result = updateUserProfileUseCase.execute(command);

        // Then
        assertNotNull(result);
        verify(updateUserRepository, times(1)).execute(any(User.class));
        verify(getPlanByIdRepository, never()).execute(anyInt());
        verify(getCookLevelByIdRepository, never()).execute(anyInt());
        verify(getDietByIdRepository, never()).execute(anyInt());
        verify(getDietaryNeedsByIdRepository, never()).execute(anyList());
        verify(getAllergiesByIdRepository, never()).execute(anyList());
    }
} 