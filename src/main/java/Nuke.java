import main.java.Command;

import java.util.Scanner;

public class Nuke {
    private static Command[] commands = new Command[100];
    private static int numOfCommand = 0;
    private static void announce(String mode){
        if(mode.equals("greet")){
            System.out.println("\t===============================");
            System.out.println("\tHi! Nuke's waiting for your command!");
            System.out.println("\tWho do you want me to nuke today?");
            separate();
        }
        else{// default to exit
            separate();
            System.out.println("\tKaboommm!");
            System.out.println("\tI have destroyed our current session!");
            System.out.println("\tSee you later!");
            System.out.println("\t===============================");
        }
    }

    private static void separate(){
        System.out.println("\t-------------------------------");
    }

    private static boolean receiveCommand(String command){

        if(command.equals("bye")){ // Handle bye directly
            announce("exit");
            return false;
        }
        separate();
        handleCommand(command);
        separate();
        return true;
    }

    private static void handleCommand(String commandLine) {
        String[] parsedComment = commandLine.split(" ");
        if(parsedComment[0].equals("list")){// list command
            for(int i = 0; i < numOfCommand;i++){
                System.out.printf("%d.%s%n", i+1, commands[i].toString());
            }
            return;
        }
        if(parsedComment[0].equals("mark") || parsedComment[0].equals("unmark")){ //mark and unmark command
            try{
                int chosenCommand = Integer.parseInt(parsedComment[1]);
                if(parsedComment[0].equals("mark")){
                    commands[chosenCommand -1].setDone();
                    System.out.printf("\tMark the command: %s%n", commands[chosenCommand - 1].getName());
                }
                else{
                    commands[chosenCommand -1].setUndone();
                    System.out.printf("\tUnmark the command: %s%n", commands[chosenCommand - 1].getName());
                }

            }
            catch (Exception e){
                System.out.println("\tYour command is wrong, please try again!");
            }
            return;
        }
        commands[numOfCommand++] = new Command(commandLine);
        System.out.printf("\tReceive command: %s%n", commandLine);
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        announce("greet");
        // Hand over execution for handle()
        while(receiveCommand(in.nextLine())){
        }
    }
}
