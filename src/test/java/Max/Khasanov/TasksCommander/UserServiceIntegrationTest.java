package Max.Khasanov.TasksCommander;

import Max.Khasanov.TasksCommander.entities.User;
import Max.Khasanov.TasksCommander.exepions.DuplicateEmailException;
import Max.Khasanov.TasksCommander.exepions.DuplicateUsernameException;
import Max.Khasanov.TasksCommander.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void createUser_ShouldPersistUser() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");

        // Act
        User savedUser = userService.createUser(user);

        // Assert
        assertNotNull(savedUser.getId());
        assertEquals("testuser", savedUser.getUsername());
    }

    @Test
    void createUser_ShouldThrowOnDuplicateUsername() {
        // Arrange
        User user1 = new User();
        user1.setUsername("duplicate");
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");
        userService.createUser(user1);

        User user2 = new User();
        user2.setUsername("duplicate");
        user2.setEmail("user2@example.com");
        user2.setPassword("password2");

        // Act & Assert
        assertThrows(DuplicateUsernameException.class, () -> {
            userService.createUser(user2);
        });
    }

    @Test
    void deleteUser_ShouldRemoveFromDatabase() {
        // Arrange
        User user = new User();
        user.setUsername("todelete");
        user.setEmail("delete@test.com");
        user.setPassword("pass");
        user = userService.createUser(user);

        // Act
        userService.deleteUser(user.getId());

        // Assert
        assertFalse(userService.getUserById(user.getId()).isPresent());
    }
}