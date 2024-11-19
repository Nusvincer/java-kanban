package test.model;

import com.yandex.app.model.Task;
import com.yandex.app.service.InMemoryTaskManager;
import com.yandex.app.util.Status;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskIntersectionTest {

    @Test
    public void testNoIntersection() {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        task1.setStartTime(LocalDateTime.of(2024, 11, 18, 10, 0));
        task1.setDuration(Duration.ofMinutes(120));

        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        task2.setStartTime(LocalDateTime.of(2024, 11, 18, 13, 0));
        task2.setDuration(Duration.ofMinutes(60));

        manager.addTask(task1);
        assertDoesNotThrow(() -> manager.addTask(task2), "Задачи не должны пересекаться");
    }

    @Test
    public void testIntersection() {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        task1.setStartTime(LocalDateTime.of(2024, 11, 18, 10, 0));
        task1.setDuration(Duration.ofMinutes(120));

        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        task2.setStartTime(LocalDateTime.of(2024, 11, 18, 11, 0));
        task2.setDuration(Duration.ofMinutes(60));

        manager.addTask(task1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> manager.addTask(task2));
        assertEquals("Задача пересекается с другой задачей.", exception.getMessage());
    }
}
