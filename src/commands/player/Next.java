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

public abstract class Next {
    /**Go to the next audio file
     * @param username  - user that issued the command
     * @param memory    - database
     * @param timestamp - current timestamp
     * @return next status
     */
    public static JsonNode next(final String username, final UserMemory memory,
                                final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "next");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        if (!memory.getLoadedAudio().containsKey(username)) {
            commandResult.put("message",
                    "Please load a source before skipping to the next track.");
            return commandResult;
        }

        if (memory.getLoadedAudio().get(username).getAudioType().equals("song")) {
            Song loadedSong = (Song) memory.getLoadedAudio().get(username);
            nextSong(commandResult, username, loadedSong, memory);
        } else if (memory.getLoadedAudio().get(username).getAudioType().equals("playlist")) {
            Playlist loadedPlaylist = (Playlist) memory.getLoadedAudio().get(username);
            nextPlaylist(commandResult, username, loadedPlaylist, memory);
        } else if (memory.getLoadedAudio().get(username).getAudioType().equals("album")) {
            Playlist loadedPlaylist = (Playlist) memory.getLoadedAudio().get(username);
            nextPlaylist(commandResult, username, loadedPlaylist, memory);
        } else {
            Podcast loadedPodcast = (Podcast) memory.getLoadedAudio().get(username);
            nextPodcast(commandResult, username, loadedPodcast, memory);
        }

        return commandResult;
    }

    /**
     * In case a podcast is loaded
     *
     * @param commandResult - command result
     * @param username      - user that issued the command
     * @param loadedPodcast - currently loaded podcast
     * @param memory        - database
     */
    private static void nextPodcast(final ObjectNode commandResult, final String username,
                                    final Podcast loadedPodcast, final UserMemory memory) {
        int podcastIndex = memory.getLoadedPodcasts().get(username).indexOf(loadedPodcast);
        Episode loadedEpisode = memory.getLastEpisodes().get(username).get(podcastIndex);
        int episodeIndex = loadedPodcast.getEpisodes().indexOf(loadedEpisode);

        Integer repeatMode = 0;
        if (memory.getIsRepeating().containsKey(username)) {
            repeatMode = memory.getIsRepeating().get(username);
        }

        if (repeatMode == 0) {
            // if this is the last episode, remove podcast
            if (episodeIndex == loadedPodcast.getEpisodes().size() - 1) {
                commandResult.put("message",
                        "Please load a source before skipping to the next track.");
                memory.getLoadedAudio().remove(username);
                memory.getEpisodeRemainingTime().get(username).remove(podcastIndex);
                memory.getLastEpisodes().get(username).remove(podcastIndex);
                memory.getLoadedPodcasts().get(username).remove(podcastIndex);
                memory.getIsPaused().remove(username);
                memory.getIsShuffled().remove(username);
                return;
            }
            episodeIndex++;
            Episode nextEpisode = loadedPodcast.getEpisodes().get(episodeIndex + 1);
            memory.getLastEpisodes().get(username).set(podcastIndex, nextEpisode);
            memory.getCurrentIndex().put(username, episodeIndex);
            memory.getEpisodeRemainingTime().get(username).set(podcastIndex,
                    nextEpisode.getDuration());
            memory.getIsPaused().remove(username);
            commandResult.put("message", "Skipped to next track successfully."
                    + " The current track is " + nextEpisode.getName() + ".");
        } else {
            // start episode again
            commandResult.put("message", "Skipped to next track successfully."
                    + " The current track is " + loadedEpisode.getName() + ".");
            memory.getEpisodeRemainingTime().get(username).set(podcastIndex,
                    loadedEpisode.getDuration());
            memory.getIsPaused().remove(username);
            if (repeatMode == 1) {
                memory.getIsRepeating().remove(username);
            }
        }
    }

    /**
     * In case a playlist is loaded
     *
     * @param commandResult  - command result
     * @param username       - user that issued the command
     * @param loadedPlaylist - currently loaded playlist
     * @param memory         - database
     */
    private static void nextPlaylist(final ObjectNode commandResult, final String username,
                                     final Playlist loadedPlaylist,
                                     final UserMemory memory) {
        Integer currentIndex = memory.getCurrentIndex().get(username);
        ArrayList<Integer> indexes = memory.getCollectionIndexes().get(username);
        ArrayList<Song> songs = loadedPlaylist.getPlaylistSongs();
        Integer repeatMode = 0;
        if (memory.getIsRepeating().containsKey(username)) {
            repeatMode = memory.getIsRepeating().get(username);
        }

        if (repeatMode == 0) {
            if (indexes.indexOf(currentIndex) == indexes.size() - 1) {
                commandResult.put("message",
                        "Please load a source before skipping to the next track.");
                memory.getLoadedAudio().remove(username);
                memory.getIsPaused().remove(username);
                memory.getIsShuffled().remove(username);
                return;
            }

            int indexOfCurrentSong = indexes.indexOf(currentIndex);
            currentIndex = indexes.get(indexOfCurrentSong + 1);
            Song currentSong = songs.get(currentIndex);
            memory.getCurrentIndex().put(username, currentIndex);
            memory.getRemainingTime().put(username, currentSong.getDuration());
            memory.getIsPaused().remove(username);
            commandResult.put("message", "Skipped to next track successfully."
                    + " The current track is " + currentSong.getName() + ".");
        } else {
            int last = 0;
            if (indexes.indexOf(currentIndex) == indexes.size() - 1) {
                last = 1;
            }

            int indexOfCurrentSong;
            if (repeatMode == 1 && last == 1) {
                // if this is the last song, start over
                currentIndex = indexes.get(0);
                indexOfCurrentSong = indexes.indexOf(currentIndex);
                currentIndex = indexes.get(indexOfCurrentSong);
            } else if (repeatMode == 1) {
                // skip to next song
                indexOfCurrentSong = indexes.indexOf(currentIndex);
                currentIndex = indexes.get(indexOfCurrentSong + 1);
            } else if (repeatMode == 2) {
                // start current song again
                indexOfCurrentSong = indexes.indexOf(currentIndex);
                currentIndex = indexes.get(indexOfCurrentSong);
            }
            Song currentSong = songs.get(currentIndex);
            memory.getCurrentIndex().put(username, currentIndex);
            memory.getRemainingTime().put(username, currentSong.getDuration());
            memory.getIsPaused().remove(username);
            commandResult.put("message", "Skipped to next track successfully."
                    + " The current track is " + currentSong.getName() + ".");
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
    private static void nextSong(final ObjectNode commandResult, final String username,
                                 final Song loadedSong,
                                 final UserMemory memory) {
        Integer repeatMode = 0;
        if (memory.getIsRepeating().containsKey(username)) {
            repeatMode = memory.getIsRepeating().get(username);
        }

        if (repeatMode == 0) {
            commandResult.put("message",
                    "Please load a source before skipping to the next track.");
            memory.getLoadedAudio().remove(username);
            memory.getIsPaused().remove(username);
            memory.getIsShuffled().remove(username);
        } else {
            commandResult.put("message", "Skipped to next track successfully."
                    + " The current track is " + loadedSong.getName() + ".");
            memory.getIsPaused().remove(username);
            memory.getRemainingTime().put(username, loadedSong.getDuration());
            if (repeatMode == 1) {
                memory.getIsRepeating().remove(username);
            }
        }
    }
}
