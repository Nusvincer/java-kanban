package service;

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

    @Test
    public void testHistoryClear() {
        HistoryManager manager = Managers.getDefaultHistory();

        Task task1 = new Task("Task 1", "Description", Status.NEW);
        Task task2 = new Task("Task 2", "Description", Status.NEW);

        manager.add(task1);
        manager.add(task2);

        manager.clear();

        assertTrue(manager.getHistory().isEmpty(), "История должна быть очищена");
    }
}
