package nuke.behavior.command;

import nuke.behavior.Comms;
import nuke.mission.MissionList;
import nuke.storage.Storage;

import java.io.IOException;
/**
 * Represents an abstract command in the Nuke chatbot.
 *
 * <p>All specific commands (e.g., {@code AddCommand}, {@code DeleteCommand}, {@code ByeCommand})
 * extend this class. It defines the basic interface for executing a command
 * and optionally signaling termination of the program.
 *
 * <p>Subclasses must implement {@link #execute(MissionList, Comms, Storage)} to define
 * the command's behavior.
 */
public abstract class Command {

    /**
     * Executes the command using the given mission list, user interface, and storage.
     *
     * @param missions The current list of missions to operate on.
     * @param ui The communication handler for interacting with the user.
     * @param storage The storage handler for saving/loading missions.
     * @throws IOException If an I/O error occurs during storage operations.
     */
    public abstract void execute(MissionList missions, Comms ui, Storage storage) throws IOException;

    /**
     *
     * @return {@code true} if it is bye command, {@code false} otherwise.
     */
    public boolean isBye(){
        return false;
    }
}
