package nuke.behavior.command;

import nuke.behavior.Comms;
import nuke.mission.MissionList;
import nuke.storage.Storage;

import java.io.IOException;

public abstract class Command {

    // Execute the command after being parsed
    public abstract void execute(MissionList missions, Comms ui, Storage storage) throws IOException;

    public boolean isBye(){
        return false;
    }
}
