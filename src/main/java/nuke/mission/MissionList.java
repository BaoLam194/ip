package nuke.mission;

import java.util.ArrayList;

/**
 * A wrapper class to wrap {@code ArrayList<Mission>}
 */
public class MissionList extends ArrayList<Mission> {
    public MissionList() {
        super();
    }
    public MissionList(ArrayList<Mission> missions) {
        super(missions);
    }

}