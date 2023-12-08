package commands.player;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Audio;
import entities.Episode;
import entities.Podcast;
import user.memory.UserMemory;

import java.util.ArrayList;
import java.util.Map;

public abstract class Load {
    /**
     * @param username  - user that issued the command
     * @param memory    - database
     * @param timestamp - current timestamp
     * @return load status
     */
    public static JsonNode load(final String username, final UserMemory memory,
                                final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "load");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        Map<String, Audio> map = memory.getCurrentSelect();
        if (!map.containsKey(username)) {
            commandResult.put("message", "Please select a source before attempting to load.");
            return commandResult;
        } else if (map.get(username).getAudioType().equals("playlist")
                && map.get(username).getPlaylistSongs().isEmpty()) {
            commandResult.put("message", "You can't load an empty audio collection!");
            return commandResult;
        } else if (map.get(username).getAudioType().equals("podcast")
                && map.get(username).getEpisodes().isEmpty()) {
            commandResult.put("message", "You can't load an empty audio collection!");
            return commandResult;
        }

        Audio currentSelect = memory.getCurrentSelect().get(username);
        memory.getIsShuffled().remove(username);
        if (currentSelect.getAudioType().equals("song")) {
            memory.getRemainingTime().put(username, currentSelect.getDuration());
            memory.getLoadedAudio().put(username, currentSelect);
            memory.getIsPaused().remove(username);
            commandResult.put("message", "Playback loaded successfully.");
        } else if (currentSelect.getAudioType().equals("playlist")
                || currentSelect.getAudioType().equals("album")) {
            memory.getIsPaused().remove(username);

            ArrayList<Integer> indexes = new ArrayList<>();
            int size = currentSelect.getPlaylistSongs().size();

            for (int iterator = 0; iterator < size; iterator++) {
                indexes.add(iterator);
            }

            Integer songDuration = currentSelect.getPlaylistSongs().get(0).getDuration();
            memory.getCollectionIndexes().put(username, indexes);
            memory.getCurrentIndex().put(username, 0);
            memory.getLoadedAudio().put(username, currentSelect);
            memory.getRemainingTime().put(username, songDuration);
            commandResult.put("message", "Playback loaded successfully.");
        } else {
            loadPodcast(username, memory, timestamp);
            commandResult.put("message", "Playback loaded successfully.");
        }
        memory.getCurrentSelect().remove(username);
        memory.getIsRepeating().remove(username);

        return commandResult;
    }

    /**
     * Loading a podcast is more complex, as it sometimes needs to load from the last
     * loaded episode
     *
     * @param username  - user that issued the command
     * @param memory    - database
     * @param timestamp - current timestamp
     */
    private static void loadPodcast(final String username, final UserMemory memory,
                                    final Integer timestamp) {
        Podcast selectedPodcast = (Podcast) memory.getCurrentSelect().get(username);
        memory.getLoadedAudio().put(username, selectedPodcast);
        memory.getIsPaused().remove(username);

        ArrayList<Integer> indexes = new ArrayList<>();
        int size = selectedPodcast.getEpisodes().size();
        for (int iterator = 0; iterator < size; iterator++) {
            indexes.add(iterator);
        }

        // if it is the first time this user loads a podcast, initialize the array lists
        if (!memory.getLastEpisodes().containsKey(username)) {
            memory.getLastEpisodes().put(username, new ArrayList<>());
        }

        if (!memory.getEpisodeRemainingTime().containsKey(username)) {
            memory.getEpisodeRemainingTime().put(username, new ArrayList<>());
        }

        if (!memory.getLoadedPodcasts().containsKey(username)) {
            memory.getLoadedPodcasts().put(username, new ArrayList<>());
            addNewPodcast(username, memory, selectedPodcast, indexes);
        }

        /* if the username is already in the hashmap, it means user already loaded
             a podcast, so first check if now a different podcast must be loaded */
        int different = 1;
        for (Podcast currentPodcast : memory.getLoadedPodcasts().get(username)) {
            if (currentPodcast == selectedPodcast) {
                different = 0;
                break;
            }
        }

        // if the podcasts are different, simply load the podcast from the beginning
        if (different == 1) {
            addNewPodcast(username, memory, selectedPodcast, indexes);
            return;
        }

        // if the podcasts are not different, get the last episode loaded from the podcast
        int podcastIndex = memory.getLoadedPodcasts().get(username).indexOf(selectedPodcast);
        Episode lastEpisode = memory.getLastEpisodes().get(username).get(podcastIndex);

        int episodeIndex = selectedPodcast.getEpisodes().indexOf(lastEpisode);
        memory.getCurrentIndex().put(username, episodeIndex);
        memory.getCollectionIndexes().put(username, indexes);
        memory.getRemainingTime().put(username,
                memory.getEpisodeRemainingTime().get(username).get(podcastIndex));
    }

    private static void addNewPodcast(final String username, final UserMemory memory,
                                      final Podcast selectedPodcast,
                                      final ArrayList<Integer> indexes) {
        memory.getLoadedPodcasts().get(username).add(selectedPodcast);
        Episode firstEpisode = selectedPodcast.getEpisodes().get(0);
        memory.getEpisodeRemainingTime().get(username).add(firstEpisode.getDuration());
        memory.getLastEpisodes().get(username).add(firstEpisode);
        memory.getCurrentIndex().put(username, 0);
        memory.getCollectionIndexes().put(username, indexes);
    }
}
