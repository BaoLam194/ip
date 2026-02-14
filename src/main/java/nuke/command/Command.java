package nuke.command;

public abstract class Command {
    private boolean isDone;
    private String description;

    // Constructor
    public Command(String description) {
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
