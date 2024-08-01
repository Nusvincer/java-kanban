package com.yandex.app.service;

import com.yandex.app.model.Task;

import java.util.ArrayList;
import java.util.List;

public class HistoryManager {
    private List<Task> history;

    public HistoryManager() {
        this.history = new ArrayList<>();
    }

    public void add(Task task) {
        history.add(task);
    }

    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
