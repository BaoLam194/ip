package nuke;

import nuke.behavior.Comms;
import nuke.behavior.command.*;
import nuke.mission.*;
import nuke.exception.NukeException;
import nuke.parser.CommandParser;
import nuke.storage.Storage;

import java.io.IOException;
/**
 * Main entry point of the Nuke chatbot application
 *
 * <p>The {@code Nuke} class is responsible for bootstrapping the system by
 * initializing core components such as:
 * <ul>
 *     <li>{@link Storage} for persistent mission history management</li>
 *     <li>{@link MissionList} for in-memory mission tracking</li>
 *     <li>{@link Comms} for user communication</li>
 * </ul>
 *
 */
public class Nuke {
    private static MissionList missions;
    private static Storage militarySecret;
    private static Comms signalOfficer;
    private static boolean isActive = true;
    /**
     * Starting point of Nuke chatbot.
     *
     * <p>Upon startup, the chatbot attempts to load previously saved mission data
     * from persistent storage. If loading fails due to an {@link IOException},
     * an empty {@link MissionList} is initialized instead.
     *
     * <p>The chatbot then enters a REPL loop where it:
     * <ol>
     *     <li>Reads user input</li>
     *     <li>Parses the input into a {@link Command} using {@link CommandParser}</li>
     *     <li>Executes the command</li>
     *     <li>Handles any {@link NukeException} or {@link IOException} that occurs</li>
     * </ol>
     *
     * <p>The loop continues until a command signals termination (e.g., a "bye" command),
     * after which the chatbot exits.
     *
     * @param args Command-line arguments passed during program execution (not used).
     */
    public static void main(String[] args) {
        militarySecret = new Storage("data/history.txt");
        signalOfficer = new Comms();

        // Retrieve history
        try {
            missions = new MissionList(militarySecret.load());
        } catch (IOException e) {
            // Default empty fallback
            missions = new MissionList();
        }
        signalOfficer.greet();

        // REPL loop
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
