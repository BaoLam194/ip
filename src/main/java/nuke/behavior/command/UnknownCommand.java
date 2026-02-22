package nuke.behavior.command;

import nuke.behavior.Comms;
import nuke.mission.MissionList;
import nuke.storage.Storage;

import java.io.IOException;
/**
 * Represents a fallback command for Nuke chatbot when no known command is matched
 */
public class UnknownCommand extends Command{
    /**
     * Update ui
     */
    @Override
    public void execute(MissionList missions, Comms ui, Storage storage) throws IOException {
        ui.transmitOrder("\tNuke is confused, unknown command");
    }
}
