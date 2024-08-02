package test;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.TaskManager;
import com.yandex.app.util.Managers;
import com.yandex.app.util.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    @Test
    public void testTaskAdditionAndRetrieval() {
        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Описание 1", "Название 1", Status.NEW);
        Task task2 = new Task("Описание 2", "Название 2", Status.NEW);
        Epic epic = new Epic("Описание эпика", "Название эпика", Status.NEW);
        Subtask subtask = new Subtask("Описание подзадачи", "Название подзадачи", Status.NEW, epic.getId());

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic);
        manager.addSubtask(subtask);

        Task retrievedTask1 = manager.getTaskById(task1.getId());
        Task retrievedTask2 = manager.getTaskById(task2.getId());
        Epic retrievedEpic = manager.getEpicById(epic.getId());
        Subtask retrievedSubtask = manager.getSubtaskById(subtask.getId());

        assertNotNull(retrievedTask1, "Задача 1 должна быть доступна по ID");
        assertEquals(task1, retrievedTask1, "Полученная задача 1 должна совпадать с добавленной задачей 1");

        assertNotNull(retrievedTask2, "Задача 2 должна быть доступна по ID");
        assertEquals(task2, retrievedTask2, "Полученная задача 2 должна совпадать с добавленной задачей 2");

        assertNotNull(retrievedEpic, "Эпик должен быть доступен по ID");
        assertEquals(epic, retrievedEpic, "Полученный эпик должен совпадать с добавленным эпиком");

        assertNotNull(retrievedSubtask, "Подзадача должна быть доступна по ID");
        assertEquals(subtask, retrievedSubtask, "Полученная подзадача должна совпадать с добавленной подзадачей");
    }
}