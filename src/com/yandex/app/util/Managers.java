package com.yandex.app.util;

import com.yandex.app.service.TaskManager;
import com.yandex.app.service.InMemoryTaskManager;
import com.yandex.app.service.HistoryManager;
import com.yandex.app.service.InMemoryHistoryManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
