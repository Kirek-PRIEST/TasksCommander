package Max.Khasanov.TasksCommander.controller;

import Max.Khasanov.TasksCommander.exepions.DuplicateEmailException;
import Max.Khasanov.TasksCommander.exepions.DuplicateUsernameException;
import Max.Khasanov.TasksCommander.entities.User;
import Max.Khasanov.TasksCommander.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller

public class UserController {
    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Отображает главную страницу со списком всех пользователей
     * @param model Модель для передачи данных в представление
     * @return Имя шаблона index.html
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", new User()); // Для формы добавления
        return "index";
    }

    /**
     * Обрабатывает создание нового пользователя
     * @param user Данные пользователя из формы
     * @param result Результаты валидации
     * @param model Модель для передачи ошибок
     * @return Перенаправляет на главную страницу
     */
    @PostMapping("/")
    public String createUser(@ModelAttribute @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.getAllUsers());
            return "index";
        }
        try {
            userService.createUser(user);
            return "redirect:/";
        } catch (DuplicateUsernameException | DuplicateEmailException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("users", userService.getAllUsers());
            return "index";
        }
    }

    /**
     * Удаляет пользователя по ID
     * @param id ID пользователя для удаления
     * @return Редирект на главную страницу
     */
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/";
    }

    /**
     * Отображает форму редактирования пользователя
     * @param id ID редактируемого пользователя
     * @param model Модель для передачи данных пользователя
     * @return Имя шаблона edit-user.html
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        userService.getUserById(id).ifPresent(user -> {
            model.addAttribute("user", user);
            model.addAttribute("isEdit", true);
        });
        return "edit-user";
    }

    /**
     * Метод для обновления данных пользователя
     * @param id ID пользователя для обновления
     * @param user Обновленные данные пользователя
     * @param result Результаты валидации
     * @param model Модель для передачи ошибок
     * @return Перенаправление на главную страницу
     */
    @PostMapping("/edit/{id}")
    public String updateUser(
            @PathVariable Long id,
            @ModelAttribute @Valid User user,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "edit-user";
        }

        try {
            userService.updateUser(id, user);
            return "redirect:/";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("isEdit", true);
            return "edit-user";
        }
    }
}