package test;

import com.yandex.app.model.Task;
import com.yandex.app.service.TaskManager;
import com.yandex.app.util.Managers;
import com.yandex.app.util.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class InMemoryTaskManagerIdTest {

    @Test
    public void testTaskIdGenerationConflict() {
        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Описание 1", "Название 1", Status.NEW);
        Task task2 = new Task("Описание 2", "Название 2", Status.NEW);

        manager.addTask(task1);
        manager.addTask(task2);

        int id1 = task1.getId();
        int id2 = task2.getId();

        assertNotEquals(id1, id2, "Задачи с разными описаниями должны иметь разные ID");
    }
}