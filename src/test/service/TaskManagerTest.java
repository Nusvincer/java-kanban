package test.service;

import com.yandex.app.model.Task;
import com.yandex.app.service.TaskManager;
import com.yandex.app.util.Managers;
import com.yandex.app.util.Status;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {

    @Test
    public void testAddAndGetTask() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Test Task", "Test Description", Status.NEW);
        manager.addTask(task);

        assertEquals(task, manager.getTaskById(task.getId()), "Task должен быть добавлен и получен корректно");
    }

    @Test
    public void testUpdateTask() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Test Task", "Test Description", Status.NEW);
        manager.addTask(task);

        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);

        assertEquals(Status.IN_PROGRESS, manager.getTaskById(task.getId()).getStatus(), "Task должен быть обновлена корректно");
    }

    @Test
    public void testDeleteTask() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Test task", "Test Descriptin", Status.NEW);
        manager.addTask(task);

        manager.deleteTaskById(task.getId());

        assertNull(manager.getTaskById(task.getId()), "Task должен быть удален");
    }

    @Test
    public void testGetAllTasks() {
        TaskManager manager = Managers.getDefault();
        Task task1 = new Task("Test Task 1", "Test Description 1", Status.NEW);
        Task task2 = new Task("Test test 2", "Test Description 2", Status.NEW);
        manager.addTask(task1);
        manager.addTask(task2);

        List<Task> tasks = manager.getAllTasks();
        assertEquals(2,tasks.size(), "Должно быть 2 tasks");
        assertTrue(tasks.contains(task1), "Task 1 должен быть в списке");
        assertTrue(tasks.contains(task2), "Task 2 должен быть в списке");
    }
}
