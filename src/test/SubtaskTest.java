package test;

import com.yandex.app.model.Subtask;
import com.yandex.app.util.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SubtaskTest {

    @Test
    public void testSubtaskCannotBeEpic() {
        Subtask subtask = new Subtask("Описание подзадачи", "Название подзадачи", Status.NEW, 1);
        subtask.setId(1);

        assertThrows(IllegalArgumentException.class, () -> {
            subtask.setEpicId(subtask.getId());
        }, "Подзадача не должна устанавливать себя в качестве эпика");
    }
}