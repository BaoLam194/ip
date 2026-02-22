package nuke.behavior.command;

import nuke.behavior.Comms;
import nuke.mission.MissionList;
import nuke.storage.Storage;

import java.io.IOException;
/**
 * Represents a mark command for Nuke chatbot
 */
public class MarkCommand extends Command{
    private final int index;

    /**
     *
     * @param index position of mission in mission list
     */
    public MarkCommand(int index) {
        this.index = index;
    }

    /**
     * Mark the mission, update ui and storage
     */
    @Override
    public void execute(MissionList missions, Comms ui, Storage storage) throws IOException {
        missions.get(index - 1).setDone();
        ui.transmitOrder(String.format("\tMark the mission: %s", missions.get(index - 1).toString()));
        storage.update(missions);
    }
}
