package nuke.storage;

import nuke.exception.NukeException;
import nuke.mission.Mission;
import nuke.parser.MissionParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles persistent storage of mission history for the Nuke chatbot.
 *
 * <p>The {@code Storage} class is responsible for reading mission data from
 * a file during application startup and writing updated mission data back
 * to the file when changes occur.
 */
public class Storage {
    private String filePath;

    /**
     * @param filePath String, to identify which the path of the history file
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads mission history from the data file at application startup.
     *
     * <p>If the "data" directory does not exist, it will be created and
     * an empty mission list will be returned.
     *
     * <p>The history file is read block by block, where each mission
     * is separated by a delimiter line ("-----"). Each block is parsed
     * into a {@link Mission} using {@link MissionParser}.
     *
     * @return an {@link ArrayList} containing all previously saved missions;
     *         returns an empty list if no history exists
     * @throws IOException if an error occurs while accessing or reading the file
     */
    // retrieve history every start of the app
    public ArrayList<Mission> load() throws IOException {
        Path data = Path.of("data");
        boolean isExisted = Files.exists(data);
        if (!isExisted) {
            Files.createDirectory(data);
            return new ArrayList<>();
        }
        File f = new File("data/history.txt");
        Scanner s = new Scanner(f); // create a Scanner using the File as the source

        //Construct each block of history and parse it into mission
        StringBuilder tempBlock = new StringBuilder();
        ArrayList<Mission> missions = new ArrayList<>();
        while (s.hasNext()) {
            String line = s.nextLine();
            if (!line.equals("-----")) {
                tempBlock.append(line);
                tempBlock.append(System.lineSeparator());
            } else {
                try{
                    missions.add(MissionParser.parse(tempBlock.toString()));
                } catch (NukeException ignored) {

                }
                tempBlock = new StringBuilder();
            }
        }
        if (!tempBlock.isEmpty()) {
            try{
                missions.add(MissionParser.parse(tempBlock.toString()));
            } catch (NukeException ignored) {

            }
        }
        return missions;
    }

    // update the history.txt file based on the current mission lists.
    public void update(ArrayList<Mission> missions) throws IOException {
        FileWriter fw = new FileWriter(this.filePath);
        fw.write(convertToHistory(missions));
        fw.close();
    }

    /**
     *
     * @param missions ArrayList, the current mission record to be converted
     * @return an {@link String} that is in correct formal for history
     */
    private static String convertToHistory(ArrayList<Mission> missions) {

        StringBuilder lines = new StringBuilder();
        for (Mission c : missions) {
            if (!lines.isEmpty()) {
                lines.append("-----");
                lines.append(System.lineSeparator());
            }

            lines.append(c.toHistory());
        }
        return lines.toString();
    }
}
