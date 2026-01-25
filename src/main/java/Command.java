package main.java;

public class Command {
    private boolean isDone;
    private String name;
    // Constructor
    public Command( String name) {
        this.isDone = false;
        this.name = name;
    }
    // Getter and Setter
    public String getName() {
        return name;
    }
    public void setDone() {
        this.isDone = true;
    }
    public void setUndone() {
        this.isDone = false;
    }
    //Convert into format : [ ] command
    public String toString(){
        String res ="";
        res = ((this.isDone) ? "[X]" : "[ ]") + " " + name;
        return res;
    }
}
