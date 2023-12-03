package utils;

import entities.Episode;
import entities.Playlist;
import entities.Song;
import entities.Podcast;
import user.memory.UserMemory;

import java.util.ArrayList;

public abstract class UpdatePlayer {
    /**
     * Update the player state for a user
     *
     * @param username  - user that issued the command
     * @param timestamp - current timestamp
     * @param memory    - database
     */
    public static void updatePlayer(final String username,
                                    final Integer timestamp, final UserMemory memory) {
        if (!memory.getLoadedAudio().containsKey(username)) {
            UpdateTimestamp.updateTimestamp(username, timestamp, memory);
            return;
        }
        if (memory.getLoadedAudio().get(username).getAudioType().equals("song")) {
            updateForSong(username, timestamp, memory);
        } else if (memory.getLoadedAudio().get(username).getAudioType().equals("playlist")) {
            updateForPlaylist(username, timestamp, memory);
        } else {
            updateForPodcast(username, timestamp, memory);
        }
    }

    /**
     * Update the player when a podcast is loaded. Similar to when a playlist is loaded
     *
     * @param username  - user that issued the command
     * @param timestamp - current timestamp
     * @param memory    - database
     */
    private static void updateForPodcast(final String username, final Integer timestamp,
                                         final UserMemory memory) {
        Integer currentEpIndex = memory.getCurrentIndex().get(username);
        Podcast currentPodcast = (Podcast) memory.getLoadedAudio().get(username);
        ArrayList<Integer> indexes = memory.getCollectionIndexes().get(username);

        Episode currentEpisode = currentPodcast.getEpisodes().get(currentEpIndex);
        int podcastIndex = memory.getLoadedPodcasts().get(username).indexOf(currentPodcast);

        UpdateRemainingTimeEpisode.updateEp(username, timestamp, currentEpisode,
                podcastIndex, memory);
        UpdateTimestamp.updateTimestamp(username, timestamp, memory);

        Integer repeatingMode = 0;
        if (memory.getIsRepeating().containsKey(username)) {
            repeatingMode = memory.getIsRepeating().get(username);
        }

        if (repeatingMode != 0) {
            repeatPodcast(currentEpisode, podcastIndex, memory, username,
                    timestamp, repeatingMode);
        }

        Integer remainingTime = memory.getEpisodeRemainingTime().get(username).get(podcastIndex);
        while (remainingTime <= 0) {
            // if remaining time is <= 0 and it is the last episode, remove podcast
            if (indexes.indexOf(currentEpIndex) == indexes.size() - 1) {
                memory.getLoadedAudio().remove(username);
                memory.getIsPaused().put(username, 1);
                memory.getEpisodeRemainingTime().get(username).remove(podcastIndex);
                memory.getLoadedPodcasts().get(username).remove(currentPodcast);
                memory.getLastEpisodes().get(username).remove(podcastIndex);
                break;
            }

            // go to next podcast episode
            int indexOfCurrentEpisode = indexes.indexOf(currentEpIndex);
            currentEpIndex = indexes.get(indexOfCurrentEpisode + 1);
            currentEpisode = currentPodcast.getEpisodes().get(currentEpIndex);
            memory.getCurrentIndex().put(username, currentEpIndex);
            memory.getEpisodeRemainingTime().get(username).set(podcastIndex,
                    currentEpisode.getDuration() + remainingTime);
            remainingTime = memory.getEpisodeRemainingTime().get(username).get(podcastIndex);
        }
    }

    /**
     * Method used to repeat the podcast loaded in the player.
     *
     * @param currentEpisode - episode currently running
     * @param podcastIndex   - index for loadedPodcasts hashmap
     * @param memory         - database
     * @param username       - user that issued the command
     * @param timestamp      - current timestamp
     */
    private static void repeatPodcast(final Episode currentEpisode, final int podcastIndex,
                                      final UserMemory memory, final String username,
                                      final Integer timestamp, final Integer repeatMode) {
        int remainingTime, timePassed, timestampFinished;
        if (repeatMode == 1) {
            // repeat podcast episode once
            remainingTime = memory.getEpisodeRemainingTime().get(username).get(podcastIndex);
            if (remainingTime <= 0) {
                timestampFinished = timestamp + remainingTime;
                timePassed = timestamp - timestampFinished;
                remainingTime = currentEpisode.getDuration() - timePassed;
                memory.getEpisodeRemainingTime().get(username).set(podcastIndex, remainingTime);
                memory.getIsRepeating().remove(username);
            }
            return;
        }

        remainingTime = memory.getEpisodeRemainingTime().get(username).get(podcastIndex);
        while (remainingTime <= 0) {
            // repeat until remaining time is positive
            timestampFinished = timestamp + remainingTime;
            timePassed = timestamp - timestampFinished;
            remainingTime = currentEpisode.getDuration() - timePassed;
            memory.getEpisodeRemainingTime().get(username).set(podcastIndex, remainingTime);
        }
    }

    /**
     * Update the player when a playlist is loaded
     *
     * @param username  - user that issued the command
     * @param timestamp - current timestamp
     * @param memory    - database for users
     */
    private static void updateForPlaylist(final String username, final Integer timestamp,
                                          final UserMemory memory) {
        Integer currentIndex = memory.getCurrentIndex().get(username);
        Playlist currentPlaylist = (Playlist) memory.getLoadedAudio().get(username);
        ArrayList<Integer> indexes = memory.getCollectionIndexes().get(username);
        Song currentSong = currentPlaylist.getPlaylistSongs().get(currentIndex);

        UpdateRemainingTime.updateRemainingTime(username, timestamp, currentSong, memory);
        UpdateTimestamp.updateTimestamp(username, timestamp, memory);

        Integer repeatMode = 0;
        if (memory.getIsRepeating().containsKey(username)) {
            repeatMode = memory.getIsRepeating().get(username);
        }

        Integer remainingTime = memory.getRemainingTime().get(username);

        // repeat current song until remaining time is positive
        while (remainingTime <= 0 && repeatMode == 2) {
            int timestampFinished = timestamp + remainingTime;
            int timePassed = timestamp - timestampFinished;
            remainingTime = currentSong.getDuration() - timePassed;
            memory.getRemainingTime().put(username, remainingTime);
        }

        while (remainingTime <= 0) {
            int repeat = 0;
            // if this is the last song, and there is no repeat, remove playlist from player
            if (indexes.indexOf(currentIndex) == indexes.size() - 1) {
                if (repeatMode == 1) {
                    currentIndex = indexes.get(0);
                    repeat = 1;
                } else {
                    memory.getLoadedAudio().remove(username);
                    memory.getIsPaused().put(username, 1);
                    memory.getRemainingTime().remove(username);
                    memory.getIsShuffled().remove(username);
                    break;
                }
            }
            // go to next song
            int indexOfCurrentSong;
            indexOfCurrentSong = indexes.indexOf(currentIndex);
            if (repeat == 1) {
                currentIndex = indexes.get(indexOfCurrentSong);
            } else {
                currentIndex = indexes.get(indexOfCurrentSong + 1);
            }
            currentSong = currentPlaylist.getPlaylistSongs().get(currentIndex);
            memory.getCurrentIndex().put(username, currentIndex);
            memory.getRemainingTime().put(username, currentSong.getDuration() + remainingTime);

            remainingTime = memory.getRemainingTime().get(username);
        }
    }

    /**
     * Update the player when a song is loaded
     *
     * @param username - user that issued the command
     * @param timestamp - current timestamp
     * @param memory - database
     */
    private static void updateForSong(final String username, final Integer timestamp,
                                      final UserMemory memory) {
        UpdateRemainingTime.updateRemainingTime(username, timestamp,
                memory.getLoadedAudio().get(username), memory);
        UpdateTimestamp.updateTimestamp(username, timestamp, memory);

        Integer repeatMode = 0;
        if (memory.getIsRepeating().containsKey(username)) {
            repeatMode = memory.getIsRepeating().get(username);
        }

        if (repeatMode != 0) {
            repeatLoadedSong(username, timestamp, repeatMode, memory);
            return;
        }

        if (memory.getRemainingTime().get(username) <= 0) {
            memory.getLoadedAudio().remove(username);
            memory.getIsPaused().put(username, 1);
            memory.getRemainingTime().remove(username);
        }
    }

    /**
     * Method used to repeat the song loaded in the player.
     *
     * @param username   - user that issued the command
     * @param timestamp  - current timestamp
     * @param repeatMode - no repeat / repeat once / repeat infinite
     * @param memory     - database
     */
    private static void repeatLoadedSong(final String username, final Integer timestamp,
                                         final Integer repeatMode, final UserMemory memory) {
        Song song = (Song) memory.getLoadedAudio().get(username);
        int remainingTime, timePassed, timestampFinished;
        if (repeatMode == 1) {
            remainingTime = memory.getRemainingTime().get(username);
            if (remainingTime <= 0) {
                timestampFinished = timestamp + remainingTime;
                timePassed = timestamp - timestampFinished;
                remainingTime = song.getDuration() - timePassed;
                memory.getRemainingTime().put(username, remainingTime);
                memory.getIsRepeating().remove(username);
            }
            if (remainingTime <= 0) {
                memory.getLoadedAudio().remove(username);
                memory.getIsPaused().put(username, 1);
                memory.getRemainingTime().remove(username);
            }
            return;
        }

        remainingTime = memory.getRemainingTime().get(username);
        while (remainingTime <= 0) {
            timestampFinished = timestamp + remainingTime;
            timePassed = timestamp - timestampFinished;
            remainingTime = song.getDuration() - timePassed;
            memory.getRemainingTime().put(username, remainingTime);
        }
    }

}
