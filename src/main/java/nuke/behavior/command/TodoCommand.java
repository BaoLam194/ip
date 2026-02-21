package nuke.behavior.command;

import nuke.behavior.Comms;
import nuke.mission.MissionList;
import nuke.mission.Task;
import nuke.storage.Storage;

import java.io.IOException;

public class TodoCommand extends Command{
    private final String description;

    public TodoCommand(String description) {
        this.description = description;
    }

    @Override
    public void execute(MissionList missions, Comms ui, Storage storage) throws IOException {
        missions.add(new Task(description));
        ui.transmitOrder(String.format("\tReceive a pending task: " + description));
        storage.update(missions);
    }
}
