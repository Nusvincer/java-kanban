package test.service;


import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.TaskManager;
import com.yandex.app.util.Managers;
import com.yandex.app.util.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataManagerTest {

    @Test
    public void testTaskStatusChangeDoesNotAffectManager() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Test Task", "Test Description", Status.NEW);
        manager.addTask(task);

        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);

        assertEquals(Status.IN_PROGRESS, manager.getTaskById(task.getId()).getStatus(), "Статус task в менеджере должен быть IN_PROGRESS");
    }

    @Test
    public void testEpicStatusChangeDoesNotAffectManager() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Test Epic", "Test Description", Status.NEW);
        manager.addEpic(epic);

        epic.setStatus(Status.IN_PROGRESS);
        manager.updateEpic(epic);

        assertEquals(Status.IN_PROGRESS, manager.getEpicById(epic.getId()).getStatus(), "Статус epic в менеджере должен быть IN_PROGRESS");
    }

    @Test
    public void testSubtaskStatusChangeDoesNotAffectManager() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Test Epic", "Test Description", Status.NEW);
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Test Subtask", "Test Description", Status.NEW, epic.getId());
        manager.addSubtask(subtask);

        subtask.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask);

        assertEquals(Status.IN_PROGRESS, manager.getSubtaskById(subtask.getId()).getStatus(), "Статус subtask в менеджере должен быть IN_PROGRESS");
    }

    @Test
    public void testTaskDescriptionChangeDoesNotAffectManager() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Test Task", "Test Description", Status.NEW);
        manager.addTask(task);

        task.setDescription("New Description");
        manager.updateTask(task);

        assertEquals("New Description", manager.getTaskById(task.getId()).getDescription(), "Описание task в менеджере должно быть New Description");
    }

    @Test
    public void testEpicDescriptionChangeDoesNotAffectManager() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Test Epic", "Test Description", Status.NEW);
        manager.addEpic(epic);

        epic.setDescription("New Description");
        manager.updateEpic(epic);

        assertEquals("New Description", manager.getEpicById(epic.getId()).getDescription(), "Описание epic в менеджере должно быть New Description");
    }

    @Test
    public void testSubtaskDescriptionChangeDoesNotAffectManager() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Test Epic", "Test Description", Status.NEW);
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Test Subtask", "Test Description", Status.NEW, epic.getId());
        manager.addSubtask(subtask);

        subtask.setDescription("New Description");
        manager.updateSubtask(subtask);

        assertEquals("New Description", manager.getSubtaskById(subtask.getId()).getDescription(), "Описание subtask в менеджере должно быть New Description");
    }
}


