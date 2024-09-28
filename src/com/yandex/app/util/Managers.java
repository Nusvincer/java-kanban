package com.yandex.app.util;

import com.yandex.app.service.TaskManager;
import com.yandex.app.service.InMemoryTaskManager;
import com.yandex.app.service.HistoryManager;
import com.yandex.app.service.InMemoryHistoryManager;

public class Managers {
    private static TaskManager taskManager;
    private static HistoryManager historyManager;

    public static TaskManager getDefault() {
        if (taskManager == null) {
            taskManager = new InMemoryTaskManager();
        }
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        if (historyManager == null) {
            historyManager = new InMemoryHistoryManager();
        }
        return historyManager;
    }
}
