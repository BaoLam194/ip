import main.java.Command;

import java.util.Scanner;

public class Nuke {
    private static Command[] commands = new Command[100];
    private static int numOfCommand = 0;
    private static void greet(){
        System.out.println("\t===============================");
        System.out.println("\tHi! Nuke's waiting for your command!");
        System.out.println("\tWho do you want me to nuke today?");
        separate();
    }

    private static void exit(){
        separate();
        System.out.println("\tKaboommm!");
        System.out.println("\tI have destroyed our current session!");
        System.out.println("\tSee you later!");
        System.out.println("\t===============================");
    }

    private static void separate(){
        System.out.println("\t-------------------------------");
    }

    private static boolean receiveCommand(String command){
        if(command.equals("bye")){ // Handle bye directly
            exit();
            return false;
        }
        separate();
        handleCommand(command);
        separate();
        return true;
    }

    private static void handleCommand(String command) {
        if(command.equals("list")){
            for(int i = 0; i < numOfCommand;i++){
                System.out.printf("%d.%s%n", i, commands[i].toString());
            }
            return;
        }
        commands[numOfCommand++] = new Command(command);
        System.out.printf("\tAdded: %s%n", command);
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        greet();
        // Hand over execution for handle()
        while(receiveCommand(in.nextLine())){
        }
    }
}
