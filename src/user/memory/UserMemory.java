package user.memory;

import fileio.input.Audio;
import fileio.input.EpisodeInput;
import fileio.input.PlaylistInput;
import fileio.input.PodcastInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *  This class keeps all configurations made by users.
 *  Instance variables:
 *
 *  lastSearch - last search for every user
 *  lastTimestamp - timestamp the last command was given at
 *  currentSelect - currently selected audio file
 *  userPlaylists - all playlists
 *  publicPlaylists - all public playlists
 *  followedPlaylists - playlists a user follows
 *  loadedAudio - currently loaded audio file
 *  remainingTime - remaining time for the loaded audio file
 *  lastPodcastEpisode - last podcast episode that a user was listening to
 *  lastPodcast - last podcast that a user was listening to
 *  episodeRemainingTime - remaining time of the last podcast episode
 *  collectionIndexes - arrays of indexes, used to decide which audio file to play next
 *                      Makes shuffle and load easier to implement
 *  currentIndex - current index from collectionIndexes
 *  isPaused - check if a user's player is paused
 *
 */
public final class UserMemory {
    private final Map<String, ArrayList<Audio>> lastSearch;
    private final Map<String, Integer> lastTimestamp;
    private final Map<String, Audio> currentSelect;
    private final Map<String, ArrayList<PlaylistInput>> userPlaylists;
    private final ArrayList<PlaylistInput> publicPlaylists;
    private final Map<String, ArrayList<Audio>> followedPlaylists;
    private final Map<String, Audio> loadedAudio;
    private final Map<String, Integer> remainingTime;
    private final Map<String, EpisodeInput> lastPodcastEpisode;
    private final Map<String, ArrayList<PodcastInput>> lastPodcast;
    private final Map<String, Integer> episodeRemainingTime;
    private final Map<String, ArrayList<Integer>> collectionIndexes;
    private final Map<String, Integer> currentIndex;
    private final Map<String, Integer> isPaused;
    private final Map<String, Integer> isShuffled;
    private final Map<String, Integer> isRepeating;

    private static UserMemory uniqueInstance = null;

    private UserMemory() {
        lastSearch = new HashMap<>();
        lastTimestamp = new HashMap<>();
        currentSelect = new HashMap<>();
        userPlaylists = new HashMap<>();
        publicPlaylists = new ArrayList<>();
        followedPlaylists = new HashMap<>();
        loadedAudio = new HashMap<>();
        remainingTime = new HashMap<>();
        lastPodcastEpisode = new HashMap<>();
        lastPodcast = new HashMap<>();
        episodeRemainingTime = new HashMap<>();
        collectionIndexes = new HashMap<>();
        currentIndex = new HashMap<>();
        isPaused = new HashMap<>();
        isShuffled = new HashMap<>();
        isRepeating = new HashMap<>();
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

    public Map<String, Audio> getLoadedAudio() {
        return loadedAudio;
    }

    public Map<String, Integer> getRemainingTime() {
        return remainingTime;
    }

    public Map<String, EpisodeInput> getLastPodcastEpisode() {
        return lastPodcastEpisode;
    }

    public Map<String, ArrayList<PodcastInput>> getLastPodcast() {
        return lastPodcast;
    }

    public Map<String, Integer> getEpisodeRemainingTime() {
        return episodeRemainingTime;
    }

    public Map<String, ArrayList<Integer>> getCollectionIndexes() {
        return collectionIndexes;
    }

    public Map<String, Integer> getCurrentIndex() {
        return currentIndex;
    }

    public Map<String, Integer> getIsPaused() {
        return isPaused;
    }

    public Map<String, Integer> getIsShuffled() {
        return isShuffled;
    }

    public Map<String, Integer> getIsRepeating() {
        return isRepeating;
    }
}
