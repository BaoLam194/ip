package nuke.mission;

public abstract class Mission {
    private boolean isDone;
    private String description;

    // Constructor
    public Mission(String description) {
        this.isDone = false;
        this.description = description;
    }

    // Getter and Setter
    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone() {
        this.isDone = true;
    }

    public void setUndone() {
        this.isDone = false;
    }

    //Convert into format : [ ] command
    public String toString() {
        String res = "";
        res = ((this.isDone) ? "[X]" : "[ ]") + " " + description;
        return res;
    }

    public abstract String toHistory();
}
