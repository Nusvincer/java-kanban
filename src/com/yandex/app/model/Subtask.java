package com.yandex.app.model;

import com.yandex.app.util.Status;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String description, String name, Status status, int epicId) {
        super(description, name, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
