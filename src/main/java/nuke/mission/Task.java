package nuke.mission;
/**
 * Represents a task-type mission.
 *
 * <p>An {@code Task} extends {@link Mission}
 */
public class Task extends Mission {
    /**
     * Creates a mission with the given description.
     * The mission is initially marked as not completed.
     * No need parameter as it inherits the Mission directly
     */
    public Task(String name) {
        super(name);
    }

    /**
     * Add it own type representation [T] into string format.
     * E.g: [T][X] make book
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }


    public String toHistory() {
        String res = "";
        res += "Type: todo";
        res += System.lineSeparator();
        res += "Desc: " + this.getDescription();
        res += System.lineSeparator();
        res += "Mark: " + this.isDone();
        res += System.lineSeparator();
        return res;
    }
}
