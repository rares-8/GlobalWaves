package user.memory;

import entities.Audio;
import entities.Episode;
import entities.Playlist;
import entities.Podcast;
import entities.Song;
import entities.pages.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class keeps all configurations made by users.
 * Instance variables:
 *
 * lastSearch - last search for every user
 * lastTimestamp - timestamp the last command was given at
 * currentSelect - currently selected audio file
 * userPlaylists - all playlists
 * publicPlaylists - all public playlists
 * followedPlaylists - playlists a user follows
 * loadedAudio - currently loaded audio file
 * remainingTime - remaining time for the loaded audio file
 * loadedPodcasts - all podcasts the user loaded and didn't finish
 * lastEpisodes - last episode loaded from every podcast
 * episodeRemainingTime - remaining time for the last loaded episode
 * collectionIndexes - arrays of indexes, used to decide which audio file to play next
 *                      Makes shuffle easier to implement
 * currentIndex - current index from collectionIndexes
 * isPaused - check if a user's player is paused
 * connectionStatus - online = 1 / offline = 0
 * currentPage - page a user is currently on
 */
public final class UserMemory {
    private final Map<String, ArrayList<Audio>> lastSearch;
    private final Map<String, Integer> lastTimestamp;
    private final Map<String, Audio> currentSelect;
    private final Map<String, ArrayList<Playlist>> userPlaylists;
    private final ArrayList<Playlist> publicPlaylists;
    private final Map<String, ArrayList<Audio>> followedPlaylists;
    private final Map<String, ArrayList<Song>> likedSongs;
    private final Map<String, Audio> loadedAudio;
    private final Map<String, Integer> remainingTime;
    private final Map<String, ArrayList<Podcast>> loadedPodcasts;
    private final Map<String, ArrayList<Episode>> lastEpisodes;
    private final Map<String, ArrayList<Integer>> episodeRemainingTime;
    private final Map<String, ArrayList<Integer>> collectionIndexes;
    private final Map<String, Integer> currentIndex;
    private final Map<String, Integer> isPaused;
    private final Map<String, Integer> isShuffled;
    private final Map<String, Integer> isRepeating;
    private final Map<String, Integer> connectionStatus;
    private final Map<String, Page> currentPage;

    private static UserMemory uniqueInstance = null;

    private UserMemory() {
        lastSearch = new HashMap<>();
        lastTimestamp = new HashMap<>();
        currentSelect = new HashMap<>();
        userPlaylists = new HashMap<>();
        publicPlaylists = new ArrayList<>();
        followedPlaylists = new HashMap<>();
        likedSongs = new HashMap<>();
        loadedAudio = new HashMap<>();
        remainingTime = new HashMap<>();
        loadedPodcasts = new HashMap<>();
        lastEpisodes = new HashMap<>();
        episodeRemainingTime = new HashMap<>();
        collectionIndexes = new HashMap<>();
        currentIndex = new HashMap<>();
        isPaused = new HashMap<>();
        isShuffled = new HashMap<>();
        isRepeating = new HashMap<>();
        connectionStatus = new HashMap<>();
        currentPage = new HashMap<>();
    }

    /**
     * Class is using Singleton pattern
     *
     * @return instance
     */
    public static UserMemory getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new UserMemory();
        }
        return uniqueInstance;
    }

    /**
     * Free the instance for the next test, or else it keeps some values
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
     * @return hashmap that contains last timestamp for all users
     * ( timestamp the last command for a user was given at)
     */
    public Map<String, Integer> getLastTimestamp() {
        return lastTimestamp;
    }

    /**
     * @return hashmap that keeps current selection for all users
     */
    public Map<String, Audio> getCurrentSelect() {
        return currentSelect;
    }

    /**
     * @return hashmap that keeps playlists for all users
     */
    public Map<String, ArrayList<Playlist>> getUserPlaylists() {
        return userPlaylists;
    }

    public ArrayList<Playlist> getPublicPlaylists() {
        return publicPlaylists;
    }

    public Map<String, ArrayList<Audio>> getFollowedPlaylists() {
        return followedPlaylists;
    }

    public Map<String, Audio> getLoadedAudio() {
        return loadedAudio;
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

    public Map<String, ArrayList<Song>> getLikedSongs() {
        return likedSongs;
    }

    public Map<String, Integer> getRemainingTime() {
        return remainingTime;
    }

    public Map<String, ArrayList<Podcast>> getLoadedPodcasts() {
        return loadedPodcasts;
    }

    public Map<String, ArrayList<Episode>> getLastEpisodes() {
        return lastEpisodes;
    }

    public Map<String, ArrayList<Integer>> getEpisodeRemainingTime() {
        return episodeRemainingTime;
    }

    public Map<String, Integer> getConnectionStatus() {
        return connectionStatus;
    }

    public Map<String, Page> getCurrentPage() {
        return currentPage;
    }
}
