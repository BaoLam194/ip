package nuke.storage;

import nuke.mission.Mission;
import nuke.parser.MissionParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;


public class Storage {
    private String filePath;

    // suppose it will be hardcode to be data/history.txt
    public Storage(String filePath) {
        this.filePath = filePath;
    }

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
                missions.add(MissionParser.parse(tempBlock.toString()));
                tempBlock = new StringBuilder();
            }
        }
        if (!tempBlock.isEmpty()) {
            missions.add(MissionParser.parse(tempBlock.toString()));
        }
        return missions;
    }

    // update the history.txt file based on the current mission lists.
    public void update(ArrayList<Mission> missions) throws IOException {
        FileWriter fw = new FileWriter(this.filePath);
        fw.write(convertToHistory(missions));
        fw.close();
    }

    // convert receive missions into history
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
