package nuke;

import nuke.mission.*;
import nuke.exception.NukeException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.FileWriter;

public class Nuke {
    private static ArrayList<Mission> missions = new ArrayList<>();
    private static boolean isActive = true;
    private static boolean isChanged = false;

    private static void stopNuke() {
        isActive = false;
    }

    private static void announce(String mode) {
        if (mode.equals("greet")) {
            System.out.println("\t===============================");
            System.out.println("\tHi! Nuke's waiting for your command!");
            System.out.println("\tWho do you want me to nuke today?");
            separate();
        } else {// default to exit
            System.out.println("\tKaboommm!");
            System.out.println("\tI have destroyed our current session!");
            System.out.println("\tSee you later!");
            System.out.println("\t=======================================");
        }
    }

    private static void separate() {
        System.out.println("\t---------------------------------------");
    }

    private static void addCommand(Mission c) {
        missions.add(c);
    }

    private static String convertToHistory(){

        String lines = "";
        for(Mission c : missions){
            if(!lines.isEmpty()) {
                lines += "-----";
                lines += System.lineSeparator();
            }

            lines += c.toHistory();
        }
        return lines;
    }
    // write to history
    private static void writeToHistory() throws IOException {
        FileWriter fw = new FileWriter("data/history.txt");
        fw.write(convertToHistory());
        fw.close();
    }
    // retrieve history every start of the app
    private static void retrieveHistory() throws IOException {
        Path data = Path.of("data");
        Boolean isExisted = Files.exists(data);
        if (!isExisted){
            Files.createDirectory(data);
            throw new IOException("No path now");
        }
        File f = new File("data/history.txt");
        Scanner s = new Scanner(f); // create a Scanner using the File as the source
        String tempBlock = "";
        while (s.hasNext()) {
            String line = s.nextLine();
            if(!line.equals("-----")) {
                tempBlock += line;
                tempBlock += System.lineSeparator();
            }
            else{ // parse the current line
                missions.add(MissionParser.parse(tempBlock));
                tempBlock = "";
            }
        }
        if(!tempBlock.isEmpty()){
            missions.add(MissionParser.parse(tempBlock));
        }
    }

    private static void receiveCommand(String command) {
        separate();
        try { // error will only arise from this
            handleCommand(command);
            if(isChanged){ // need to writeback to history
                writeToHistory();
            }
        } catch (Exception e) {
            recoverError(e);
        }
        if (isActive) { // stop separate if it has to be terminated
            separate();
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
            System.out.printf("\tYou order %d missions:%n", missions.size());
        } else {
            System.out.println("\tThere is no mission yet!");
        }

        for (int i = 0; i < missions.size(); i++) {
            System.out.printf("\t%d.%s%n", i + 1, missions.get(i).toString());
        }
    }

    private static void executeMark(int index) {
        missions.get(index-1).setDone();
        System.out.printf("\tMark the mission: %s%n", missions.get(index-1).toString());
    }

    private static void executeUnmark(int index) {
        missions.get(index-1).setUndone();
        System.out.printf("\tUnmark the mission: %s%n", missions.get(index-1).toString());

    }

    private static void executeTodo(String description) {
        addCommand(new Task(description));
        System.out.println("\tReceive a pending task: " + description);
    }

    private static void executeDeadline(String description, String by) {
        addCommand(new Strike(description, by));
        System.out.println("\tReceive a strike order: " + description + ", by " + by);
    }

    private static void executeEvent(String description, String from, String to) {
        addCommand(new Operation(description, from, to));
        System.out.println("\tReceive an operation: " + description + ", from " + from + ", to " + to);
    }

    private static void executeDelete(int index) {
        Mission temp = missions.remove(index - 1);
        System.out.printf("\tDelete old mission: %s%n", temp.toString());
    }

    private static void executeFind(String word) {
        int count = 0;
        for (Mission mission : missions) {
            if (mission.getDescription().contains(word)) {
                count++;
                System.out.printf("\t%d.%s%n", count, mission);
            }
        }
        if (count > 0) {
            System.out.printf("\tYou got %d match.%n", count);
        } else {
            System.out.println("\tThere is no mission yet!");
        }


    }

    private static void recoverError(Exception e){
        System.out.println("\t" + e.getMessage());
    }

    public static void main(String[] args)  {
        // Retrieve history
        try{
            retrieveHistory();
        } catch(IOException e){
            System.out.println("no file ?");
        }

        Scanner in = new Scanner(System.in);
        announce("greet");
        // Hand over execution for handle()
        while (isActive) {
            String command = in.nextLine();
            receiveCommand(command);
        }
    }
}
