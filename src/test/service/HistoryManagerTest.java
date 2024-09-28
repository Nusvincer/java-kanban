package test.service;

import com.yandex.app.model.Task;
import com.yandex.app.service.HistoryManager;
import com.yandex.app.util.Managers;
import com.yandex.app.util.Status;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {
    @Test
    public void testAddAndGetHistory() {
        HistoryManager manager = Managers.getDefaultHistory();
        Task task = new Task("Test Task", "Test Description", Status.NEW);
        manager.add(task);

        List<Task> history = manager.getHistory();
        assertTrue(history.contains(task), "Task должен быть добавлен в историю");
    }

    @Test
    public void testRemoveFromHistory() {
        HistoryManager manager = Managers.getDefaultHistory();
        Task task = new Task("Test Task", "Test Description", Status.NEW);
        manager.add(task);

        manager.remove(task.getId());

        List<Task> history = manager.getHistory();
        assertFalse(history.contains(task), "Task должен быть удален из истории");
    }
}
