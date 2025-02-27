package commands.player;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Episode;
import entities.Podcast;
import user.memory.UserMemory;

import static utils.Constants.FORWARD_BACKWARD_TIME;

public abstract class Forward {
    /**Go forward in a podcast
     * @param username  - user that issued the command
     * @param memory    - database
     * @param timestamp - current timestamp
     * @return forward status
     */
    public static JsonNode forward(final String username, final UserMemory memory,
                                   final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "forward");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        if (!memory.getLoadedAudio().containsKey(username)) {
            commandResult.put("message", "Please load a source before attempting to forward.");
            return commandResult;
        } else if (!memory.getLoadedAudio().get(username).getAudioType().equals("podcast")) {
            commandResult.put("message", "The loaded source is not a podcast.");
            return commandResult;
        }

        Integer repeatMode = 0;
        if (memory.getIsRepeating().containsKey(username)) {
            repeatMode = memory.getIsRepeating().get(username);
        }

        Podcast loadedPodcast = (Podcast) memory.getLoadedAudio().get(username);
        int podcastIndex = memory.getLoadedPodcasts().get(username).indexOf(loadedPodcast);
        Episode loadedEpisode = memory.getLastEpisodes().get(username).get(podcastIndex);
        int episodeIndex = loadedPodcast.getEpisodes().indexOf(loadedEpisode);

        int timeRemaining = memory.getEpisodeRemainingTime().get(username).get(podcastIndex);
        // check if next episode has to be loaded or not
        if (timeRemaining > FORWARD_BACKWARD_TIME) {
            memory.getEpisodeRemainingTime().get(username).set(podcastIndex,
                    timeRemaining - FORWARD_BACKWARD_TIME);
            commandResult.put("message", "Skipped forward successfully.");
            return commandResult;
        }

        commandResult.put("message", "Skipped forward successfully.");
        // if repeat is not activated, skip to next song
        if (repeatMode == 0) {
            if (episodeIndex == loadedPodcast.getEpisodes().size() - 1) {
                memory.getLoadedAudio().remove(username);
                memory.getLoadedPodcasts().get(username).remove(podcastIndex);
                memory.getEpisodeRemainingTime().get(username).remove(podcastIndex);
                memory.getLastEpisodes().get(username).remove(podcastIndex);
                return commandResult;
            }
            episodeIndex++;
            loadedEpisode = memory.getLastEpisodes().get(username).get(podcastIndex);
            memory.getCurrentIndex().put(username, episodeIndex);
            memory.getEpisodeRemainingTime().get(username).set(podcastIndex,
                    loadedEpisode.getDuration());
            memory.getLastEpisodes().get(username).set(podcastIndex, loadedEpisode);
        } else {
            // if repeat is activated, start current episode again
            if (repeatMode == 1) {
                memory.getIsPaused().remove(username);
            }
            memory.getEpisodeRemainingTime().get(username).set(podcastIndex,
                    loadedEpisode.getDuration());
        }

        return commandResult;
    }
}
