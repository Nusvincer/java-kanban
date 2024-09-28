package test.model;


import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.service.TaskManager;
import com.yandex.app.util.Managers;
import com.yandex.app.util.Status;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskManagerTest {

    @Test
    public void testAddAndGetSubtask() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Test Epic", "Test Description", Status.NEW);
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Test Subtask", "Test Description", Status.NEW, epic.getId());
        manager.addSubtask(subtask);

        assertEquals(subtask, manager.getSubtaskById(subtask.getId()), "Subtask должен быть получен и добавлен корректно");
    }

    @Test
    public void testUpdateSubtask() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Test Epic", "Test Description", Status.NEW);
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Test Subtask", "Test Description", Status.NEW, epic.getId());
        manager.addSubtask(subtask);

        subtask.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask);

        assertEquals(Status.IN_PROGRESS, manager.getSubtaskById(subtask.getId()).getStatus(),"Subtask должен быть обновлен корректно");
    }

    @Test
    public void testDeleteSubtask() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Test Epic", "Test Description", Status.NEW);
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Test Subtask", "Test Description", Status.NEW, epic.getId());
        manager.addSubtask(subtask);

        manager.deleteSubtaskById(subtask.getId());

        assertNull(manager.getSubtaskById(subtask.getId()), "Subtask должен быть удален");
    }
}
