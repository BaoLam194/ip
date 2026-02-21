package nuke.behavior.command;

import nuke.behavior.Comms;
import nuke.mission.Mission;
import nuke.mission.MissionList;
import nuke.storage.Storage;

import java.io.IOException;

public class DeleteCommand extends Command{
    private final int index;

    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(MissionList missions, Comms ui, Storage storage) throws IOException {
        Mission temp = missions.remove(index - 1);
        ui.transmitOrder(String.format("\tDelete old mission: %s", temp.toString()));
        storage.update(missions);
    }
}
