package model;

import com.yandex.app.model.Task;
import com.yandex.app.service.InMemoryTaskManager;
import com.yandex.app.util.Status;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskPriorityTest {

    @Test
    public void testPrioritizedTasks() {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        task1.setStartTime(LocalDateTime.of(2024, 11, 18, 11, 0));
        task1.setDuration(Duration.ofMinutes(60));

        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        task2.setStartTime(LocalDateTime.of(2024, 11, 18, 10, 0));
        task2.setDuration(Duration.ofMinutes(30));

        Task task3 = new Task("Task 3", "Description 3", Status.NEW);

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        List<Task> prioritizedTasks = manager.getPrioritizedTasks();
        assertEquals(task2, prioritizedTasks.get(0), "Первая задача должна быть с наименьшим временем начала");
        assertEquals(task1, prioritizedTasks.get(1), "Вторая задача должна быть следующей по времени начала");
        assertFalse(prioritizedTasks.contains(task3), "Задача без времени начала не должна попадать в отсортированный список");
    }

    @Test
    public void testAddTaskWithNullStartTime() {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task task = new Task("Task with no startTime", "Description", Status.NEW);

        manager.addTask(task);

        assertTrue(manager.getPrioritizedTasks().isEmpty(), "Задача с null startTime не должна быть в prioritizedTasks.");
    }
}
