package com.cuoco.application.usecase;

import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.in.CreateUserCommand;
import com.cuoco.application.port.out.CreateUserRepository;
import com.cuoco.application.port.out.GetAllergiesByIdRepository;
import com.cuoco.application.port.out.GetCookLevelByIdRepository;
import com.cuoco.application.port.out.GetDietByIdRepository;
import com.cuoco.application.port.out.GetDietaryNeedsByIdRepository;
import com.cuoco.application.port.out.GetPlanByIdRepository;
import com.cuoco.application.port.out.UserExistsByEmailRepository;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.User;
import com.cuoco.factory.domain.UserFactory;
import com.cuoco.shared.model.ErrorDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateUserUseCaseTest {

    private PasswordEncoder passwordEncoder;
    private CreateUserRepository createUserRepository;
    private UserExistsByEmailRepository userExistsByEmailRepository;
    private GetPlanByIdRepository getPlanByIdRepository;
    private GetDietByIdRepository getDietByIdRepository;
    private GetCookLevelByIdRepository getCookLevelByIdRepository;
    private GetDietaryNeedsByIdRepository getDietaryNeedsByIdRepository;
    private GetAllergiesByIdRepository getAllergiesByIdRepository;
    private CreateUserUseCase useCase;

    @BeforeEach
    void setup() {
        passwordEncoder = mock(PasswordEncoder.class);
        createUserRepository = mock(CreateUserRepository.class);
        userExistsByEmailRepository = mock(UserExistsByEmailRepository.class);
        getPlanByIdRepository = mock(GetPlanByIdRepository.class);
        getDietByIdRepository = mock(GetDietByIdRepository.class);
        getCookLevelByIdRepository = mock(GetCookLevelByIdRepository.class);
        getDietaryNeedsByIdRepository = mock(GetDietaryNeedsByIdRepository.class);
        getAllergiesByIdRepository = mock(GetAllergiesByIdRepository.class);

        useCase = new CreateUserUseCase(
                passwordEncoder,
                createUserRepository,
                userExistsByEmailRepository,
                getPlanByIdRepository,
                getDietByIdRepository,
                getCookLevelByIdRepository,
                getDietaryNeedsByIdRepository,
                getAllergiesByIdRepository
        );
    }

    @Test
    void GIVEN_valid_command_WHEN_execute_THEN_return_created_user() {
        var user = UserFactory.create();
        var plan = user.getPlan();
        var diet = user.getPreferences().getDiet();
        var cookLevel = user.getPreferences().getCookLevel();
        var dietaryNeedsIds = user.getDietaryNeeds().stream().map(DietaryNeed::getId).toList();
        var allergiesIds = user.getAllergies().stream().map(Allergy::getId).toList();

        var command = CreateUserCommand.Command.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .planId(user.getPlan().getId())
                .cookLevelId(user.getPreferences().getCookLevel().getId())
                .dietId(user.getPreferences().getDiet().getId())
                .dietaryNeeds(dietaryNeedsIds)
                .allergies(allergiesIds)
                .build();

        when(userExistsByEmailRepository.execute(command.getEmail())).thenReturn(false);
        when(getPlanByIdRepository.execute(command.getPlanId())).thenReturn(plan);
        when(getDietByIdRepository.execute(command.getDietId())).thenReturn(diet);
        when(getCookLevelByIdRepository.execute(command.getCookLevelId())).thenReturn(cookLevel);
        when(getDietaryNeedsByIdRepository.execute(command.getDietaryNeeds())).thenReturn(user.getDietaryNeeds());
        when(getAllergiesByIdRepository.execute(command.getAllergies())).thenReturn(user.getAllergies());
        when(passwordEncoder.encode(command.getPassword())).thenReturn("encrypted");
        when(createUserRepository.execute(any())).thenReturn(user);

        User result = useCase.execute(command);

        assertNotNull(result);
        assertNull(result.getPassword());
    }

    @Test
    void GIVEN_existing_email_WHEN_execute_THEN_throw_bad_request() {
        var command = CreateUserCommand.Command.builder()
                .email("existing@email.com")
                .build();

        when(userExistsByEmailRepository.execute(command.getEmail())).thenReturn(true);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
        assertEquals(ErrorDescription.USER_DUPLICATED.getValue(), ex.getDescription());
    }

    @Test
    void GIVEN_invalid_plan_id_WHEN_execute_THEN_throw_bad_request() {
        var command = CreateUserCommand.Command.builder().email("existing@email.com").planId(1).build();

        when(userExistsByEmailRepository.execute(command.getEmail())).thenReturn(false);
        when(getPlanByIdRepository.execute(command.getPlanId())).thenReturn(null);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
        assertEquals(ErrorDescription.PLAN_NOT_EXISTS.getValue(), ex.getDescription());
    }

    @Test
    void GIVEN_invalid_diet_id_WHEN_execute_THEN_throw_bad_request() {
        User user = UserFactory.create();
        var command = CreateUserCommand.Command.builder().email("existing@email.com").planId(1).dietId(1).build();

        when(userExistsByEmailRepository.execute(command.getEmail())).thenReturn(false);
        when(getPlanByIdRepository.execute(command.getPlanId())).thenReturn(user.getPlan());
        when(getCookLevelByIdRepository.execute(command.getCookLevelId())).thenReturn(user.getPreferences().getCookLevel());
        when(getDietByIdRepository.execute(command.getDietId())).thenReturn(null);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
        assertEquals(ErrorDescription.DIET_NOT_EXISTS.getValue(), ex.getDescription());
    }

    @Test
    void GIVEN_invalid_cook_level_id_WHEN_execute_THEN_throw_bad_request() {
        User user = UserFactory.create();
        var command = CreateUserCommand.Command.builder()
                .email("existing@email.com")
                .planId(1)
                .dietId(1)
                .cookLevelId(1)
                .build();

        when(userExistsByEmailRepository.execute(command.getEmail())).thenReturn(false);
        when(getPlanByIdRepository.execute(command.getPlanId())).thenReturn(user.getPlan());
        when(getCookLevelByIdRepository.execute(command.getCookLevelId())).thenReturn(null);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
        assertEquals(ErrorDescription.COOK_LEVEL_NOT_EXISTS.getValue(), ex.getDescription());
    }

    @Test
    void GIVEN_invalid_dietary_needs_WHEN_execute_THEN_throw_bad_request() {
        User user = UserFactory.create();
        var command = CreateUserCommand.Command.builder()
                .email("existing@email.com")
                .planId(1)
                .dietId(1)
                .cookLevelId(1)
                .dietaryNeeds(List.of(1,2))
                .build();

        when(userExistsByEmailRepository.execute(command.getEmail())).thenReturn(false);
        when(getPlanByIdRepository.execute(command.getPlanId())).thenReturn(user.getPlan());
        when(getDietByIdRepository.execute(command.getDietId())).thenReturn(user.getPreferences().getDiet());
        when(getCookLevelByIdRepository.execute(command.getCookLevelId())).thenReturn(user.getPreferences().getCookLevel());
        when(getDietaryNeedsByIdRepository.execute(command.getDietaryNeeds())).thenReturn(Collections.emptyList());

        BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
        assertEquals(ErrorDescription.DIETARY_NEEDS_NOT_EXISTS.getValue(), ex.getDescription());
    }

    @Test
    void GIVEN_invalid_allergies_WHEN_execute_THEN_throw_bad_request() {
        User user = UserFactory.create();
        var command = CreateUserCommand.Command.builder()
                .email("existing@email.com")
                .planId(1)
                .dietId(1)
                .cookLevelId(1)
                .dietaryNeeds(List.of(1,2,3))
                .allergies(List.of(1,2,3))
                .build();

        when(userExistsByEmailRepository.execute(command.getEmail())).thenReturn(false);
        when(getPlanByIdRepository.execute(command.getPlanId())).thenReturn(user.getPlan());
        when(getDietByIdRepository.execute(command.getDietId())).thenReturn(user.getPreferences().getDiet());
        when(getCookLevelByIdRepository.execute(command.getCookLevelId())).thenReturn(user.getPreferences().getCookLevel());
        when(getDietaryNeedsByIdRepository.execute(command.getDietaryNeeds())).thenReturn(user.getDietaryNeeds());
        when(getAllergiesByIdRepository.execute(command.getAllergies())).thenReturn(Collections.emptyList());

        BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
        assertEquals(ErrorDescription.ALLERGIES_NOT_EXISTS.getValue(), ex.getDescription());
    }
}