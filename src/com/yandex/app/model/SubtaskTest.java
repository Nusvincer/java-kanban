package com.yandex.app.model;

import com.yandex.app.util.Status;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void testSubtaskEqualityById() {
        Subtask subtask1 = new Subtask("Задача 1", "Описание 1", Status.NEW, 1);
        Subtask subtask2 = new Subtask("Задача 2", "Описание 2", Status.NEW, 1);

        subtask1.setId(1);
        subtask2.setId(1);

        assertEquals(subtask1, subtask2);
    }

    @Test
    void testSubtaskInequalityById() {
        Subtask subtask1 = new Subtask("Задача 1", "Описание 1", Status.NEW, 1);
        Subtask subtask2 = new Subtask("Задача 2", "Описание 2", Status.NEW, 1);

        subtask1.setId(1);
        subtask2.setId(2);

        assertNotEquals(subtask1, subtask2);
    }
}