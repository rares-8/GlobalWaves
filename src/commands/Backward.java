package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import user.memory.UserMemory;

public class Backward {
    /**
     * @param username  - user that issued the command
     * @param memory    - database
     * @param timestamp - current timestamp
     * @return command result
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

        Integer repeatMode = 0;
        if (memory.getIsRepeating().containsKey(username)) {
            repeatMode = memory.getIsRepeating().get(username);
        }

        PodcastInput loadedPodcast = (PodcastInput) memory.getLoadedAudio().get(username);
        int podcastIndex = memory.getLoadedPodcasts().get(username).indexOf(loadedPodcast);
        EpisodeInput loadedEpisode = memory.getLastEpisodes().get(username).get(podcastIndex);
        int episodeIndex = loadedPodcast.getEpisodes().indexOf(loadedEpisode);

        int timePassed = loadedEpisode.getDuration() - memory.getEpisodeRemainingTime().get(username).get(podcastIndex);
        if (timePassed < 90) {
            memory.getEpisodeRemainingTime().get(username).set(podcastIndex, loadedEpisode.getDuration());
            commandResult.put("message", "Rewound successfully.");
            return commandResult;
        }

        commandResult.put("message", "Rewound successfully.");
        int remainingTime = memory.getEpisodeRemainingTime().get(username).get(podcastIndex);
        memory.getEpisodeRemainingTime().get(username).set(podcastIndex, remainingTime + 90);
        return commandResult;
    }
}
