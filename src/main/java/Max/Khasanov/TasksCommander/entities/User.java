package Max.Khasanov.TasksCommander.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;


    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();

    //region Конструкторы
    public User() {
    }

    public User(Long id, String username, String email, String password,
                LocalDateTime createdAt, List<Task> tasks) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.tasks = tasks;
    }


    public User(String todelete, String mail, String pass) {
    }
    //endregion

    //region Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    //endregion


    public static UserBuilder builder() {
        return new UserBuilder();
    }

    /**
     * Вложенный класс для удобного создания объектов Task
     */
    public static class UserBuilder {
        private Long id;
        private String username;
        private String email;
        private String password;
        private LocalDateTime createdAt;
        private List<Task> tasks;

        // Устанавливает ID пользователя
        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }
        // Устанавливает имя пользователя
        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        //Устанавливает почту пользователя
        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        //устанавливает пароль пользователя
        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        // Устанавливает дату создания пользователя
        public UserBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        // Устанавливает список дадач пользоватедя
        public UserBuilder tasks(List<Task> tasks) {
            this.tasks = tasks;
            return this;
        }
        //создаёт объект User с установленными параметрами.
        public User build() {
            return new User(id, username, email, password, createdAt, tasks);
        }
    }
}