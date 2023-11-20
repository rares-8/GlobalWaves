package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;
import fileio.input.PlaylistInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import user.memory.UserMemory;
import utils.UpdateTimestamp;

import java.util.ArrayList;

public class Next {
    /**
     * @param username  - user that issued the command
     * @param memory    - database
     * @param timestamp - current timestamp
     * @return command result
     */
    public static JsonNode next(final String username, final UserMemory memory,
                                final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "next");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        if (!memory.getLoadedAudio().containsKey(username)) {
            commandResult.put("message", "Please load a source before skipping to the next track.");
            return commandResult;
        }

        if (memory.getLoadedAudio().get(username).getAudioType().equals("song")) {
            SongInput loadedSong = (SongInput) memory.getLoadedAudio().get(username);
            nextSong(commandResult, username, loadedSong, memory);
        } else if (memory.getLoadedAudio().get(username).getAudioType().equals("playlist")) {
            PlaylistInput loadedPlaylist = (PlaylistInput) memory.getLoadedAudio().get(username);
            nextPlaylist(commandResult, username, loadedPlaylist, memory, timestamp);
        } else {
            PodcastInput loadedPodcast = (PodcastInput) memory.getLoadedAudio().get(username);
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
                                    final PodcastInput loadedPodcast, final UserMemory memory) {
        Integer repeatMode = 0;
        if (memory.getIsRepeating().containsKey(username)) {
            repeatMode = memory.getIsRepeating().get(username);
        }

        int podcastIndex = memory.getLoadedPodcasts().get(username).indexOf(loadedPodcast);
        EpisodeInput loadedEpisode = memory.getLastEpisodes().get(username).get(podcastIndex);
        int episodeIndex = loadedPodcast.getEpisodes().indexOf(loadedEpisode);

        if (repeatMode == 0) {
            commandResult.put("message", "Please load a source before skipping to the next track.");
            memory.getLoadedAudio().remove(username);
            memory.getIsPaused().remove(username);
            memory.getIsShuffled().remove(username);
        } else {
            if (repeatMode == 1) {
                memory.getIsRepeating().remove(username);
            }
            commandResult.put("message", "Skipped to next track successfully. The current track is " + loadedEpisode.getName() + ".");
            memory.getIsPaused().remove(username);
            memory.getRemainingTime().put(username, loadedEpisode.getDuration());
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
                                     final PlaylistInput loadedPlaylist,
                                     final UserMemory memory, final Integer timestamp) {
        Integer repeatMode = 0;
        if (memory.getIsRepeating().containsKey(username)) {
            repeatMode = memory.getIsRepeating().get(username);
        }

        Integer currentIndex = memory.getCurrentIndex().get(username);
        ArrayList<Integer> indexes = memory.getCollectionIndexes().get(username);
        ArrayList<SongInput> songs = loadedPlaylist.getPlaylistSongs();
        if (repeatMode == 0) {
            if (indexes.indexOf(currentIndex) == indexes.size() - 1) {
                commandResult.put("message", "Please load a source before skipping to the next track.");
                memory.getLoadedAudio().remove(username);
                memory.getIsPaused().remove(username);
                memory.getIsShuffled().remove(username);
                return;
            }

            int indexOfCurrentSong = indexes.indexOf(currentIndex);
            currentIndex = indexes.get(indexOfCurrentSong + 1);
            SongInput currentSong = songs.get(currentIndex);
            memory.getCurrentIndex().put(username, currentIndex);
            memory.getRemainingTime().put(username, currentSong.getDuration());
            memory.getIsPaused().remove(username);
            commandResult.put("message", "Skipped to next track successfully. The current track is " + currentSong.getName() + ".");
        } else {
            if (repeatMode == 1) {
                currentIndex = indexes.get(0);
            }

            int indexOfCurrentSong;
            indexOfCurrentSong = indexes.indexOf(currentIndex);
            if (repeatMode == 1) {
                currentIndex = indexes.get(indexOfCurrentSong);
            } else {
                currentIndex = indexes.get(indexOfCurrentSong + 1);
            }
            SongInput currentSong = songs.get(currentIndex);
            memory.getCurrentIndex().put(username, currentIndex);
            memory.getRemainingTime().put(username, currentSong.getDuration());
            memory.getIsPaused().remove(username);
            commandResult.put("message", "Skipped to next track successfully. The current track is " + currentSong.getName() + ".");
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
                                 final SongInput loadedSong,
                                 final UserMemory memory) {
        Integer repeatMode = 0;
        if (memory.getIsRepeating().containsKey(username)) {
            repeatMode = memory.getIsRepeating().get(username);
        }

        if (repeatMode == 0) {
            commandResult.put("message", "Please load a source before skipping to the next track.");
            memory.getLoadedAudio().remove(username);
            memory.getIsPaused().remove(username);
            memory.getIsShuffled().remove(username);
        } else {
            commandResult.put("message", "Skipped to next track successfully. The current track is " + loadedSong.getName());
            memory.getIsPaused().remove(username);
            memory.getRemainingTime().put(username, loadedSong.getDuration());
            if (repeatMode == 1) {
                memory.getIsRepeating().remove(username);
            }
        }
    }
}
