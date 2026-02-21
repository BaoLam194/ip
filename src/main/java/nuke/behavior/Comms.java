package nuke.behavior;

public class Comms {
    public Comms() {
    }
    public void greet() {
        System.out.println("\t===============================");
        System.out.println("\tHi! Nuke's waiting for your command!");
        System.out.println("\tWho do you want me to nuke today?");
        separate();

    }

    public void bye() {
        System.out.println("\tKaboommm!");
        System.out.println("\tI have destroyed our current session!");
        System.out.println("\tSee you later!");
        System.out.println("\t=======================================");
    }

    public void separate() {
        System.out.println("\t---------------------------------------");
    }

    public void transmit(String order){
        System.out.print(order);
    }
}
