package Max.Khasanov.TasksCommander.repository;

import Max.Khasanov.TasksCommander.entities.Task;
import Max.Khasanov.TasksCommander.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
    @Query("SELECT t FROM Task t WHERE t.user = :user AND " +
            "(:status IS NULL OR t.completed = :status)")
    List<Task> findByUserAndStatus(
            @Param("user") User user,
            @Param("status") Boolean status,
            Sort sort
    );

    default List<Task> findByUserAndStatus(User user, Boolean status, String sortBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        return findByUserAndStatus(user, status, sort);
    }
}