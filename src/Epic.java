import java.util.List;
import java.util.ArrayList;

public class Epic extends Task {
    private List<Subtask> subtasks;

    public Epic(String description, String name, int id, Status status) {
        super(description, name, id, status);
        this.subtasks = new ArrayList<>();
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtasks(Subtask subtask) {
        subtasks.add(subtask);
    }
}
