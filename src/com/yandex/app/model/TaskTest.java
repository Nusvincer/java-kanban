package com.yandex.app.model;

import com.yandex.app.util.Status;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testTaskEqualityById() {
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW);

        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2);
    }

    @Test
    void testTaskInequalityById() {
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW);

        task1.setId(1);
        task2.setId(2);

        assertNotEquals(task1, task2);
    }
}