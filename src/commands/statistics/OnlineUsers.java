package commands.statistics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.User;
import user.memory.UserMemory;

public abstract class OnlineUsers {
    /**
     * @param memory    - database
     * @param timestamp - current timestamp
     * @param library - library containing every user, song and podcast
     */
    public static JsonNode getOnlineUsers(final UserMemory memory,
                                         final Integer timestamp, final Library library) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "getOnlineUsers");
        commandResult.put("timestamp", timestamp);

        ArrayNode onlineUsers = commandResult.putArray("result");
        for (User user : library.getUsers()) {
            if (memory.getConnectionStatus().containsKey(user.getUsername())) {
                onlineUsers.add(user.getUsername());
            }
        }

        return commandResult;
    }
}
