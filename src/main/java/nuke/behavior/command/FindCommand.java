package nuke.behavior.command;

import nuke.behavior.Comms;
import nuke.mission.Mission;
import nuke.mission.MissionList;
import nuke.storage.Storage;

import java.io.IOException;
/**
 * Represents a find command for Nuke chatbot
 */
public class FindCommand extends Command{
    private final String word;

    /**
     *
     * @param word the substring that find command needs to match
     */
    public FindCommand(String word) {
        this.word = word;
    }

    /**
     * Return all mission that has substring {@code word} variable of this command.
     */
    @Override
    public void execute(MissionList missions, Comms ui, Storage storage) throws IOException {
        int count = 0;
        for (Mission mission : missions) {
            if (mission.getDescription().contains(word)) {
                count++;
                ui.transmitOrder(String.format("\t%d.%s", count, mission));
            }
        }
        if (count > 0) {
            ui.transmitOrder(String.format("\tYou got %d match.", count));

        } else {
            ui.transmitOrder("\tThere is no match yet!");
        }
    }
}
