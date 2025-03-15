package Max.Khasanov.TasksCommander.service;

import Max.Khasanov.TasksCommander.exepions.DuplicateEmailException;
import Max.Khasanov.TasksCommander.exepions.DuplicateUsernameException;
import Max.Khasanov.TasksCommander.entities.User;
import Max.Khasanov.TasksCommander.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Класс {@code UserService} - сервисный слой приложения, отвечающий за бизнес-логику, связанную с пользователями (User).
 * Он предоставляет методы для создания, чтения, обновления и удаления пользователей,
 * а также для проверки уникальности имени пользователя и email.
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     *
     * @return возвращает список всех пользователей
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Создаёт нового пользователя. Использует методы репозитория {@code UserRepository}:
     * {@code existsByUsername} для проверки занятости имени пользователя
     * {@code existsByEmail} для проверки занятости email пользователя
     * {@code save} для сохранения пользователя в базе данных
     * @param user
     * @return объект нового пользователя
     */
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateUsernameException("Пользователь с таким именем уже существует");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateEmailException("Адрес электронной почты занят другим пользователем");
        }

        return userRepository.save(user);
    }

    /**
     * Удаляет пользователя по ID. Использует методы репозитория {@code UserRepository} {@code deleteById}
     * @param id ID пользователя
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Возвращает пользователя по его ID. Если пользователь не найден возвращает Optional.empty().
     * Использует методы репозитория {@code UserRepository} {@code deleteById}
     * @param id
     * @return Объект Optional, содержащий пользователя, если он найден.
     * Если пользователь не найден, возвращается Optional.empty()
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Метод обновляет информацию о пользователе. Использует методы репозитория {@code UserRepository}:
     * {@code deleteById} для поиска пользователя о ID
     * {@code existsByUsername} - для проверки занятости имени пользователя
     * {@code existsByEmail} - для проверки занятости email пользователя
     * {@code save} для сохранения пользователя в базе данных
     * @param id - ID пользователя
     * @param updatedUser - изменённый объект пользователя
     * @return Обновленный объект пользователя с актуальными данными
     */
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    if (!user.getUsername().equals(updatedUser.getUsername())
                            && userRepository.existsByUsername(updatedUser.getUsername())) {
                        throw new DuplicateUsernameException("Пользователь с таким именем уже существует");
                    }

                    if (!user.getEmail().equals(updatedUser.getEmail())
                            && userRepository.existsByEmail(updatedUser.getEmail())) {
                        throw new DuplicateEmailException("Адрес электронной почты занят другим пользователем");
                    }

                    user.setUsername(updatedUser.getUsername());
                    user.setEmail(updatedUser.getEmail());
                    user.setPassword(updatedUser.getPassword());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }
}