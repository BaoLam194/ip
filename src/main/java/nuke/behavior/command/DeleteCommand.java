package nuke.behavior.command;

import nuke.behavior.Comms;
import nuke.mission.Mission;
import nuke.mission.MissionList;
import nuke.storage.Storage;

import java.io.IOException;
/**
 * Represents a delete command for Nuke chatbot
 */
public class DeleteCommand extends Command{
    private final int index;

    public DeleteCommand(int index) {
        this.index = index;
    }

    /**
     * Remove the mission out of current mission list and update the storage
     */
    @Override
    public void execute(MissionList missions, Comms ui, Storage storage) throws IOException {
        Mission temp = missions.remove(index - 1);
        ui.transmitOrder(String.format("\tDelete old mission: %s", temp.toString()));
        storage.update(missions);
    }
}
