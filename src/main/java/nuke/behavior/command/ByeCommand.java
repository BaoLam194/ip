package nuke.behavior.command;

import nuke.behavior.Comms;
import nuke.mission.MissionList;
import nuke.storage.Storage;

import java.io.IOException;

public class ByeCommand extends Command{

    @Override
    public void execute(MissionList missions, Comms ui, Storage storage) throws IOException {
        ui.bye();
    }

    @Override
    public boolean isBye(){
        return true;
    }
}
