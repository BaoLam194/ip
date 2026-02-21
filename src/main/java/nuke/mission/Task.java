package nuke.mission;

public class Task extends Mission {

    public Task(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }


    public String toHistory() {
        String res="";
        res += "Type: todo";
        res += System.lineSeparator();
        res +=  "Desc: " + this.getDescription();
        res += System.lineSeparator();
        res +=  "Mark: " + this.isDone();
        res += System.lineSeparator();
        return res;
    }
}
