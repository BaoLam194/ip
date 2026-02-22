package nuke.behavior.command;

import nuke.behavior.Comms;
import nuke.mission.MissionList;
import nuke.mission.Strike;
import nuke.mission.Task;
import nuke.storage.Storage;

import java.io.IOException;

/**
 * Represents a deadline command for Nuke chatbot
 */
public class DeadlineCommand extends Command {
    private final String description;
    private final String by;

    /**
     *
     * @param description String, the description of the mission
     * @param by String, the deadline time of the mission
     */
    public DeadlineCommand(String description, String by) {
        this.description = description;
        this.by = by;
    }
    /**
     * Create a Mission type Strike to the mission list and update the storage
     */
    public void execute(MissionList missions, Comms ui, Storage storage) throws IOException {
        missions.add(new Strike(description, by));
        ui.transmitOrder(String.format("\tReceive a strike order: " + description + ", by " + by));
        storage.update(missions);
    }
}
