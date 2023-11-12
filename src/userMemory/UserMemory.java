package userMemory;

import fileio.input.Audio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserMemory {
    private Map<String, ArrayList<Audio>> lastSearch = new HashMap<>();
    private Map<String, Integer> lastTimestamp = new HashMap<>();

    private static UserMemory uniqueInstance = null;

    private UserMemory() {
    }

    public static UserMemory getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new UserMemory();
        }
        return uniqueInstance;
    }

    public Map<String, ArrayList<Audio>> getLastSearch() {
        return lastSearch;
    }

    public void setLastSearch(Map<String, ArrayList<Audio>> lastSearch) {
        this.lastSearch = lastSearch;
    }

    public Map<String, Integer> getLastTimestamp() {
        return lastTimestamp;
    }

    public void setLastTimestamp(Map<String, Integer> lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }
}
