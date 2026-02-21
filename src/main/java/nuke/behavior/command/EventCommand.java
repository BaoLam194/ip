package nuke.behavior.command;

import nuke.behavior.Comms;
import nuke.mission.MissionList;
import nuke.mission.Operation;
import nuke.mission.Strike;
import nuke.mission.Task;
import nuke.storage.Storage;

import java.io.IOException;

public class EventCommand extends Command{
    private final String description;
    private final String from;
    private final String to;

    public EventCommand(String description, String from, String to) {
        this.description = description;
        this.from = from;
        this.to = to;
    }

    public void execute(MissionList missions, Comms ui, Storage storage) throws IOException {
        missions.add(new Operation(description, from, to));
        ui.transmitOrder(String.format("\tReceive an operation: " + description + ", from " + from + ", to " + to));
        storage.update(missions);
    }
}
