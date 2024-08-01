package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.util.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class InMemoryTaskManagerTest {
    private InMemoryTaskManager manager;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
    }

    @Test
    void testAddAndGetTask() {
        Task task = new Task("Task 1", "Description 1", Status.NEW);
        manager.addTask(task);

        Task retrievedTask = manager.getTaskById(task.getId());
        assertEquals(task, retrievedTask);
    }

    @Test
    void testAddAndGetEpic() {
        Epic epic = new Epic("Epic 1", "Description 1", Status.NEW);
        manager.addEpic(epic);

        Epic retrievedEpic = manager.getEpicById(epic.getId());
        assertEquals(epic, retrievedEpic);
    }

    @Test
    void testAddAndGetSubtask() {
        Epic epic = new Epic("Epic 1", "Description 1", Status.NEW);
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", "Description 1", Status.NEW, epic.getId());
        manager.addSubtask(subtask);

        Subtask retrievedSubtask = manager.getSubtaskById(subtask.getId());
        assertEquals(subtask, retrievedSubtask);
    }

    @Test
    void testNoConflictBetweenGeneratedAndSpecifiedId() {
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW);

        manager.addTask(task1);
        task2.setId(1);  // Setting a specified id
        manager.addTask(task2);

        assertEquals(task1, manager.getTaskById(task1.getId()));
        assertEquals(task2, manager.getTaskById(task2.getId()));
    }

    @Test
    void testTaskImmutabilityUponAddition() {
        Task task = new Task("Задача 1", "Описание 1", Status.NEW);
        manager.addTask(task);

        Task retrievedTask = manager.getTaskById(task.getId());
        assertEquals(task.getName(), retrievedTask.getName());
        assertEquals(task.getDescription(), retrievedTask.getDescription());
        assertEquals(task.getStatus(), retrievedTask.getStatus());
    }
}