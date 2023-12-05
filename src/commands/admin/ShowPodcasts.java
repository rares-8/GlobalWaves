package commands.admin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.*;

public abstract class ShowPodcasts {
    /**
     * @param username  - artist name
     * @param library - library containing songs, users, podcasts
     * @param timestamp - current timestamp
     * @return show podcasts status
     */
    public static JsonNode showPodcasts(final String username, final Library library,
                                   final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "showPodcasts");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        Host host = null;
        for (User user : library.getUsers()) {
            if (user.getUsername().equals(username)) {
                host = (Host) user;
            }
        }

        ArrayNode podcasts = commandResult.putArray("result");
        for (Podcast podcast : host.getPodcasts()) {
            ObjectNode currentPodcast = mapper.createObjectNode();
            currentPodcast.put("name", podcast.getName());
            ArrayNode podcastEpisodes = currentPodcast.putArray("songs");
            for (Episode episode : podcast.getEpisodes()) {
                podcastEpisodes.add(episode.getName());
            }
            podcasts.add(currentPodcast);
        }
        return commandResult;
    }
}
