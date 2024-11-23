package http;

import com.google.gson.Gson;
import com.yandex.app.http.HttpTaskServer;
import com.yandex.app.model.Task;
import com.yandex.app.util.GsonProvider;
import com.yandex.app.util.Managers;
import com.yandex.app.util.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    private HttpTaskServer server;
    private HttpClient client;
    private Gson gson;

    @BeforeEach
    void setUp() throws IOException {
        Managers.getDefault().clearAll();
        server = new HttpTaskServer();
        server.start();
        client = HttpClient.newHttpClient();
        gson = GsonProvider.getGson();
    }

    @AfterEach
    public void tearDown() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    void testGetEmptyTasks() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа должен быть 200");
        assertEquals("[]", response.body(), "Тело ответа должно быть пустым списком");
    }

    @Test
    void testCreateTask() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Test Description", Status.NEW, Duration.ofMinutes(60), LocalDateTime.of(2024, 1, 1, 10, 0));
        String taskJson = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Код ответа должен быть 201");

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        Task[] tasks = gson.fromJson(getResponse.body(), Task[].class);
        assertEquals(1, tasks.length, "Должна быть добавлена одна задача");
        assertEquals("Test Task", tasks[0].getName(), "Имя задачи должно совпадать");
    }

    @Test
    void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Test Description", Status.NEW, Duration.ofMinutes(60), LocalDateTime.of(2024, 1, 1, 10, 0));
        String taskJson = gson.toJson(task);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, postResponse.statusCode(), "Код ответа при создании задачи должен быть 201");

        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .DELETE()
                .build();
        HttpResponse<String> deleteResponse = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, deleteResponse.statusCode(), "Код ответа должен быть 200");

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .GET()
                .build();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, getResponse.statusCode(), "Код ответа должен быть 404, так как задачи больше нет");
    }
}