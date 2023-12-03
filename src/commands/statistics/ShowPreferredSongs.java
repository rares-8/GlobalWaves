package commands.statistics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Song;
import user.memory.UserMemory;

public abstract class ShowPreferredSongs {
    /**
     *
     * @param username - user that issued the command
     * @param memory - database
     * @param timestamp - current timestamp
     * @return preferred songs
     */
    public static JsonNode showPreferred(final String username, final UserMemory memory,
                                         final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "showPreferredSongs");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        if (!memory.getLikedSongs().containsKey(username)) {
            commandResult.putArray("result");
            return commandResult;
        }

        ArrayNode likedSongs = commandResult.putArray("result");
        for (Song song : memory.getLikedSongs().get(username)) {
            likedSongs.add(song.getName());
        }

        return commandResult;
    }
}
