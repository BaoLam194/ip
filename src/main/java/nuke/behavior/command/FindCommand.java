package nuke.behavior.command;

import nuke.behavior.Comms;
import nuke.mission.Mission;
import nuke.mission.MissionList;
import nuke.storage.Storage;

import java.io.IOException;

public class FindCommand extends Command{
    private final String word;

    public FindCommand(String word) {
        this.word = word;
    }

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
