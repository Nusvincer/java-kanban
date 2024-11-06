package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.util.ManagerSaveException;
import com.yandex.app.util.Status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    private static final int EXPECTED_NUMBER_OF_COLUMNS = 6;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public void save() {
        StringBuilder sb = new StringBuilder();
        sb.append("id,type,name,status,description,epic\n");

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
            System.err.println("Ошибка при сохранении в файл: " + file.getAbsolutePath());
            throw new ManagerSaveException("Ошибка при сохранении в файл", e);
        }
    }


    private String toString(Task task) {
        String type;
        if (task instanceof Subtask) {
            type = "SUBTASK";
            int epicId = ((Subtask) task).getEpicId();
            return String.format("%d,%s,%s,%s,%s,%d", task.getId(), type, task.getName(), task.getStatus(), task.getDescription(),
                    epicId);
        } else if (task instanceof Epic) {
            type = "EPIC";
            return String.format("%d,%s,%s,%s,%s,", task.getId(), type, task.getName(), task.getStatus(), task.getDescription());
        } else {
            type = "TASK";
            return String.format("%d,%s,%s,%s,%s,", task.getId(), type, task.getName(), task.getStatus(), task.getDescription());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines.subList(1, lines.size())) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] columns = line.split(",", -1);
                if (columns.length != EXPECTED_NUMBER_OF_COLUMNS) {
                    throw new ManagerSaveException("Некорректная строка: " + line);
                }

                Task task = fromString(line);
                if (task != null) {
                    if (task instanceof Epic) {
                        manager.addEpic((Epic) task);
                    } else if (task instanceof Subtask) {
                        manager.addSubtask((Subtask) task);
                    } else {
                        manager.addTask(task);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке из файла: " + file.getAbsolutePath());
            throw new ManagerSaveException("Ошибка при загрузке из файла", e);
        } catch (ManagerSaveException e) {
            System.err.println("Ошибка при обработке файла: " + file.getAbsolutePath());
            throw e;
        } catch (Exception e) {
            System.err.println("Ошибка при обработке файла: " + file.getAbsolutePath());
            throw new ManagerSaveException("Ошибка при обработке файла", e);
        }
        return manager;
    }

    private static Task fromString(String value) {
        String[] parts = value.split(",");
        if (parts.length < 6) {
            return null;
        }
        int id = Integer.parseInt(parts[0]);
        String type = parts[1];
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        int epicId = parts.length > 5 && !parts[5].isEmpty() ? Integer.parseInt(parts[5]) : -1;

        switch (type) {
            case "TASK":
                return new Task(name, description, status);
            case "EPIC":
                return new Epic(name, description, status);
            case "SUBTASK":
                return new Subtask(name, description, status, epicId);
            default:
                return null;
        }
    }
}