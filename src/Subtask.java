public class Subtask extends Task {
    private int epicId;

    public Subtask(String description, String name, int id, Status status, int epicId) {
        super(description, name, id, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
