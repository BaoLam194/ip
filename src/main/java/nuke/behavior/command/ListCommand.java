package nuke.behavior.command;

import nuke.behavior.Comms;
import nuke.mission.MissionList;
import nuke.mission.Task;
import nuke.storage.Storage;

import java.io.IOException;

public class ListCommand extends Command{

    @Override
    public void execute(MissionList missions, Comms ui, Storage storage) throws IOException {
        if (!missions.isEmpty()) {
            ui.transmitOrder(String.format("\tWe currently have %d missions:", missions.size()));
        } else {
            ui.transmitOrder("\tThere is no mission yet!");
        }

        for (int i = 0; i < missions.size(); i++) {
            ui.transmitOrder(String.format("\t%d.%s", i + 1, missions.get(i).toString()));
        }
    }
}
