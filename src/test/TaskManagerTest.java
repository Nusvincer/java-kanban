package test;

import com.yandex.app.model.Task;
import com.yandex.app.service.TaskManager;
import com.yandex.app.util.Managers;
import com.yandex.app.util.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {

    @Test
    public void testAddAndGetTask() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Test Task", "Test Description", Status.NEW);
        manager.addTask(task);

        assertEquals(task, manager.getTaskById(task.getId()), "Задача должна быть добавлена и получена корректно");
    }

    @Test
    public void testUpdateTask() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Test Task", "Test Description", Status.NEW);
        manager.addTask(task);

        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);

        assertEquals(Status.IN_PROGRESS, manager.getTaskById(task.getId()).getStatus(), "Задача должна быть обновлена корректно");
    }
}
