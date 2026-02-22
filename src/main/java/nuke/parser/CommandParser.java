package nuke.parser;

import nuke.behavior.command.*;
import nuke.exception.NukeException;
import nuke.mission.MissionList;

import java.io.IOException;
import java.util.Arrays;

/**
 * Convert the user command line into the {@link Command} object to being executed
 */
public class CommandParser {
    /**
     * Take in the user input and the current mission list record to convert into Nuke-related command
     * @param missions MissionList,
     * @param commandLine String, the user-input string
     * @return an {@link Command} object that contains all information about the command user requires.
     * @throws NukeException will be thrown if the format of the command line is wrong
     */
    public static Command parse(MissionList missions, String commandLine) throws NukeException {
        // reset the routine
        String[] parsedCommand = commandLine.split(" ");
        String command = parsedCommand[0];
        Command c;
        switch (command) {
        case "bye" -> {
            // Handle bye directly
            return new ByeCommand();

        }
        case "list" -> {
            return new ListCommand();
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
                return new MarkCommand(index);
            } else {
                return new UnmarkCommand(index);
            }
        }
        case "todo" -> { // to do command
            if (parsedCommand.length < 2) { // Not enough parameters
                throw new NukeException("Nuke needs a description!");
            }
            String description = String.join(" ", Arrays.copyOfRange(parsedCommand, 1, parsedCommand.length));
            return new TodoCommand(description);
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
            return new DeadlineCommand(description, by);
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
            return new EventCommand(description, from, to);
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
            return new DeleteCommand(index);
        }
        case "find" -> {
            if (parsedCommand.length != 2) { // some explicit format handling
                throw new NukeException("Nuke doesn't know what to delete");
            }
            return new FindCommand(parsedCommand[1]);
        }
        }
        //Not match any built-in command so reject
        return new UnknownCommand();
    }
}
