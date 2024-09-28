package test.model;

import com.yandex.app.model.Epic;
import com.yandex.app.service.TaskManager;
import com.yandex.app.util.Managers;
import com.yandex.app.util.Status;
import org.junit.jupiter.api.Test;

import java.util.List;

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

        assertEquals(2, epics.size(), "Должно быть 2 epics");
        assertTrue(epics.contains(epic1),"Epic 1 должен быть в списке");
        assertTrue(epics.contains(epic2), "Epic 2 должен быть в списке");
    }
}
