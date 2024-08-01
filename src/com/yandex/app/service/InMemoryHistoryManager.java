package com.yandex.app.service;

import com.yandex.app.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager extends HistoryManager {
    private List<Task> history = new ArrayList<>();
    private Map<Integer, Task> historyMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        if (historyMap.containsKey(task.getId())) {
            history.remove(task);
        }

        history.add(task);
        historyMap.put(task.getId(), task);

        if (history.size() > 10) {
            Task removedTask = history.remove(0);
            historyMap.remove(removedTask.getId());
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
