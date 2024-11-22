package util;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.FileBackedTaskManager;
import com.yandex.app.util.ManagerSaveException;
import com.yandex.app.util.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private FileBackedTaskManager manager;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("taskManagerTest", ".csv");
        tempFile.deleteOnExit();
        manager = new FileBackedTaskManager(tempFile);
    }

    @AfterEach
    void tearDown() {
        try {
            Files.deleteIfExists(tempFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSaveEmptyFile() {
        manager.save();
        try {
            List<String> lines = Files.readAllLines(tempFile.toPath());
            assertEquals(1, lines.size());
            assertEquals("id,type,name,status,description,duration,startTime,epic", lines.get(0));
        } catch (IOException e) {
            fail("IOException возникла при чтении файла: " + e.getMessage());
        }
    }

    @Test
    void testSaveMultipleTasks() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        task1.setStartTime(LocalDateTime.of(2024, 11, 18, 10, 0));
        task1.setDuration(Duration.ofMinutes(120));

        Epic epic = new Epic("Epic 1", "Epic Description", Status.NEW);
        Subtask subtask = new Subtask("Subtask 1", "Subtask Description", Status.NEW, epic.getId());
        subtask.setStartTime(LocalDateTime.of(2024, 11, 18, 13, 0));
        subtask.setDuration(Duration.ofMinutes(90));

        manager.addTask(task1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);

        manager.save();

        try {
            List<String> lines = Files.readAllLines(tempFile.toPath());
            assertEquals(4, lines.size(), "Должно быть 4 строки: заголовок, задача, эпик и подзадача.");
        } catch (IOException e) {
            fail("IOException возникла при чтении файла: " + e.getMessage());
        }
    }

    @Test
    void testLoadMultipleTasks() {
        StringBuilder sb = new StringBuilder();
        sb.append("id,type,name,status,description,duration,startTime,epic\n");
        sb.append("1,TASK,Task 1,NEW,Description 1,120,2024-11-18T10:00,\n");
        sb.append("2,EPIC,Epic 1,NEW,Epic Description,,,\n");
        sb.append("3,SUBTASK,Subtask 1,NEW,Subtask Description,90,2024-11-18T13:00,2\n");

        try {
            Files.writeString(tempFile.toPath(), sb.toString());

            FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

            Task loadedTask = loadedManager.getTaskById(1);
            Epic loadedEpic = loadedManager.getEpicById(2);
            Subtask loadedSubtask = loadedManager.getSubtaskById(3);

            assertNotNull(loadedTask, "Задача должна быть загружена.");
            assertEquals(LocalDateTime.of(2024, 11, 18, 10, 0), loadedTask.getStartTime(), "Время начала задачи должно совпадать.");
            assertEquals(Duration.ofMinutes(120), loadedTask.getDuration(), "Продолжительность задачи должна совпадать.");

            assertNotNull(loadedEpic, "Эпик должен быть загружен.");
            assertEquals("Epic 1", loadedEpic.getName(), "Название эпика должно совпадать.");

            assertNotNull(loadedSubtask, "Подзадача должна быть загружена.");
            assertEquals(2, loadedSubtask.getEpicId(), "ID эпика у подзадачи должен совпадать.");
            assertEquals(Duration.ofMinutes(90), loadedSubtask.getDuration(), "Продолжительность подзадачи должна совпадать.");
        } catch (IOException e) {
            fail("IOException возникла при записи или чтении файла: " + e.getMessage());
        }
    }

    @Test
    void testLoadInvalidDataFormat() {
        String invalidData = """
        id,type,name,status,description,duration,startTime,epic
        1,TASK,Task 1,NEW,Description 1,,,
        invalid_line
        """;

        try {
            Files.writeString(tempFile.toPath(), invalidData);
            ManagerSaveException exception = assertThrows(ManagerSaveException.class, () -> {
                FileBackedTaskManager.loadFromFile(tempFile);
            });

            assertEquals("Ошибка при разборе строки: invalid_line", exception.getMessage(), "Сообщение об ошибке должно совпадать.");
        } catch (IOException e) {
            fail("Ошибка записи тестовых данных в файл: " + e.getMessage());
        }
    }

    @Test
    void testSaveAndLoadWithNewFields() {
        Task task = new Task("Task", "Description", Status.NEW);
        task.setStartTime(LocalDateTime.of(2024, 11, 18, 10, 0));
        task.setDuration(Duration.ofMinutes(60));

        manager.addTask(task);
        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        Task loadedTask = loadedManager.getTaskById(task.getId());
        assertEquals(task.getStartTime(), loadedTask.getStartTime(), "Время начала должно совпадать");
        assertEquals(task.getDuration(), loadedTask.getDuration(), "Продолжительность должна совпадать");
    }
}
