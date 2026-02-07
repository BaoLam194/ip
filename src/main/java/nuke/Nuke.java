package nuke;

import nuke.command.Command;
import nuke.command.Deadline;
import nuke.command.Event;
import nuke.command.Todo;

import java.util.Arrays;
import java.util.Scanner;

public class Nuke {
    private static Command[] commands = new Command[100];
    private static int numCommand = 0;
    private static boolean isActive = true;

    private static void stopNuke(){
        isActive = false;
    }

    private static void announce(String mode) {
        if (mode.equals("greet")) {
            System.out.println("\t===============================");
            System.out.println("\tHi! main.java.nuke.Nuke's waiting for your command!");
            System.out.println("\tWho do you want me to nuke today?");
            separate();
        } else {// default to exit
            System.out.println("\tKaboommm!");
            System.out.println("\tI have destroyed our current session!");
            System.out.println("\tSee you later!");
            System.out.println("\t===============================");
        }
    }

    private static void separate() {
        System.out.println("\t-------------------------------");
    }

    private static void addCommand(Command c) {
        commands[numCommand++] = c;
    }

    private static void receiveCommand(String command) {
        separate();
        handleCommand(command);
        if(isActive){
            separate();
        }
    }

    private static void handleCommand(String commandLine) {
        String[] parsedCommand = commandLine.split(" ");
        String command = parsedCommand[0];
        switch (command) {
        case "bye" ->{
             // Handle bye directly
            announce("exit");
            stopNuke();
            return;

        }
        case "list" -> {
            executeList();
            return;
        }
        case "mark", "unmark" -> {
            if (parsedCommand.length != 2) { // some explicit format handling
                System.out.println("Your command format is wrong. Try again!");
                return;
            }
            if (command.equals("mark")) {
                executeMark(parsedCommand[1]);
            } else {
                executeUnmark(parsedCommand[1]);
            }
            return;
        }
        case "todo" -> { // to do command
            String description = String.join(" ", Arrays.copyOfRange(parsedCommand, 1, parsedCommand.length));
            executeTodo(description);
            return;
        }
        case "deadline" -> { // deadline command
            int byIndex = -1;
            // Find "/by" keyword to create new main.java.nuke.command.Command
            for (int i = 1; i < parsedCommand.length; i++) {
                if (parsedCommand[i].equals("/by")) {
                    byIndex = i;
                    break;
                }
            }
            if (byIndex == -1) {
                System.out.println("Your command format is wrong. Try again!");
                return;
            }
            String description = String.join(" ", Arrays.copyOfRange(parsedCommand, 1, byIndex));
            String by = String.join(" ", Arrays.copyOfRange(parsedCommand, byIndex + 1, parsedCommand.length));
            executeDeadline(description, by);
            return;
        }
        case "event" -> { // deadline command
            int fromIndex = -1;
            int toIndex = -1;
            // Find "/from" and "/to" keyword to create new main.java.nuke.command.Command
            for (int i = 1; i < parsedCommand.length; i++) {
                if (parsedCommand[i].equals("/from")) {
                    fromIndex = i;
                    break;
                }
            }
            for (int i = 1; i < parsedCommand.length; i++) {
                if (parsedCommand[i].equals("/to")) {
                    toIndex = i;
                    break;
                }
            }
            if (fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
                System.out.println("Your command format is wrong. Try again!");
                return;
            }
            String description = String.join(" ", Arrays.copyOfRange(parsedCommand, 1, fromIndex));
            String from = String.join(" ", Arrays.copyOfRange(parsedCommand, fromIndex + 1, toIndex));
            String to = String.join(" ", Arrays.copyOfRange(parsedCommand, toIndex + 1, parsedCommand.length));
            executeEvent(description, from, to);
            return;
        }
        }
        addCommand(new Command(commandLine));
        System.out.printf("\tReceive command: %s%n", commandLine);
    }

    // main.java.nuke.command.Command implementation
    private static void executeList() {
        for (int i = 0; i < numCommand; i++) {
            System.out.printf("%d.%s%n", i + 1, commands[i].toString());
        }
    }

    private static void executeMark(String commandIndex) {
        try {
            int index = Integer.parseInt(commandIndex);
            if (index > numCommand) {
                System.out.println("\tYou mark the out-of-bound command");
                return;
            }
            commands[index - 1].setDone();
            System.out.printf("\tMark the command: %s%n", commands[index - 1].getDescription());
        } catch (Exception e) {
            System.out.println("\tYour command is wrong, please try again!");
        }
    }

    private static void executeUnmark(String commandIndex) {
        try {
            int index = Integer.parseInt(commandIndex);
            if (index > numCommand) {
                System.out.println("\tYou mark the out-of-bound command");
                return;
            }
            commands[index - 1].setUndone();
            System.out.printf("\tUnmark the command: %s%n", commands[index - 1].getDescription());
        } catch (Exception e) {
            System.out.println("\tYour command is wrong, please try again!");
        }
    }

    private static void executeTodo(String description) {
        addCommand(new Todo(description));
        System.out.println("\tReceive a pending command: " + description);
    }

    private static void executeDeadline(String description, String by) {
        addCommand(new Deadline(description, by));
        System.out.println("\tReceive a strike order: " + description + " by " + by);
    }

    private static void executeEvent(String description, String from, String to) {
        addCommand(new Event(description, from, to));
        System.out.println("\tReceive an operation " + description + " from " + from + " to " + to);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        announce("greet");
        // Hand over execution for handle()
        while (isActive) {
            String command = in.nextLine();
            receiveCommand(command);
        }
    }
}
