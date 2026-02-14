package nuke;

import nuke.command.*;
import nuke.exception.NukeException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.FileWriter;
//
public class Nuke {
    private static ArrayList<Command> commands = new ArrayList<>();
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

    private static void addCommand(Command c) {
        commands.add(c);
    }

    private static String convertToHistory(){

        String lines = "";
        for(Command c : commands){
            if(!lines.isEmpty()) {
                lines += "-----";
                lines += System.lineSeparator();
            }

            lines += c.toHistory();
        }
        return lines;
    }

    private static void writeToHistory() throws IOException {
        FileWriter fw = new FileWriter("data/history.txt");
        fw.write(convertToHistory());
        fw.close();
    }

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
                commands.add(CommandParser.parse(tempBlock));
                tempBlock = "";
            }
        }
        if(!tempBlock.isEmpty()){
            commands.add(CommandParser.parse(tempBlock));
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
                throw new NukeException("Nuke can not mark not-number command");
            }
            if (index > commands.size() || index <= 0) {//out-of-bound
                throw new NukeException("Sir! You access out-of-bound command");
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
                throw new NukeException("Nuke can not delete not-number command");
            }
            if (index > commands.size() || index <= 0) {//out-of-bound
                throw new NukeException("Sir! You access out-of-bound command");
            }
            executeDelete(index);
            isChanged = true;
            return;
        }
        }
        //Not match any built-in command so reject
        throw new NukeException("Nuke is confused, unknown command");
    }

    // command implementation, should be no error here onwards
    private static void executeList() {
        if (!commands.isEmpty()) {
            System.out.printf("\tYou order %d commands:%n", commands.size());
        } else {
            System.out.println("\tThere is no command yet!");
        }

        for (int i = 0; i < commands.size(); i++) {
            System.out.printf("\t%d.%s%n", i + 1, commands.get(i).toString());
        }
    }

    private static void executeMark(int index) {
        commands.get(index-1).setDone();
        System.out.printf("\tMark the command: %s%n", commands.get(index-1).toString());
    }

    private static void executeUnmark(int index) {
        commands.get(index-1).setUndone();
        System.out.printf("\tUnmark the command: %s%n", commands.get(index-1).toString());

    }

    private static void executeTodo(String description) {
        addCommand(new Todo(description));
        System.out.println("\tReceive a pending command: " + description);
    }

    private static void executeDeadline(String description, String by) {
        addCommand(new Deadline(description, by));
        System.out.println("\tReceive a strike order: " + description + ", by " + by);
    }

    private static void executeEvent(String description, String from, String to) {
        addCommand(new Event(description, from, to));
        System.out.println("\tReceive an operation: " + description + ", from " + from + ", to " + to);
    }

    private static void executeDelete(int index) {
        Command temp = commands.remove(index - 1);
        System.out.printf("\tDelete old command: %s%n", temp.toString());
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
