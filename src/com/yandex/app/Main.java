package com.yandex.app;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.InMemoryTaskManager;
import com.yandex.app.service.TaskManager;
import com.yandex.app.util.Status;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW);
        manager.addTask(task1);
        manager.addTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание Эпика 1", Status.NEW);
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание Подзадачи 1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание Подзадачи 2", Status.NEW, epic1.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        Epic epic2 = new Epic("Эпик 2", "Описание Эпика 2", Status.NEW);
        manager.addEpic(epic2);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание Подзадачи 3", Status.NEW, epic2.getId());
        manager.addSubtask(subtask3);

        System.out.println("Задачи: " + manager.getAllTasks());
        System.out.println("Эпики: " + manager.getAllEpics());
        System.out.println("Подзадачи: " + manager.getAllSubtasks());

        task1.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task1);
        subtask1.setStatus(Status.DONE);
        manager.updateSubtask(subtask1);
        System.out.println("Обновленные задачи: " + manager.getAllTasks());
        System.out.println("Обновленные эпики: " + manager.getAllEpics());

        manager.deleteTaskById(task1.getId());
        manager.deleteEpicById(epic1.getId());
        System.out.println("Задачи после удаления: " + manager.getAllTasks());
        System.out.println("Эпики после удаления: " + manager.getAllEpics());
    }
}