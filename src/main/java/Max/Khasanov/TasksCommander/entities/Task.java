package Max.Khasanov.TasksCommander.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    private boolean completed;


    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //region Конструкторы
    public Task() {
    }

    public Task(Long id, String title, String description,
                LocalDateTime dueDate, boolean completed,
                LocalDateTime createdAt, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = completed;
        this.createdAt = createdAt;
        this.user = user;
    }
    //endregion

    // region Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //endregion

    public static TaskBuilder builder() {
        return new TaskBuilder();
    }


    public static class TaskBuilder {
        private Long id;
        private String title;
        private String description;
        private LocalDateTime dueDate;
        private boolean completed;
        private LocalDateTime createdAt;
        private User user;

        //Устанавливает идентификатор задачи
        public TaskBuilder id(Long id) {
            this.id = id;
            return this;
        }

        //Устанавливает название задачи
        public TaskBuilder title(String title) {
            this.title = title;
            return this;
        }

        //Устанавливает описание задачи.
        public TaskBuilder description(String description) {
            this.description = description;
            return this;
        }

        //Устанавливает срок выполнения задачи.
        public TaskBuilder dueDate(LocalDateTime dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        //Устанавливает статус выполнения задачи.
        public TaskBuilder completed(boolean completed) {
            this.completed = completed;
            return this;
        }

        // Устанавливает дату создания задачи.
        public TaskBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        //Устанавливает пользователя, связанного с задачей.
        public TaskBuilder user(User user) {
            this.user = user;
            return this;
        }

        //Создает объект Task с указанными параметрами
        public Task build() {
            return new Task(id, title, description, dueDate, completed, createdAt, user);
        }
    }
}