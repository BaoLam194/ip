package nuke.command;

public class Deadline extends Command {

    protected String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    public String toHistory() {
        String res="";
        res += "Type: deadline";
        res += System.lineSeparator();
        res +=  "Desc: " + this.getDescription();
        res += System.lineSeparator();
        res +=  "By: " + this.by;
        res += System.lineSeparator();
        res +=  "Mark: " + this.isDone();
        res += System.lineSeparator();
        return res;
    }
}