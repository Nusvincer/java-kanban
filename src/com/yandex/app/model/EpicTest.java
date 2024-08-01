package com.yandex.app.model;

import com.yandex.app.util.Status;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void testEpicCannotAddSelfAsSubtask() {
        Epic epic = new Epic("Epic 1", "Description 1", Status.NEW);
        epic.setId(1);

        assertThrows(IllegalArgumentException.class, () -> {
            epic.addSubtask(epic.getId());
        });
    }
}