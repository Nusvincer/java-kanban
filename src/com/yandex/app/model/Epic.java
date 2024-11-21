package com.yandex.app.model;

import com.yandex.app.util.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasks;
    private LocalDateTime endTime;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        this.subtasks = new ArrayList<>();
    }

    public List<Integer> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(int subtaskId) {
        subtasks.add(subtaskId);
    }

    public void removeSubtask(int subtaskId) {
        subtasks.remove(Integer.valueOf(subtaskId));
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                ", endTime=" + endTime +
                ", subtasks=" + subtasks +
                '}';
    }
}
