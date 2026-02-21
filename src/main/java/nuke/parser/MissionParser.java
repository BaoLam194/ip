package nuke.parser;

import nuke.mission.Mission;
import nuke.mission.Operation;
import nuke.mission.Strike;
import nuke.mission.Task;

public class MissionParser {
    public static Mission parse(String block) {
        // Improve later
        String[] lines = block.split(System.lineSeparator());
        String type = "";
        String desc = "";
        String mark = "";
        String from = "";
        String to = "";
        String by = "";
        for (String line : lines) {
            if (line.startsWith("Type: ")) {
                type = line.substring(6);
            } else if (line.startsWith("Desc: ")) {
                desc = line.substring(6);
            } else if (line.startsWith("Mark: ")) {
                mark = line.substring(6);
            } else if (line.startsWith("From: ")) {
                from = line.substring(6);
            } else if (line.startsWith("To: ")) {
                to = line.substring(4);
            } else if (line.startsWith("By: ")) {
                by = line.substring(4);
            } else { // if not follow format, handle later
                return new Task("not format");
            }


        }
        switch (type) {
        case "todo": {
            if (!desc.isEmpty() && !mark.isEmpty()) {
                Task c = new Task(desc);
                if (mark.equals("true")) { // handle wrong case later
                    c.setDone();
                }
                return c;
            }
            break;
        }
        case "deadline": {
            if (!desc.isEmpty() && !mark.isEmpty() && !by.isEmpty()) {
                Strike c = new Strike(desc, by);
                if (mark.equals("true")) { // handle wrong case later
                    c.setDone();
                }
                return c;
            }
            break;
        }
        case "event": {
            if (!desc.isEmpty() && !mark.isEmpty() && !from.isEmpty() && !to.isEmpty()) {
                Operation c = new Operation(desc, from, to);
                if (mark.equals("true")) { // handle wrong case later
                    c.setDone();
                }
                return c;
            }
            break;
        }
        default: {
            return new Task("wrong type");
        }
        }
        return new Task("haha");
    }
}
