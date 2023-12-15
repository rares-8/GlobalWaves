package commands.statistics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.User;
import user.memory.UserMemory;

public abstract class AllUsers {
    /** Print all users
     * @param memory    - database
     * @param timestamp - current timestamp
     * @param library   - library containing every user, song and podcast
     */
    public static JsonNode getUsers(final UserMemory memory,
                                    final Integer timestamp, final Library library) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "getAllUsers");
        commandResult.put("timestamp", timestamp);

        ArrayNode allUsers = commandResult.putArray("result");
        // get normal users
        for (User user : library.getUsers()) {
            if (!user.getType().equals("user")) {
                continue;
            }
            allUsers.add(user.getUsername());
        }

        // get artists
        for (User user : library.getUsers()) {
            if (!user.getType().equals("artist")) {
                continue;
            }
            allUsers.add(user.getUsername());
        }

        // get hosts
        for (User user : library.getUsers()) {
            if (!user.getType().equals("host")) {
                continue;
            }
            allUsers.add(user.getUsername());
        }

        return commandResult;
    }
}
