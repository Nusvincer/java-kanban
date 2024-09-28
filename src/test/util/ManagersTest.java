package test.util;

import com.yandex.app.service.HistoryManager;
import com.yandex.app.service.TaskManager;
import com.yandex.app.util.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManagersTest {

    @Test
    public void testDefaultTaskManager() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager, "Менеджер задач должен быть проинициализирован и не должен быть null");
    }

    @Test
    public void testDefaultHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "Менеджер истории должен быть проинициализирован и не должен быть null");
    }
}