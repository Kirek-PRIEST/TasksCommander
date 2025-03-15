package Max.Khasanov.TasksCommander.repository;

import Max.Khasanov.TasksCommander.entities.Task;
import Max.Khasanov.TasksCommander.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Интерфейс {@code TaskRepository} - репозиторий, который предоставляет методы для работы с задачами (Task) в базе данных.
 *
 */

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);

    /**
     * Метод для отображения списка задач выбранного пользователя и даёт возможность фильтровать список задач
     * @param user Объект пользователя, для которого нужно найти задачи
     * @param status Статус задачи (true — выполнена, false — не выполнена)
     * @param sort Объект Sort, определяющий порядок сортировки.
     * @return Отсортированный список задач, отфильтрованных по статусу
     */
    @Query("SELECT t FROM Task t WHERE t.user = :user AND " +
            "(:status IS NULL OR t.completed = :status)")
    List<Task> findByUserAndStatus(
            @Param("user") User user,
            @Param("status") Boolean status,
            Sort sort
    );

    /**
     * Перегруженный метод для отображения списка задач выбранного пользователя и даёт возможность фильтровать список задач
     * @param user Объект пользователя, для которого нужно найти задачи.
     * @param status Статус задачи (true — выполнена, false — не выполнена).
     * @param sortBy выбор порядка сортировки.
     * @param direction направление сортировки (возрастание/убывание)
     * @return Отсортированный список задач, отфильтрованных по статусу
     */
    default List<Task> findByUserAndStatus(User user, Boolean status, String sortBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        return findByUserAndStatus(user, status, sort);
    }
}