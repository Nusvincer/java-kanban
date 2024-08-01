package com.yandex.app.service;

import com.yandex.app.model.Task;
import com.yandex.app.util.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new HistoryManager();
    }

    @Test
    void testAddAndRetrieveHistory() {
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        task1.setId(1);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW);
        task2.setId(2);

        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    void testHistoryPreservesTaskState() {
        Task task = new Task("Задача 1", "Описание 1", Status.NEW);
        task.setId(1);

        historyManager.add(task);

        task.setDescription("Обновленное описания");
        List<Task> history = historyManager.getHistory();
        assertNotEquals("Обновленное описание", history.get(0).getDescription());
    }
}