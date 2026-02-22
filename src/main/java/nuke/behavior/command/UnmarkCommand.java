package nuke.behavior.command;

import nuke.behavior.Comms;
import nuke.mission.MissionList;
import nuke.mission.Task;
import nuke.storage.Storage;

import java.io.IOException;
/**
 * Represents a unmark command for Nuke chatbot
 */
public class UnmarkCommand extends Command{
    private final int index;

    /**
     *
     * @param index position of mission in mission list
     */
    public UnmarkCommand(int index) {
        this.index = index;
    }

    /**
     * Unmark the mission, update ui and storage
     */
    @Override
    public void execute(MissionList missions, Comms ui, Storage storage) throws IOException {
        missions.get(index - 1).setUndone();
        ui.transmitOrder(String.format("\tUnmark the mission: %s", missions.get(index - 1).toString()));
        storage.update(missions);
    }
}
