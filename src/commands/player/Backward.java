package commands.player;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Episode;
import entities.Podcast;
import user.memory.UserMemory;

import static utils.Constants.FORWARD_BACKWARD_TIME;

public abstract class Backward {
    /**
     * @param username  - user that issued the command
     * @param memory    - database
     * @param timestamp - current timestamp
     * @return backward status
     */
    public static JsonNode backward(final String username, final UserMemory memory,
                                    final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "backward");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        if (!memory.getLoadedAudio().containsKey(username)) {
            commandResult.put("message", "Please select a source before rewinding.");
            return commandResult;
        } else if (!memory.getLoadedAudio().get(username).getAudioType().equals("podcast")) {
            commandResult.put("message", "The loaded source is not a podcast.");
            return commandResult;
        }

        Podcast loadedPodcast = (Podcast) memory.getLoadedAudio().get(username);
        int podcastIndex = memory.getLoadedPodcasts().get(username).indexOf(loadedPodcast);
        Episode loadedEpisode = memory.getLastEpisodes().get(username).get(podcastIndex);
        int episodeIndex = loadedPodcast.getEpisodes().indexOf(loadedEpisode);

        int timePassed = loadedEpisode.getDuration()
                - memory.getEpisodeRemainingTime().get(username).get(podcastIndex);
        // check if episode has to start over or just go back 90 seconds
        if (timePassed < FORWARD_BACKWARD_TIME) {
            memory.getEpisodeRemainingTime().get(username).set(podcastIndex,
                    loadedEpisode.getDuration());
            commandResult.put("message", "Rewound successfully.");
            return commandResult;
        }

        commandResult.put("message", "Rewound successfully.");
        int remainingTime = memory.getEpisodeRemainingTime().get(username).get(podcastIndex);
        memory.getEpisodeRemainingTime().get(username).set(podcastIndex,
                remainingTime + FORWARD_BACKWARD_TIME);
        return commandResult;
    }
}
