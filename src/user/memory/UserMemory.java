package user.memory;

import fileio.input.Audio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class UserMemory {
    private final Map<String, ArrayList<Audio>> lastSearch = new HashMap<>();
    private final Map<String, Integer> lastTimestamp = new HashMap<>();
    private final Map<String, Audio> currentSelect = new HashMap<>();

    private static UserMemory uniqueInstance = null;

    private UserMemory() {
    }

    /**
     * Class is using Singleton pattern
     * @return instance
     */
    public static UserMemory getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new UserMemory();
        }
        return uniqueInstance;
    }

    /**
     * @return hashmap that contains last searches for users
     */
    public Map<String, ArrayList<Audio>> getLastSearch() {
        return lastSearch;
    }

    /**
     *
     * @return hashmap that contains last timestamp for all users
     *          ( timestamp the last command for a user was given at)
     */
    public Map<String, Integer> getLastTimestamp() {
        return lastTimestamp;
    }

    /**
     *
     * @return hashmap that keeps current selection for all users
     */
    public Map<String, Audio> getCurrentSelect() {
        return currentSelect;
    }
}
