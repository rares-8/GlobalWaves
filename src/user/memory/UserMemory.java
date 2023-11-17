package user.memory;

import fileio.input.Audio;
import fileio.input.PlaylistInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class UserMemory {
    private final Map<String, ArrayList<Audio>> lastSearch;
    private final Map<String, Integer> lastTimestamp;
    private final Map<String, Audio> currentSelect;
    private final Map<String, ArrayList<PlaylistInput>> userPlaylists;
    private final ArrayList<PlaylistInput> publicPlaylists;
    private final Map<String, ArrayList<Audio>> followedPlaylists;

    private static UserMemory uniqueInstance = null;

    private UserMemory() {
        lastSearch = new HashMap<>();
        lastTimestamp = new HashMap<>();
        currentSelect = new HashMap<>();
        userPlaylists = new HashMap<>();
        publicPlaylists = new ArrayList<>();
        followedPlaylists = new HashMap<>();
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

    /*
        Free the instance for the next test, or else it keeps some values
     */
    public void removeInstance() {
        uniqueInstance = null;
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

    /**
     *
     * @return hashmap that keeps playlists for all users
     */
    public Map<String, ArrayList<PlaylistInput>> getUserPlaylists() {
        return userPlaylists;
    }

    public ArrayList<PlaylistInput> getPublicPlaylists() {
        return publicPlaylists;
    }

    public Map<String, ArrayList<Audio>> getFollowedPlaylists() {
        return followedPlaylists;
    }
}
