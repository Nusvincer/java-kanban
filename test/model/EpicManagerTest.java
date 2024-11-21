package model;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.service.InMemoryTaskManager;
import com.yandex.app.service.TaskManager;
import com.yandex.app.util.Managers;
import com.yandex.app.util.Status;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EpicManagerTest {

    @Test
    public void testAddAndGetEpic() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Test Epic", "Test description", Status.NEW);
        manager.addEpic(epic);
        assertEquals(epic, manager.getEpicById(epic.getId()), "Epic должен был получен и добавлен корректно");
    }

    @Test
    public void testDeleteEpic() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Test Epic", "Test Description", Status.NEW);
        manager.addEpic(epic);

        manager.deleteEpicById(epic.getId());

        assertNull(manager.getEpicById(epic.getId()), "Epic должен быть удален");
    }

    @Test
    public void testUpdateEpic() {
        TaskManager manager = Managers.getDefault();
        Epic epic = new Epic("Test Epic", "Test Description", Status.NEW);
        manager.addEpic(epic);

        epic.setStatus(Status.IN_PROGRESS);
        manager.updateEpic(epic);

        assertEquals(Status.IN_PROGRESS, manager.getEpicById(epic.getId()).getStatus(), "Эпик должен быть обновлен корректно");
    }

    @Test
    public void testGetAllEpics() {
        TaskManager manager = Managers.getDefault();
        Epic epic1 = new Epic("Test Epic 1", "Test Description 1", Status.NEW);
        Epic epic2 = new Epic("Test Epic 2", "Test Description 2", Status.NEW);
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        List<Epic> epics = manager.getAllEpics();
        System.out.println("Epics:");
        for (Epic epic : epics) {
            System.out.println("Epic: " + epic.getName());
        }

        assertEquals(3, epics.size(), "Должно быть 2 epics");
        assertTrue(epics.contains(epic1),"Epic 1 должен быть в списке");
        assertTrue(epics.contains(epic2), "Epic 2 должен быть в списке");
    }

    @Test
    public void testEpicPropertiesCalculation() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic("Epic Test", "Description", Status.NEW);
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", Status.NEW, epic.getId());
        subtask1.setStartTime(LocalDateTime.of(2024, 11, 18, 10, 0));
        subtask1.setDuration(Duration.ofMinutes(120));

        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", Status.NEW, epic.getId());
        subtask2.setStartTime(LocalDateTime.of(2024, 11, 18, 13, 0));
        subtask2.setDuration(Duration.ofMinutes(60));

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertEquals(Duration.ofMinutes(180), epic.getDuration(), "Общая продолжительность эпика должна быть 180 минут");
        assertEquals(LocalDateTime.of(2024, 11, 18, 10, 0), epic.getStartTime(), "Время начала эпика должно быть 10:00");
        assertEquals(LocalDateTime.of(2024, 11, 18, 14, 0), epic.getEndTime(), "Время окончания эпика должно быть 14:00");
    }

    @Test
    public void testEpicPropertiesNoSubtasks() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic("Epic Test", "Description", Status.NEW);
        manager.addEpic(epic);

        assertNull(epic.getStartTime(), "Если нет подзадач, время начала эпика должно быть null");
        assertNull(epic.getDuration(), "Если нет подзадач, продолжительность эпика должна быть null");
        assertNull(epic.getEndTime(), "Если нет подзадач, время окончания эпика должно быть null");
    }
}
