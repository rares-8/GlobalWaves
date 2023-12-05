package commands.player;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Episode;
import entities.Playlist;
import entities.Podcast;
import entities.Song;
import user.memory.UserMemory;

import java.util.ArrayList;

public abstract class Prev {
    /**
     * @param username  - user that issued the command
     * @param memory    - database
     * @param timestamp - current timestamp
     * @return command result
     */
    public static JsonNode prev(final String username, final UserMemory memory,
                                final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "prev");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        if (!memory.getLoadedAudio().containsKey(username)) {
            commandResult.put("message",
                    "Please load a source before returning to the previous track.");
            return commandResult;
        }

        if (memory.getLoadedAudio().get(username).getAudioType().equals("song")) {
            Song loadedSong = (Song) memory.getLoadedAudio().get(username);
            prevSong(commandResult, username, loadedSong, memory);
        } else if (memory.getLoadedAudio().get(username).getAudioType().equals("playlist")) {
            Playlist loadedPlaylist = (Playlist) memory.getLoadedAudio().get(username);
            prevPlaylist(commandResult, username, loadedPlaylist, memory);
        } else if (memory.getLoadedAudio().get(username).getAudioType().equals("album")) {
            Playlist loadedPlaylist = (Playlist) memory.getLoadedAudio().get(username);
            prevPlaylist(commandResult, username, loadedPlaylist, memory);
        } else {
            Podcast loadedPodcast = (Podcast) memory.getLoadedAudio().get(username);
            prevPodcast(commandResult, username, loadedPodcast, memory);
        }
        return commandResult;
    }

    private static void prevPodcast(final ObjectNode commandResult, final String username,
                                    final Podcast loadedPodcast, final UserMemory memory) {
        int podcastIndex = memory.getLoadedPodcasts().get(username).indexOf(loadedPodcast);
        Integer repeatMode = 0;
        if (memory.getIsRepeating().containsKey(username)) {
            repeatMode = memory.getIsRepeating().get(username);
        }

        Episode loadedEpisode = memory.getLastEpisodes().get(username).get(podcastIndex);
        int episodeIndex = loadedPodcast.getEpisodes().indexOf(loadedEpisode);

        int timePassed = loadedEpisode.getDuration()
                - memory.getEpisodeRemainingTime().get(username).get(podcastIndex);

        // start episode again if more than a second passed
        if (timePassed >= 1 || episodeIndex == 0) {
            memory.getEpisodeRemainingTime().get(username).set(podcastIndex,
                    loadedEpisode.getDuration());
            memory.getIsPaused().remove(username);
            commandResult.put("message",
                    "Returned to previous track successfully. The current track is "
                    + loadedEpisode.getName() + ".");
            return;
        }

        if (repeatMode == 0) {
            // get last episode
            episodeIndex--;
            loadedEpisode = memory.getLastEpisodes().get(username).get(podcastIndex);
            memory.getCurrentIndex().put(username, episodeIndex);
            memory.getEpisodeRemainingTime().get(username).set(podcastIndex,
                    loadedEpisode.getDuration());
            memory.getLastEpisodes().get(username).set(podcastIndex, loadedEpisode);
        } else {
            // start episode again
            if (repeatMode == 1) {
                memory.getIsRepeating().remove(username);
            }
            memory.getEpisodeRemainingTime().get(username).set(podcastIndex,
                    loadedEpisode.getDuration());
        }
        memory.getIsPaused().remove(username);
        commandResult.put("message", "Returned to previous track successfully."
                + " The current track is " + loadedEpisode.getName() + ".");
    }

    /**
     * In case a playlist is loaded
     *
     * @param commandResult  - command result
     * @param username       - user that issued the command
     * @param loadedPlaylist - currently loaded playlist
     * @param memory         - database
     */
    private static void prevPlaylist(final ObjectNode commandResult, final String username,
                                     final Playlist loadedPlaylist, final UserMemory memory) {
        Integer repeatMode = 0;
        if (memory.getIsRepeating().containsKey(username)) {
            repeatMode = memory.getIsRepeating().get(username);
        }

        Integer currentIndex = memory.getCurrentIndex().get(username);
        ArrayList<Integer> indexes = memory.getCollectionIndexes().get(username);
        ArrayList<Song> songs = loadedPlaylist.getPlaylistSongs();

        int timePassed = songs.get(currentIndex).getDuration()
                - memory.getRemainingTime().get(username);
        if (timePassed >= 1 || indexes.indexOf(currentIndex) == 0) {
            memory.getRemainingTime().put(username, songs.get(currentIndex).getDuration());
            memory.getIsPaused().remove(username);
            commandResult.put("message",
                    "Returned to previous track successfully. The current track is "
                    + songs.get(currentIndex).getName() + ".");
            return;
        }

        if (repeatMode == 0) {
            int indexOfCurrentSong = indexes.indexOf(currentIndex);
            currentIndex = indexes.get(indexOfCurrentSong - 1);
            Song currentSong = songs.get(currentIndex);
            memory.getCurrentIndex().put(username, currentIndex);
            memory.getRemainingTime().put(username, currentSong.getDuration());
            memory.getIsPaused().remove(username);
            commandResult.put("message", "Returned to previous track successfully."
                    + " The current track is " + currentSong.getName() + ".");
        } else if (repeatMode == 1) {
            int indexOfCurrentSong;
            indexOfCurrentSong = indexes.indexOf(currentIndex);
            currentIndex = indexes.get(indexOfCurrentSong - 1);
            memory.getRemainingTime().put(username, songs.get(currentIndex).getDuration());
            memory.getCurrentIndex().put(username, currentIndex);
            memory.getIsPaused().remove(username);
            commandResult.put("message", "Returned to previous track successfully."
                    + " The current track is " + songs.get(currentIndex).getName() + ".");
        } else if (repeatMode == 2) {
            int indexOfCurrentSong;
            indexOfCurrentSong = indexes.indexOf(currentIndex);
            currentIndex = indexes.get(indexOfCurrentSong);
            memory.getRemainingTime().put(username, songs.get(currentIndex).getDuration());
            memory.getIsPaused().remove(username);
            commandResult.put("message", "Returned to previous track successfully."
                    + " The current track is " + songs.get(currentIndex).getName() + ".");
        }
    }

    /**
     * In case a song is loaded
     *
     * @param commandResult - command result
     * @param username      - user that issued the command
     * @param loadedSong    - currently loaded song
     * @param memory        - database
     */
    private static void prevSong(final ObjectNode commandResult, final String username,
                                 final Song loadedSong, final UserMemory memory) {
        Integer repeatMode = 0;
        if (memory.getIsRepeating().containsKey(username)) {
            repeatMode = memory.getIsRepeating().get(username);
        }

        if (repeatMode == 0) {
            commandResult.put("message",
                    "Please load a source before returning to the next track.");
            memory.getLoadedAudio().remove(username);
            memory.getIsPaused().remove(username);
            memory.getIsShuffled().remove(username);
        } else {
            commandResult.put("message", "Returned to previous track successfully."
                    + " The current track is " + loadedSong.getName() + ".");
            memory.getIsPaused().remove(username);
            memory.getRemainingTime().put(username, loadedSong.getDuration());
            if (repeatMode == 1) {
                memory.getIsRepeating().remove(username);
            }
        }
    }
}
