package nuke.mission;

public class Operation extends Mission {

    private String from;
    private String to;

    public Operation(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    public String toHistory() {
        String res="";
        res += "Type: event";
        res += System.lineSeparator();
        res +=  "Desc: " + this.getDescription();
        res += System.lineSeparator();
        res +=  "From: " + this.from;
        res += System.lineSeparator();
        res +=  "To: " + this.to;
        res += System.lineSeparator();
        res +=  "Mark: " + this.isDone();
        res += System.lineSeparator();
        return res;
    }

}