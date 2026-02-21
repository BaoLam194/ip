package nuke.behavior.command;

import nuke.behavior.Comms;
import nuke.mission.MissionList;
import nuke.mission.Task;
import nuke.storage.Storage;

import java.io.IOException;

public class MarkCommand extends Command{
    private final int index;

    public MarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(MissionList missions, Comms ui, Storage storage) throws IOException {
        missions.get(index - 1).setDone();
        ui.transmitOrder(String.format("\tMark the mission: %s", missions.get(index - 1).toString()));
        storage.update(missions);
    }
}
