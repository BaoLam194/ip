package nuke.mission;
/**
 * Represents an operation-type mission with a daedline time.
 *
 * <p>An {@code Operation} extends {@link Mission} by adding
 * a specific execution date and time by variable {@code by} .
 */
public class Strike extends Mission {

    protected String by;

    /**
     * Creates a mission with the given description.
     * The mission is initially marked as not completed.
     *
     * @param description The description of the mission
     * @param by The deadline that the mission must be executed
     */
    public Strike(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Add it own type representation [D] into string format.
     * E.g: [D][X] read book (by: today)
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    public String toHistory() {
        String res = "";
        res += "Type: deadline";
        res += System.lineSeparator();
        res += "Desc: " + this.getDescription();
        res += System.lineSeparator();
        res += "By: " + this.by;
        res += System.lineSeparator();
        res += "Mark: " + this.isDone();
        res += System.lineSeparator();
        return res;
    }
}