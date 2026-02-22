package nuke.mission;

/**
 * Represents a generic mission in the Nuke chatbot.
 *
 * <p>{@code Mission} serves as the abstract base class for all
 * mission types (e.g., {@link Task}, {@link Strike}, {@link Operation}).
 * It stores common attributes such as description and completion status.
 *
 * <p>Concrete subclasses must implement {@link #toHistory()} and {@link #toString()} to define
 * how the mission is serialized for persistent storage and formatted to be printed.
 */
public abstract class Mission {
    private boolean isDone;
    private String description;

    /**
     * Creates a mission with the given description.
     * The mission is initially marked as not completed.
     *
     * @param description The description of the mission.
     */
    public Mission(String description) {
        this.isDone = false;
        this.description = description;
    }

    /**
     * Returns the description of this mission.
     *
     * @return the mission description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether this mission has been completed.
     *
     * @return {@code true} if the mission is marked as done,
     *         {@code false} otherwise
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Marks this mission as completed.
     */
    public void setDone() {
        this.isDone = true;
    }

    /**
     * Marks this mission as not completed.
     */
    public void setUndone() {
        this.isDone = false;
    }

    /**
     * Returns a user-friendly string representation of the mission.
     * Different type of Mission will add their own identification
     *
     * <p>The format is:
     * <pre>
     * [X] description   // if completed
     * [ ] description   // if not completed
     * </pre>
     *
     * @return A formatted string showing completion status and description.
     */
    public String toString() {
        String res = "";
        res = ((this.isDone) ? "[X]" : "[ ]") + " " + description;
        return res;
    }

    /**
     * Converts the mission into a formatted string suitable for
     * storage in the history file.
     *
     * <p>Subclasses must define the exact format required to
     * reconstruct the mission later via the parser.
     *
     * @return A formatted string representation for persistent storage.
     */
    public abstract String toHistory();
}
