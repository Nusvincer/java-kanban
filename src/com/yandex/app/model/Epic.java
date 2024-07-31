package com.yandex.app.model;

import com.yandex.app.util.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasks;

    public Epic(String description, String name, Status status) {
        super(description, name, status);
        this.subtasks = new ArrayList<>();
    }

    public List<Integer> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(int subtaskId) {
        subtasks.add(subtaskId);
    }
}
