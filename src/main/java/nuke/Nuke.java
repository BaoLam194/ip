package nuke;

import nuke.behavior.Comms;
import nuke.mission.*;
import nuke.exception.NukeException;
import nuke.storage.Storage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.FileWriter;

public class Nuke {
    private static ArrayList<Mission> missions = new ArrayList<>();
    private static Storage militarySecret;
    private static Comms signalOfficer;
    private static boolean isActive = true;
    private static boolean isChanged = false;

    private static void stopNuke() {
        isActive = false;
    }

    private static void addCommand(Mission c) {
        missions.add(c);
    }

    private static void receiveCommand(String command) {
        signalOfficer.separate();
        try { // error will only arise from this
            handleCommand(command);
            if(isChanged){ // need to writeback to history
                militarySecret.update(missions);
            }
        } catch (Exception e) {
            recoverError(e);
        }
        if (isActive) { // stop separate if it has to be terminated
            signalOfficer.separate();
        }

    }

    // categorize the command and analyze if it is correct before passing to next function
    private static void handleCommand(String commandLine) throws NukeException {
        // reset the routine
        isChanged = false;
        String[] parsedCommand = commandLine.split(" ");
        String command = parsedCommand[0];
        switch (command) {
        case "bye" -> {
            // Handle bye directly
            signalOfficer.bye();
            stopNuke();
            return;

        }
        case "list" -> {
            executeList();
            return;
        }
        case "mark", "unmark" -> {
            if (parsedCommand.length != 2) { // some explicit format handling
                throw new NukeException("Nuke doesn't know what to mark");
            }
            int index;
            try { // validate the index
                index = Integer.parseInt(parsedCommand[1]);
            } catch (Exception e) {
                throw new NukeException("Nuke can not mark not-number mission");
            }
            if (index > missions.size() || index <= 0) {//out-of-bound
                throw new NukeException("Sir! You access out-of-bound mission");
            }
            if (command.equals("mark")) {
                executeMark(index);
            } else {
                executeUnmark(index);
            }
            isChanged = true;
            return;
        }
        case "todo" -> { // to do command
            if (parsedCommand.length < 2) { // Not enough parameters
                throw new NukeException("Nuke needs a description!");
            }
            String description = String.join(" ", Arrays.copyOfRange(parsedCommand, 1, parsedCommand.length));
            executeTodo(description);
            isChanged = true;
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
                throw new NukeException("The strike precise date highlighted \"/by\" is lacking");
            }
            if (byIndex == 1) { // no description
                throw new NukeException("Nuke needs to know the strike description!");
            }
            if (byIndex == parsedCommand.length - 1) {
                throw new NukeException("Give a precise timing! Nuke can destroy everything");
            }
            String description = String.join(" ", Arrays.copyOfRange(parsedCommand, 1, byIndex));
            String by = String.join(" ", Arrays.copyOfRange(parsedCommand, byIndex + 1, parsedCommand.length));
            executeDeadline(description, by);
            isChanged = true;
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
            if (fromIndex == -1 || toIndex == -1) {
                throw new NukeException("Nuke has to know precise duration marked by \"from\" and \"to\"");
            }
            if (toIndex < fromIndex) {
                throw new NukeException("Please swap /from and /to order");
            }
            if (fromIndex == 1) {
                throw new NukeException("Nuke needs to know the operation description!");
            }
            if (toIndex == fromIndex + 1) {
                throw new NukeException("Nuke needs to know operation starting time!");
            }
            if (toIndex == parsedCommand.length - 1) {
                throw new NukeException("Give a precise end timing! Nuke can destroy everything!");
            }
            String description = String.join(" ", Arrays.copyOfRange(parsedCommand, 1, fromIndex));
            String from = String.join(" ", Arrays.copyOfRange(parsedCommand, fromIndex + 1, toIndex));
            String to = String.join(" ", Arrays.copyOfRange(parsedCommand, toIndex + 1, parsedCommand.length));
            executeEvent(description, from, to);
            isChanged = true;
            return;
        }
        case "delete" -> {
            if (parsedCommand.length != 2) { // some explicit format handling
                throw new NukeException("Nuke doesn't know what to delete");
            }
            int index;
            try { // validate the index
                index = Integer.parseInt(parsedCommand[1]);
            } catch (Exception e) {
                throw new NukeException("Nuke can not delete not-number mission");
            }
            if (index > missions.size() || index <= 0) {//out-of-bound
                throw new NukeException("Sir! You access out-of-bound mission");
            }
            executeDelete(index);
            isChanged = true;
            return;
        }
        case "find" -> {
            if (parsedCommand.length != 2) { // some explicit format handling
                throw new NukeException("Nuke doesn't know what to delete");
            }
            executeFind(parsedCommand[1]);
            return;
        }
        }
        //Not match any built-in command so reject
        throw new NukeException("Nuke is confused, unknown command");
    }

    // command implementation, should be no error here onwards
    private static void executeList() {
        if (!missions.isEmpty()) {
            signalOfficer.transmit(String.format("\tWe currently have %d missions:%n", missions.size()));
        } else {
            signalOfficer.transmit(String.format("\tThere is no mission yet!%n"));
        }

        for (int i = 0; i < missions.size(); i++) {
            signalOfficer.transmit(String.format("\t%d.%s%n", i + 1, missions.get(i).toString()));
        }
    }

    private static void executeMark(int index) {
        missions.get(index-1).setDone();
        signalOfficer.transmit(String.format("\tMark the mission: %s%n", missions.get(index-1).toString()));
    }

    private static void executeUnmark(int index) {
        missions.get(index-1).setUndone();
        signalOfficer.transmit(String.format("\tUnmark the mission: %s%n", missions.get(index-1).toString()));

    }

    private static void executeTodo(String description) {
        addCommand(new Task(description));
        signalOfficer.transmit(String.format("\tReceive a pending task: " + description +"%n"));
    }

    private static void executeDeadline(String description, String by) {
        addCommand(new Strike(description, by));
        signalOfficer.transmit(String.format("\tReceive a strike order: " + description + ", by " + by+"%n"));
    }

    private static void executeEvent(String description, String from, String to) {
        addCommand(new Operation(description, from, to));
        signalOfficer.transmit(String.format("\tReceive an operation: " + description + ", from " + from + ", to " + to +"%n"));
    }

    private static void executeDelete(int index) {
        Mission temp = missions.remove(index - 1);
        signalOfficer.transmit(String.format("\tDelete old mission: %s%n", temp.toString()));
    }

    private static void executeFind(String word) {
        int count = 0;
        for (Mission mission : missions) {
            if (mission.getDescription().contains(word)) {
                count++;
                signalOfficer.transmit(String.format("\t%d.%s%n", count, mission));
            }
        }
        if (count > 0) {
            signalOfficer.transmit(String.format("\tYou got %d match.%n", count));
            
        } else {
            signalOfficer.transmit(String.format("\tThere is no mission yet!%n"));
        }


    }

    private static void recoverError(Exception e){
        signalOfficer.transmit(String.format("\t" + e.getMessage() +"%n"));
    }

    public static void main(String[] args)  {
        militarySecret = new Storage("data/history.txt");
        signalOfficer = new Comms();
        Scanner in = new Scanner(System.in);

        // Retrieve history
        try{
            missions = militarySecret.load();
        } catch(IOException e){
            // handle later
        }

        signalOfficer.greet();
        // Hand over execution for handle()
        while (isActive) {
            String command = in.nextLine();
            receiveCommand(command);
        }
    }
}
