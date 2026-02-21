package nuke.behavior.command;

import nuke.behavior.Comms;
import nuke.mission.MissionList;
import nuke.storage.Storage;

import java.io.IOException;

public class UnknownCommand extends Command{
    @Override
    public void execute(MissionList missions, Comms ui, Storage storage) throws IOException {
        ui.transmitOrder("\tNuke is confused, unknown command");
    }
}
