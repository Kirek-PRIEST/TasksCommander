package Max.Khasanov.TasksCommander.service;

import Max.Khasanov.TasksCommander.entities.Task;
import Max.Khasanov.TasksCommander.entities.User;
import Max.Khasanov.TasksCommander.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasksByUser(User user, String statusFilter, String sortBy, String direction) {
        Boolean status = parseStatusFilter(statusFilter);
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        return taskRepository.findByUserAndStatus(user, status, sort);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public void completeTask(Long taskId) {
        taskRepository.findById(taskId).ifPresent(task -> {
            task.setCompleted(true);
            taskRepository.save(task);
        });
    }
    private Boolean parseStatusFilter(String statusFilter) {
        if (statusFilter == null) return null;
        return switch (statusFilter.toLowerCase()) {
            case "completed" -> true;
            case "active" -> false;
            default -> null;
        };
    }
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
    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

}