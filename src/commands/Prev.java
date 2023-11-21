package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;
import fileio.input.PlaylistInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import user.memory.UserMemory;

import java.util.ArrayList;

public class Prev {
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
            commandResult.put("message", "Please load a source before returning to the previous track.");
            return commandResult;
        }

        if (memory.getLoadedAudio().get(username).getAudioType().equals("song")) {
            SongInput loadedSong = (SongInput) memory.getLoadedAudio().get(username);
            prevSong(commandResult, username, loadedSong, memory);
        } else if (memory.getLoadedAudio().get(username).getAudioType().equals("playlist")) {
            PlaylistInput loadedPlaylist = (PlaylistInput) memory.getLoadedAudio().get(username);
            prevPlaylist(commandResult, username, loadedPlaylist, memory);
        } else {
            PodcastInput loadedPodcast = (PodcastInput) memory.getLoadedAudio().get(username);
            prevPodcast(commandResult, username, loadedPodcast, memory);
        }
        return commandResult;
    }

    private static void prevPodcast(final ObjectNode commandResult, final String username,
                                    final PodcastInput loadedPodcast, final UserMemory memory) {
        Integer repeatMode = 0;
        if (memory.getIsRepeating().containsKey(username)) {
            repeatMode = memory.getIsRepeating().get(username);
        }

        int podcastIndex = memory.getLoadedPodcasts().get(username).indexOf(loadedPodcast);
        EpisodeInput loadedEpisode = memory.getLastEpisodes().get(username).get(podcastIndex);
        int episodeIndex = loadedPodcast.getEpisodes().indexOf(loadedEpisode);

        int timePassed = loadedEpisode.getDuration()
                - memory.getEpisodeRemainingTime().get(username).get(podcastIndex);

        if (timePassed >= 1 || episodeIndex == 0) {
            memory.getEpisodeRemainingTime().get(username).set(podcastIndex, loadedEpisode.getDuration());
            memory.getIsPaused().remove(username);
            commandResult.put("message", "Returned to previous track successfully. The current track is "
                    + loadedEpisode.getName() + ".");
            return;
        }

        if (repeatMode == 0) {
            episodeIndex--;
            loadedEpisode = memory.getLastEpisodes().get(username).get(podcastIndex);
            memory.getCurrentIndex().put(username, episodeIndex);
            memory.getEpisodeRemainingTime().get(username).set(podcastIndex, loadedEpisode.getDuration());
            memory.getLastEpisodes().get(username).set(podcastIndex, loadedEpisode);
            memory.getIsPaused().remove(username);
            commandResult.put("message", "Returned to previous track successfully. The current track is " + loadedEpisode.getName() + ".");
        } else {
            if (repeatMode == 1) {
                memory.getIsRepeating().remove(username);
            }
            memory.getEpisodeRemainingTime().get(username).set(podcastIndex, loadedEpisode.getDuration());
            memory.getIsPaused().remove(username);
            commandResult.put("message", "Returned to previous track successfully. The current track is " + loadedEpisode.getName() + ".");
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
    private static void prevPlaylist(final ObjectNode commandResult, final String username,
                                     final PlaylistInput loadedPlaylist, final UserMemory memory) {
        Integer repeatMode = 0;
        if (memory.getIsRepeating().containsKey(username)) {
            repeatMode = memory.getIsRepeating().get(username);
        }

        Integer currentIndex = memory.getCurrentIndex().get(username);
        ArrayList<Integer> indexes = memory.getCollectionIndexes().get(username);
        ArrayList<SongInput> songs = loadedPlaylist.getPlaylistSongs();

        int timePassed = songs.get(currentIndex).getDuration() - memory.getRemainingTime().get(username);
        if (timePassed >= 1 || indexes.indexOf(currentIndex) == 0) {
            memory.getRemainingTime().put(username, songs.get(currentIndex).getDuration());
            memory.getIsPaused().remove(username);
            commandResult.put("message", "Returned to previous track successfully. The current track is "
                    + songs.get(currentIndex).getName() + ".");
            return;
        }

        if (repeatMode == 0) {
            int indexOfCurrentSong = indexes.indexOf(currentIndex);
            currentIndex = indexes.get(indexOfCurrentSong - 1);
            SongInput currentSong = songs.get(currentIndex);
            memory.getCurrentIndex().put(username, currentIndex);
            memory.getRemainingTime().put(username, currentSong.getDuration());
            memory.getIsPaused().remove(username);
            commandResult.put("message", "Returned to previous track successfully. The current track is " + currentSong.getName() + ".");
        } else if (repeatMode == 1) {
            int indexOfCurrentSong;
            indexOfCurrentSong = indexes.indexOf(currentIndex);
            currentIndex = indexes.get(indexOfCurrentSong - 1);
            memory.getRemainingTime().put(username, songs.get(currentIndex).getDuration());
            memory.getCurrentIndex().put(username, currentIndex);
            memory.getIsPaused().remove(username);
            commandResult.put("message", "Returned to previous track successfully. The current track is " + songs.get(currentIndex).getName() + ".");
        } else if (repeatMode == 2) {
            int indexOfCurrentSong;
            indexOfCurrentSong = indexes.indexOf(currentIndex);
            currentIndex = indexes.get(indexOfCurrentSong);
            memory.getRemainingTime().put(username, songs.get(currentIndex).getDuration());
            memory.getIsPaused().remove(username);
            commandResult.put("message", "Returned to previous track successfully. The current track is " + songs.get(currentIndex).getName() + ".");
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
                                 final SongInput loadedSong, final UserMemory memory) {
        Integer repeatMode = 0;
        if (memory.getIsRepeating().containsKey(username)) {
            repeatMode = memory.getIsRepeating().get(username);
        }

        if (repeatMode == 0) {
            commandResult.put("message", "Please load a source before returning to the next track.");
            memory.getLoadedAudio().remove(username);
            memory.getIsPaused().remove(username);
            memory.getIsShuffled().remove(username);
        } else {
            commandResult.put("message", "Returned to previous track successfully. The current track is " + loadedSong.getName() + ".");
            memory.getIsPaused().remove(username);
            memory.getRemainingTime().put(username, loadedSong.getDuration());
            if (repeatMode == 1) {
                memory.getIsRepeating().remove(username);
            }
        }
    }
}
