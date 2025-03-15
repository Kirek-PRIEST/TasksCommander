package Max.Khasanov.TasksCommander.repository;

import Max.Khasanov.TasksCommander.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * {@code UserRepository} Интерфейс для работы с пользователями (User) в базе данных.
 */
    public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Проверяет, существует ли пользователь с указанным именем (username)
     * @param username Имя пользователя для проверки.
     * @return {@code true} - пользователь с таким именем существует,
     * {@code false} - пользователя с таким именем не существует
     */
    boolean existsByUsername(String username);
    /**
     * Проверяет, существует ли пользователь с указанным именем (username)
     * @param email Электронная почта пользователя для проверки.
     * @return {@code true} - пользователь с таким email существует,
     * {@code false} - пользователя с таким email не существует
     */
    boolean existsByEmail(String email);
}