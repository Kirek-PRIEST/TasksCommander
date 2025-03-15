package Max.Khasanov.TasksCommander.service;

import Max.Khasanov.TasksCommander.entities.Task;
import Max.Khasanov.TasksCommander.entities.User;
import Max.Khasanov.TasksCommander.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Класс {@code TaskService} - сервисный слой приложения, отвечающим за бизнес-логику, связанную с задачами (Task).
 * Предоставляет методы для создания, чтения, обновления и удаления задач, а также для фильтрации и сортировки задач.
 */
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Список задач для указанного пользователя с фильтрацией по статусу и сортировкой.
     * @param user Объект пользователя, для которого нужно найти задачи
     * @param statusFilter Фильтр по статусу задачи (completed, active)
     * @param sortBy Поле для сортировки (например, createdAt, dueDate)
     * @param direction Направление сортировки (По возрастанию/убыванию)
     * @return {@code List<Task>} - список задач, отфильтрованных по статусу и отсортированных.
     */
    public List<Task> getTasksByUser(User user, String statusFilter, String sortBy, String direction) {
        Boolean status = parseStatusFilter(statusFilter);
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        return taskRepository.findByUserAndStatus(user, status, sort);
    }

    /**
     * Создает новую задачу и использует метод {@code save} репозитория для сохранения её в базе данных.
     * @param task Объект задачи для создания.
     * @return {@code Task} - созданную задачу
     */
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    /**
     * Отмечает Задачу, как "выполненная", используя метод {@code findById} репозитория.
     * @param taskId ID задачи для изменения
     */
    public void completeTask(Long taskId) {
        taskRepository.findById(taskId).ifPresent(task -> {
            task.setCompleted(true);
            taskRepository.save(task);
        });
    }

    /**
     * Преобразует строковый фильтр статуса в логическое значение.
     * @param statusFilter Строковое представление статуса
     * @return булевое представление статуса ({@code true} для выполненных задач, {@code false} для активных задач)
     */
    private Boolean parseStatusFilter(String statusFilter) {
        if (statusFilter == null) return null;
        return switch (statusFilter.toLowerCase()) {
            case "completed" -> true;
            case "active" -> false;
            default -> null;
        };
    }

    /**
     * Изменяет данные задачи, используя методы репозитория
     * {@code findById} - для поиска задачи по идентификатору и
     * {@code save} - для сохранения обновленной задачи.
     * @param taskId ID изменяемой задачи
     * @param updatedTask Объект изменённой задачи
     * @return обновлённая задача
     */
    public Task updateTask(Long taskId, Task updatedTask) {
        return taskRepository.findById(taskId)
                .map(task -> {
                    task.setTitle(updatedTask.getTitle());
                    task.setDescription(updatedTask.getDescription());
                    task.setDueDate(updatedTask.getDueDate());
                    task.setCompleted(updatedTask.isCompleted());
                    task.setUser(updatedTask.getUser());
                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    /**
     * Возвращает задачу по её ID, используя метод репозитория {@code findById} для поиска задачи
     * @param taskId
     * @return
     */
    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    /**
     * Удаляет задачу, используя метод репозитория {@code deleteById}.
     * @param taskId ID задачи
     */
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

}