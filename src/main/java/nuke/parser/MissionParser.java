package nuke.parser;

import nuke.exception.NukeException;
import nuke.mission.Mission;
import nuke.mission.Operation;
import nuke.mission.Strike;
import nuke.mission.Task;

/**
 * Parses stored mission history blocks into {@link Mission} objects.
 */
public class MissionParser {
    /**
     * Converts a formatted history block into a {@link Mission} object.
     *
     * <p>The method extracts mission attributes from labeled lines:
     * <ul>
     *     <li>Type: mission category (todo, deadline, event)</li>
     *     <li>Desc: mission description</li>
     *     <li>Mark: completion status (true/false)</li>
     *     <li>From/To/By: time-related attributes (if applicable)</li>
     * </ul>
     *
     * <p>Depending on the "Type" value, the method constructs and returns:
     * <ul>
     *     <li>{@link Task} for "todo"</li>
     *     <li>{@link Strike} for "deadline"</li>
     *     <li>{@link Operation} for "event"</li>
     * </ul>
     *
     * @param block The formatted mission history block as a string.
     * @return A reconstructed {@link Mission} object.
     * @throws NukeException If the block format is invalid or missing required fields.
     */
    public static Mission parse(String block) throws NukeException {
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
            } else {
                throw new NukeException("Null");
            }


        }
        switch (type) {
        case "todo": {
            if (!desc.isEmpty() && !mark.isEmpty()) {
                Task c = new Task(desc);
                if (mark.equals("true")) { // if it does not match, ignore
                    c.setDone();
                }
                return c;
            }
            break;
        }
        case "deadline": {
            if (!desc.isEmpty() && !mark.isEmpty() && !by.isEmpty()) {
                Strike c = new Strike(desc, by);
                if (mark.equals("true")) { // if it does not match, ignore
                    c.setDone();
                }
                return c;
            }
            break;
        }
        case "event": {
            if (!desc.isEmpty() && !mark.isEmpty() && !from.isEmpty() && !to.isEmpty()) {
                Operation c = new Operation(desc, from, to);
                if (mark.equals("true")) { // if it does not match, ignore
                    c.setDone();
                }
                return c;
            }
            break;
        }
        default: {
            throw new NukeException("Null");
        }
        }
        throw new NukeException("Null");
    }
}
