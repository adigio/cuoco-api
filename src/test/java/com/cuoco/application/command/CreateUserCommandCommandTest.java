package com.cuoco.application.command;

import com.cuoco.application.port.in.CreateUserCommand.Command;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CreateUserCommand.Command - Unit Tests")
class CreateUserCommandCommandTest {

    @Test
    @DisplayName("Command should be created with valid data")
    void test_commandCreatedWithValidData() {
        Command command = new Command(
                "Juan",
                "juan@mail.com",
                "securePass",
                LocalDate.now(),
                "premium",
                true,
                "advanced",
                "vegan",
                List.of("gluten-free"),
                List.of("peanuts")
        );

        assertNotNull(command);
        assertEquals("Juan", command.getName());
        assertEquals("premium", command.getPlan());
        assertTrue(command.getIsValid());
    }

    @Test
    @DisplayName("Command should accept empty allergies and dietaryNeeds")
    void test_commandWithEmptyLists() {
        Command command = new Command(
                "Ana",
                "ana@mail.com",
                "1234",
                LocalDate.now(),
                "basic",
                false,
                "beginner",
                "omnivore",
                List.of(),
                List.of()
        );

        assertNotNull(command);
        assertTrue(command.getDietaryNeeds().isEmpty());
        assertTrue(command.getAllergies().isEmpty());
    }

    @Test
    @DisplayName("Command with null fields should still construct")
    void test_commandWithNullFields() {
        Command command = new Command(
                null, null, null, null,
                null, null, null, null,
                null, null
        );

        assertNotNull(command);
        assertNull(command.getName());
        assertNull(command.getDiet());
    }

    @Test
    @DisplayName("Command should correctly store a list of dietary needs")
    void test_commandWithDietaryNeedsList() {
        List<String> needs = List.of("low-carb", "gluten-free");
        Command command = new Command(
                "Lucia",
                "lucia@example.com",
                "luciaPass",
                LocalDate.of(2023, 5, 10),
                "pro",
                true,
                "intermediate",
                "vegetarian",
                needs,
                List.of()
        );

        assertEquals(2, command.getDietaryNeeds().size());
        assertTrue(command.getDietaryNeeds().contains("low-carb"));
    }

    @Test
    @DisplayName("Command with future registration date")
    void test_commandWithFutureRegisterDate() {
        LocalDate futureDate = LocalDate.now().plusDays(5);
        Command command = new Command(
                "FutureUser",
                "future@example.com",
                "future123",
                futureDate,
                "test-plan",
                true,
                "expert",
                "keto",
                List.of(),
                List.of()
        );

        assertEquals(futureDate, command.getRegisterDate());
        assertTrue(command.getRegisterDate().isAfter(LocalDate.now()));
    }
}
