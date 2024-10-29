package test.util;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.FileBackedTaskManager;
import com.yandex.app.util.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private FileBackedTaskManager manager;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("taskManagerTest", ".csv");
        tempFile.deleteOnExit(); // Удаление файла при завершении теста
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
            assertEquals(1, lines.size()); // Только заголовок
            assertEquals("id,type,name,status,description,epic", lines.get(0));
        } catch (IOException e) {
            fail("IOException возникла при читении файла: " + e.getMessage());
        }
    }

    @Test
    void testSaveMultipleTasks() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.IN_PROGRESS);
        Epic epic = new Epic("Epic 1", "Epic Description", Status.NEW);
        Subtask subtask = new Subtask("Subtask 1", "Subtask Description", Status.NEW, epic.getId());

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
        manager.save();

        try {
            List<String> lines = Files.readAllLines(tempFile.toPath());
            assertEquals(5, lines.size());
        } catch (IOException e) {
            fail("IOException возникла при чтении файла: " + e.getMessage());
        }
    }

    @Test
    void testLoadMultipleTasks() {
        StringBuilder sb = new StringBuilder();
        sb.append("id,type,name,status,description,epic\n");
        sb.append("1,TASK,Task 1,NEW,Description 1,\n");
        sb.append("2,TASK,Task 2,IN_PROGRESS,Description 2,\n");
        sb.append("3,EPIC,Epic 1,NEW,Epic Description,\n");
        sb.append("4,SUBTASK,Subtask 1,NEW,Subtask Description,3\n");

        try {
            Files.writeString(tempFile.toPath(), sb.toString());
            FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
            assertEquals(1, loadedManager.getAllSubtasks().size());
        } catch (IOException e) {
            fail("IOException возникла при записи или чтении файла: " + e.getMessage());
        }
    }
}