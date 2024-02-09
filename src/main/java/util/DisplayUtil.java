package util;

import entities.User;

public class DisplayUtil {
    public static final int ACTION_COLUMN_NUMBER = 6;
    public static final int ID_COLUMN_NUMBER = 0;
    public static final int STATE_COLUMN_NUMBER = 5;

    public static String displayButtonText(User user) {
        return !user.isActive() ? "Disabled" : "Enabled";
    }

    public static String displayState(User user) {
        return !user.isActive() ? "Enable" : "Disable";
    }
}
