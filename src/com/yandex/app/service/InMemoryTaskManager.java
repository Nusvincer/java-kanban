package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasks;
    private Map<Integer, Epic> epics;
    private Map<Integer, Subtask> subtasks;
    private int currentId;
    private HistoryManager historyManager;

    private TreeSet<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        currentId = 0;
        historyManager = new InMemoryHistoryManager();
        prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));
    }

    private int generateId() {
        return ++currentId;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public void addTask(Task task) {
        if (task.getStartTime() != null) {
            validateTask(task);
            prioritizedTasks.add(task);
        }

        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (subtask.getStartTime() != null) {
            validateTask(subtask);
            prioritizedTasks.add(subtask);
        }

        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask.getId());
            calculateEpicProperties(epic.getId());
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            throw new IllegalArgumentException("Task c ID " + task.getId() + " не существует");
        }
        prioritizedTasks.remove(tasks.get(task.getId()));

        validateTask(task);

        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            throw new IllegalArgumentException("Эпик с таким ID не найден");
        }

        epics.put(epic.getId(), epic);
        calculateEpicProperties(epic.getId());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            throw new IllegalArgumentException("Subtask с ID " + subtask.getId() + " не существует");
        }
        prioritizedTasks.remove(subtasks.get(subtask.getId()));

        validateTask(subtask);

        subtasks.put(subtask.getId(), subtask);

        calculateEpicProperties(subtask.getEpicId());
        prioritizedTasks.add(subtask);
    }

    @Override
    public void deleteTaskById(int id) {
        Task task = tasks.remove(id);
        if (task != null && task.getStartTime() != null) {
            prioritizedTasks.remove(task);
        }
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            prioritizedTasks.remove(subtask);

            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(id);
                calculateEpicProperties(epic.getId());
            }
        }
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtasks()) {
                prioritizedTasks.remove(subtasks.remove(subtaskId));
                historyManager.remove(subtaskId);
            }
        }
        historyManager.remove(id);
    }

    @Override
    public void clearAll() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        prioritizedTasks.clear();
        historyManager.clear();
    }

    private void calculateEpicProperties(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }

        List<Subtask> epicSubtasks = epic.getSubtasks()
                .stream()
                .map(subtasks::get)
                .filter(subtask -> subtask != null && subtask.getStartTime() != null)
                .toList();

        if (epicSubtasks.isEmpty()) {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(null);
        } else {
            epic.setStartTime(epicSubtasks.stream()
                    .map(Subtask::getStartTime)
                    .min(LocalDateTime::compareTo)
                    .orElse(null));

            epic.setEndTime(epicSubtasks.stream()
                    .map(Subtask::getEndTime)
                    .max(LocalDateTime::compareTo)
                    .orElse(null));

            epic.setDuration(epicSubtasks.stream()
                    .map(Subtask::getDuration)
                    .filter(duration -> duration != null)
                    .reduce(Duration.ZERO, Duration::plus));
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private void validateTask(Task task) {
        if (task.getStartTime() == null) return;

        Task lower = prioritizedTasks.lower(task);
        Task higher = prioritizedTasks.higher(task);

        if ((lower != null && isTimeIntersecting(task, lower)) ||
                (higher != null && isTimeIntersecting(task, higher))) {
            throw new IllegalArgumentException("Задача пересекается с другой задачей.");
        }
    }

    @Override
    public boolean isTimeIntersecting(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false;
        }
        return task1.getStartTime().isBefore(task2.getEndTime()) &&
                task1.getEndTime().isAfter(task2.getStartTime());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}

