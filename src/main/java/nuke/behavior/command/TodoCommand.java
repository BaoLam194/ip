package nuke.behavior.command;

import nuke.behavior.Comms;
import nuke.mission.MissionList;
import nuke.mission.Task;
import nuke.storage.Storage;

import java.io.IOException;
/**
 * Represents a todo command for Nuke chatbot
 */
public class TodoCommand extends Command{
    private final String description;

    /**
     *
     * @param description String, the description of the mission
     */
    public TodoCommand(String description) {
        this.description = description;
    }

    /**
     * Create a Mission type Task to the mission list and update the storage, ui
     */
    @Override
    public void execute(MissionList missions, Comms ui, Storage storage) throws IOException {
        missions.add(new Task(description));
        ui.transmitOrder(String.format("\tReceive a pending task: " + description));
        storage.update(missions);
    }
}
