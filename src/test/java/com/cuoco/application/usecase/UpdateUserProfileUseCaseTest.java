package com.cuoco.application.usecase;

import com.cuoco.application.port.in.UpdateUserProfileCommand;
import com.cuoco.application.port.out.GetAllergiesByIdRepository;
import com.cuoco.application.port.out.GetCookLevelByIdRepository;
import com.cuoco.application.port.out.GetDietByIdRepository;
import com.cuoco.application.port.out.GetDietaryNeedsByIdRepository;
import com.cuoco.application.port.out.GetUserByIdRepository;
import com.cuoco.application.port.out.UpdateUserRepository;
import com.cuoco.application.usecase.domainservice.UserDomainService;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.Diet;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.User;
import com.cuoco.factory.domain.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserProfileUseCaseTest {

    @Mock
    private UserDomainService userDomainService;
    @Mock
    private GetUserByIdRepository getUserByIdRepository;
    @Mock
    private UpdateUserRepository updateUserRepository;
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
                userDomainService,
                getUserByIdRepository,
                updateUserRepository,
                getDietByIdRepository,
                getCookLevelByIdRepository,
                getDietaryNeedsByIdRepository,
                getAllergiesByIdRepository
        );
    }

    @Test
    void shouldUpdateUserProfileSuccessfully() {
        String userName = "Updated Name";
        User currentUser = UserFactory.create();
        User existingUser = UserFactory.create();
        
        UpdateUserProfileCommand.Command command = UpdateUserProfileCommand.Command.builder()
                .name(userName)
                .planId(1)
                .cookLevelId(1)
                .dietId(1)
                .dietaryNeeds(List.of(1, 2))
                .allergies(List.of(1))
                .build();

        CookLevel mockCookLevel = CookLevel.builder().id(1).description("Beginner").build();
        Diet mockDiet = Diet.builder().id(1).description("Vegetarian").build();
        List<DietaryNeed> mockDietaryNeeds = List.of(
                DietaryNeed.builder().id(1).description("Low Sodium").build(),
                DietaryNeed.builder().id(2).description("High Protein").build()
        );
        List<Allergy> mockAllergies = List.of(
                Allergy.builder().id(1).description("Nuts").build()
        );

        when(userDomainService.getCurrentUser()).thenReturn(currentUser);
        when(getUserByIdRepository.execute(currentUser.getId())).thenReturn(existingUser);
        when(getCookLevelByIdRepository.execute(1)).thenReturn(mockCookLevel);
        when(getDietByIdRepository.execute(1)).thenReturn(mockDiet);
        when(getDietaryNeedsByIdRepository.execute(List.of(1, 2))).thenReturn(mockDietaryNeeds);
        when(getAllergiesByIdRepository.execute(List.of(1))).thenReturn(mockAllergies);

        User expectedUser = UserFactory.create();
        expectedUser.setName(userName);
        when(updateUserRepository.execute(any(User.class))).thenReturn(expectedUser);

        User result = updateUserProfileUseCase.execute(command);

        assertNotNull(result);
        assertEquals(userName, result.getName());
        
        verify(updateUserRepository, times(1)).execute(any(User.class));
        verify(getCookLevelByIdRepository, times(1)).execute(1);
        verify(getDietByIdRepository, times(1)).execute(1);
        verify(getDietaryNeedsByIdRepository, times(1)).execute(List.of(1, 2));
        verify(getAllergiesByIdRepository, times(1)).execute(List.of(1));
    }

    @Test
    void shouldPassCorrectUserDataToRepository() {
        String userName = "Test User";
        User currentUser = UserFactory.create();
        User existingUser = UserFactory.create();
        
        UpdateUserProfileCommand.Command command = UpdateUserProfileCommand.Command.builder()
                .name(userName)
                .build();

        User expectedUser = UserFactory.create();
        when(userDomainService.getCurrentUser()).thenReturn(currentUser);
        when(getUserByIdRepository.execute(currentUser.getId())).thenReturn(existingUser);
        when(updateUserRepository.execute(any(User.class))).thenReturn(expectedUser);

        updateUserProfileUseCase.execute(command);

        verify(updateUserRepository).execute(argThat(user -> 
            user.getName().equals(userName)
        ));
    }

    @Test
    void shouldMapAllFieldsFromCommandToUser() {
        String userName = "Test User";
        Integer cookLevelId = 3;
        Integer dietId = 1;
        List<Integer> dietaryNeeds = List.of(1, 2, 3);
        List<Integer> allergies = List.of(4, 5);
        
        User currentUser = UserFactory.create();
        User existingUser = UserFactory.create();
        
        UpdateUserProfileCommand.Command command = UpdateUserProfileCommand.Command.builder()
                .name(userName)
                .cookLevelId(cookLevelId)
                .dietId(dietId)
                .dietaryNeeds(dietaryNeeds)
                .allergies(allergies)
                .build();

        when(userDomainService.getCurrentUser()).thenReturn(currentUser);
        when(getUserByIdRepository.execute(currentUser.getId())).thenReturn(existingUser);
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
        
        updateUserProfileUseCase.execute(command);

        verify(updateUserRepository).execute(argThat(user -> 
            user.getName().equals(userName) &&
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
        User currentUser = UserFactory.create();
        User existingUser = UserFactory.create();
        
        UpdateUserProfileCommand.Command command = UpdateUserProfileCommand.Command.builder()
                .name(null)
                .planId(null)
                .cookLevelId(null)
                .dietId(null)
                .dietaryNeeds(null)
                .allergies(null)
                .build();

        User expectedUser = UserFactory.create();
        when(userDomainService.getCurrentUser()).thenReturn(currentUser);
        when(getUserByIdRepository.execute(currentUser.getId())).thenReturn(existingUser);
        when(updateUserRepository.execute(any(User.class))).thenReturn(expectedUser);

        User result = updateUserProfileUseCase.execute(command);

        assertNotNull(result);
        verify(updateUserRepository, times(1)).execute(any(User.class));
        verify(getCookLevelByIdRepository, never()).execute(anyInt());
        verify(getDietByIdRepository, never()).execute(anyInt());
        verify(getDietaryNeedsByIdRepository, never()).execute(anyList());
        verify(getAllergiesByIdRepository, never()).execute(anyList());
    }
} 