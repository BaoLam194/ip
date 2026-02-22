package nuke.behavior.command;

import nuke.behavior.Comms;
import nuke.mission.MissionList;
import nuke.storage.Storage;

import java.io.IOException;
/**
 * Represents a bye command for Nuke chatbot
 */

public class ByeCommand extends Command{
    /**
     *Exits the Nuke program
     */
    @Override
    public void execute(MissionList missions, Comms ui, Storage storage) throws IOException {
        ui.bye();
    }

    /**
     *
     * @return {@code true} for this command
     */
    @Override
    public boolean isBye(){
        return true;
    }
}
