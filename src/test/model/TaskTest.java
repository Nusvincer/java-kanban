package test.model;

import com.yandex.app.model.Task;
import com.yandex.app.util.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {

    @Test
    public void testTasksEqualityById() {
        Task task1 = new Task("Описание 1", "Название 1", Status.NEW);
        Task task2 = new Task("Описание 2", "Название 2", Status.NEW);

        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2, "Задачи с одинаковыми ID должны быть равны");
    }
}