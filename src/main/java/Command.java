package main.java;

public class Command {
    private boolean isDone;
    private String name;

    public Command( String name) {
        this.isDone = false;
        this.name = name;
    }

    public void setdone() {
        isDone = true;
    }

    public void setUndone() {
        isDone = false;
    }

    public String toString(){
        String res ="";
        res = (this.isDone) ? "[X]" : "[]" + " " + name;
        return res;
    }
}
