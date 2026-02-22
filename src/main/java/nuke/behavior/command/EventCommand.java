package nuke.behavior.command;

import nuke.behavior.Comms;
import nuke.mission.MissionList;
import nuke.mission.Operation;
import nuke.storage.Storage;

import java.io.IOException;
/**
 * Represents a event command for Nuke chatbot
 */
public class EventCommand extends Command{
    private final String description;
    private final String from;
    private final String to;

    /**
     *
     * @param description String, the description of the mission
     * @param from String, the starting time that the mission should begin
     * @param to String, the ending time that the mission must be done
     */
    public EventCommand(String description, String from, String to) {
        this.description = description;
        this.from = from;
        this.to = to;
    }
    /**
     * Create a Mission type Operation to the mission list and update the storage
     */
    public void execute(MissionList missions, Comms ui, Storage storage) throws IOException {
        missions.add(new Operation(description, from, to));
        ui.transmitOrder(String.format("\tReceive an operation: " + description + ", from " + from + ", to " + to));
        storage.update(missions);
    }
}
