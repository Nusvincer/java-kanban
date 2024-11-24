package com.yandex.app.http;

import com.google.gson.Gson;
import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.TaskManager;
import com.yandex.app.util.GsonProvider;
import com.yandex.app.util.Managers;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager manager;
    private final Gson gson;

    public HttpTaskServer() throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.manager = Managers.getDefault();
        this.gson = GsonProvider.getGson();

        initializeHandlers();
    }

    private void initializeHandlers() {
        server.createContext("/tasks", this::handleTasks);
        server.createContext("/subtasks", this::handleSubtasks);
        server.createContext("/epics", this::handleEpics);
        server.createContext("/history", this::handleHistory);
        server.createContext("/prioritized", this::handlePrioritizedTasks);
    }

    public TaskManager getTaskManager() {
        return manager;
    }

    private void handleTasks(HttpExchange exchange) throws IOException {
        handleEntities(exchange, "task");
    }

    private void handleSubtasks(HttpExchange exchange) throws IOException {
        handleEntities(exchange, "subtask");
    }

    private void handleEpics(HttpExchange exchange) throws IOException {
        handleEntities(exchange, "epic");
    }

    private void handleEntities(HttpExchange exchange, String entityType) throws IOException {
        String method = exchange.getRequestMethod();
        String[] pathSegments = exchange.getRequestURI().getPath().split("/");
        String response = "";

        try {
            if ("GET".equalsIgnoreCase(method)) {
                if (pathSegments.length == 2) {
                    switch (entityType) {
                        case "task" -> response = gson.toJson(manager.getAllTasks());
                        case "subtask" -> response = gson.toJson(manager.getAllSubtasks());
                        case "epic" -> response = gson.toJson(manager.getAllEpics());
                    }
                } else if (pathSegments.length == 3) {
                    int id = Integer.parseInt(pathSegments[2]);
                    Object entity = switch (entityType) {
                        case "task" -> manager.getTaskById(id);
                        case "subtask" -> manager.getSubtaskById(id);
                        case "epic" -> manager.getEpicById(id);
                        default -> null;
                    };
                    if (entity == null) {
                        sendResponse(exchange, 404, "Entity not found");
                        return;
                    }
                    response = gson.toJson(entity);
                }
                sendResponse(exchange, 200, response);
            } else if ("POST".equalsIgnoreCase(method)) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                try {
                    switch (entityType) {
                        case "task" -> {
                            Task task = gson.fromJson(body, Task.class);
                            if (task.getId() == 0) {
                                manager.addTask(task);
                            } else {
                                manager.updateTask(task);
                            }
                        }
                        case "subtask" -> {
                            Subtask subtask = gson.fromJson(body, Subtask.class);
                            if (subtask.getId() == 0) {
                                manager.addSubtask(subtask);
                            } else {
                                manager.updateSubtask(subtask);
                            }
                        }
                        case "epic" -> {
                            Epic epic = gson.fromJson(body, Epic.class);
                            if (epic.getId() == 0) {
                                manager.addEpic(epic);
                            } else {
                                manager.updateEpic(epic);
                            }
                        }
                    }
                    sendResponse(exchange, 201, "");
                } catch (Exception e) {
                    sendResponse(exchange, 400, "Invalid JSON format");
                }
            } else if ("DELETE".equalsIgnoreCase(method)) {
                if (pathSegments.length == 3) {
                    int id = Integer.parseInt(pathSegments[2]);
                    switch (entityType) {
                        case "task" -> manager.deleteTaskById(id);
                        case "subtask" -> manager.deleteSubtaskById(id);
                        case "epic" -> manager.deleteEpicById(id);
                    }
                }
                sendResponse(exchange, 200, "");
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        } catch (IllegalArgumentException e) {
            sendResponse(exchange, 406, "Entity has overlapping time or invalid data: " + e.getMessage());
        } catch (Exception e) {
            sendResponse(exchange, 500, "Internal Server Error: " + e.getMessage());
        }
    }

    private void handleHistory(HttpExchange exchange) throws IOException {
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            String response = gson.toJson(manager.getHistory());
            sendResponse(exchange, 200, response);
        } else {
            sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    private void handlePrioritizedTasks(HttpExchange exchange) throws IOException {
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            String response = gson.toJson(manager.getPrioritizedTasks());
            sendResponse(exchange, 200, response);
        } else {
            sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
        exchange.close();
    }

    public void start() {
        server.start();
        System.out.println("Server started on port " + PORT);
    }

    public void stop() {
        server.stop(0);
        System.out.println("Server stopped");
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer();
        server.start();
    }
}
