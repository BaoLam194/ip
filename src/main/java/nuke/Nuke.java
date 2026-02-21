package nuke;

import nuke.behavior.Comms;
import nuke.behavior.command.*;
import nuke.mission.*;
import nuke.exception.NukeException;
import nuke.parser.CommandParser;
import nuke.storage.Storage;

import java.io.IOException;

public class Nuke {
    private static MissionList missions;
    private static Storage militarySecret;
    private static Comms signalOfficer;
    private static boolean isActive = true;

    public static void main(String[] args) {
        militarySecret = new Storage("data/history.txt");
        signalOfficer = new Comms();

        // Retrieve history
        try {
            missions = new MissionList(militarySecret.load());
        } catch (IOException e) {
            // handle later
            missions = new MissionList();
        }

        signalOfficer.greet();
        // Hand over execution for handle()
        while (isActive) {
            try {
                String commandLine = signalOfficer.readCommand();
                signalOfficer.separate();
                Command c = CommandParser.parse(missions, commandLine);
                c.execute(missions, signalOfficer, militarySecret);
                isActive = !c.isBye();

            }catch(NukeException | IOException e ){
                signalOfficer.recoverError(e);
            }
            if (isActive) { // stop separate if it has to be terminated
                signalOfficer.separate();
            }
        }
    }
}
