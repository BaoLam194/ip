package nuke.mission;
/**
 * Represents an operation-type mission with a scheduled time.
 *
 * <p>An {@code Operation} extends {@link Mission} by adding
 * a specific execution date and time by variable {@code from} and {@code to}.
 */
public class Operation extends Mission {

    private String from;
    private String to;

    /**
     * Creates a mission with the given description.
     * The mission is initially marked as not completed.
     *
     * @param description The description of the mission
     * @param from The starting time that the mission should begin
     * @param to The ending time that the mission must be done
     */
    public Operation(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }
    /**
     * Add it own type representation [E] into string format.
     * E.g: [E][X] write book (from: 1/1/1111 to:2/2/2222)
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    public String toHistory() {
        String res = "";
        res += "Type: event";
        res += System.lineSeparator();
        res += "Desc: " + this.getDescription();
        res += System.lineSeparator();
        res += "From: " + this.from;
        res += System.lineSeparator();
        res += "To: " + this.to;
        res += System.lineSeparator();
        res += "Mark: " + this.isDone();
        res += System.lineSeparator();
        return res;
    }

}