package Max.Khasanov.TasksCommander.controller;

import Max.Khasanov.TasksCommander.exepions.TaskNotFoundException;
import Max.Khasanov.TasksCommander.exepions.UserNotFoundException;
import Max.Khasanov.TasksCommander.entities.Task;
import Max.Khasanov.TasksCommander.entities.User;
import Max.Khasanov.TasksCommander.service.TaskService;
import Max.Khasanov.TasksCommander.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users/{userId}/tasks")
public class TaskController {
    private final UserService userService;
    private final TaskService taskService;

    @Autowired
    public TaskController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    // Исправленный метод
    @GetMapping // Был: @GetMapping("/users/{userId}/tasks")
    public String getUserTasks(
            @PathVariable Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String direction,
            Model model
    ) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Task> tasks = taskService.getTasksByUser(
                user,
                status,
                sort != null ? sort : "createdAt",
                direction != null ? direction : "asc"
        );

        model.addAttribute("user", user);
        model.addAttribute("tasks", tasks);
        model.addAttribute("currentStatus", status);
        model.addAttribute("currentSort", sort);
        model.addAttribute("currentDirection", direction);

        return "user-tasks";
    }

    // Остальные методы остаются без изменений, но проверьте пути:
    @PostMapping
    public String createTask(@PathVariable Long userId, @ModelAttribute Task task) {
        userService.getUserById(userId).ifPresent(user -> {
            task.setUser(user);
            taskService.createTask(task);
        });
        return "redirect:/users/{userId}/tasks";
    }

    @PostMapping("/{taskId}/complete")
    public String completeTask(
            @PathVariable Long userId,
            @PathVariable Long taskId
    ) {
        taskService.completeTask(taskId);
        return "redirect:/users/{userId}/tasks";
    }

    @GetMapping("/{taskId}/edit")
    public String showEditTaskForm(
            @PathVariable Long userId,
            @PathVariable Long taskId,
            Model model
    ) {
        taskService.getTaskById(taskId).ifPresent(task -> {
            model.addAttribute("task", task);
            model.addAttribute("users", userService.getAllUsers());
        });

        if (!model.containsAttribute("task")) {
            throw new TaskNotFoundException("Task not found");
        }

        return "edit-task";
    }

    @PostMapping("/{taskId}/edit")
    public String updateTask(
            @PathVariable Long userId,
            @PathVariable Long taskId,
            @ModelAttribute Task task
    ) {
        taskService.updateTask(taskId, task);
        return "redirect:/users/{userId}/tasks";
    }
    @PostMapping("/{taskId}/delete")
    public String deleteTask(
            @PathVariable Long userId,
            @PathVariable Long taskId
    ) {
        taskService.deleteTask(taskId);
        return "redirect:/users/{userId}/tasks";
    }
}