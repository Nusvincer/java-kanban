package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.util.ManagerSaveException;
import com.yandex.app.util.Status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    public void save() {
        StringBuilder sb = new StringBuilder();
        sb.append("id,type,name,status,description,duration,startTime,epic\n");

        for (Task task : getAllTasks()) {
            sb.append(toString(task)).append("\n");
        }
        for (Epic epic : getAllEpics()) {
            sb.append(toString(epic)).append("\n");
        }
        for (Subtask subtask : getAllSubtasks()) {
            sb.append(toString(subtask)).append("\n");
        }

        try {
            Files.writeString(file.toPath(), sb.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения данных", e);
        }
    }

    private String toString(Task task) {
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            return String.format("%d,SUBTASK,%s,%s,%s,%d,%s,%d",
                    subtask.getId(),
                    subtask.getName(),
                    subtask.getStatus(),
                    subtask.getDescription(),
                    subtask.getDuration() != null ? subtask.getDuration().toMinutes() : 0,
                    subtask.getStartTime(),
                    subtask.getEpicId());
        } else if (task instanceof Epic) {
            return String.format("%d,EPIC,%s,%s,%s,,,",
                    task.getId(),
                    task.getName(),
                    task.getStatus(),
                    task.getDescription());
        } else {
            return String.format("%d,TASK,%s,%s,%s,%d,%s,",
                    task.getId(),
                    task.getName(),
                    task.getStatus(),
                    task.getDescription(),
                    task.getDuration() != null ? task.getDuration().toMinutes() : 0,
                    task.getStartTime());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines.subList(1, lines.size())) {
                if (line.isBlank()) continue;

                Task task = fromString(line);
                if (task instanceof Epic) {
                    manager.addEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    manager.addSubtask((Subtask) task);
                } else if (task != null) {
                    manager.addTask(task);
                } else {
                    throw new ManagerSaveException("Некорректный формат строки: " + line);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла: " + file.getName(), e);
        }
        return manager;
    }

    private static Task fromString(String value) {
        try {
            String[] parts = value.split(",", -1);
            int id = Integer.parseInt(parts[0]);
            String type = parts[1];
            String name = parts[2];
            Status status = Status.valueOf(parts[3]);
            String description = parts[4];
            Duration duration = parts[5].isBlank() ? null : Duration.ofMinutes(Long.parseLong(parts[5]));
            LocalDateTime startTime = parts[6].isBlank() ? null : LocalDateTime.parse(parts[6]);

            switch (type) {
                case "TASK":
                    return new Task(name, description, status, duration, startTime);
                case "EPIC":
                    return new Epic(name, description, status);
                case "SUBTASK":
                    int epicId = Integer.parseInt(parts[7]);
                    return new Subtask(name, description, status, epicId, duration, startTime);
                default:
                    throw new ManagerSaveException("Некорректная строка: " + value);
            }
        } catch (Exception e) {
            throw new ManagerSaveException("Ошибка при разборе строки: " + value, e);
        }
    }
}


