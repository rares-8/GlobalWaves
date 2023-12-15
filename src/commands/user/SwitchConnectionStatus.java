package commands.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.User;
import user.memory.UserMemory;

public abstract class SwitchConnectionStatus {
    /**Make a user online / offline
     * @param username  - user that issued the command
     * @param memory    - database
     * @param timestamp - current timestamp
     * @param library - library containing every user, song and podcast
     */
    public static JsonNode switchStatus(final String username, final UserMemory memory,
                                        final Integer timestamp, final Library library) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "switchConnectionStatus");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        int ok = 0;
        User user = null;
        for (User currentUser : library.getUsers()) {
            if (currentUser.getUsername().equals(username)) {
                ok = 1;
                user = currentUser;
                break;
            }
        }

        if (ok == 0) {
            commandResult.put("message", "The username " + username + " doesn't exist.");
            return commandResult;
        }

        if (!user.getType().equals("user")) {
            commandResult.put("message", username + " is not a normal user.");
            return commandResult;
        }

        if (memory.getConnectionStatus().containsKey(username)) {
            memory.getConnectionStatus().remove(username);
            commandResult.put("message",  username + " has changed status successfully.");
        } else {
            memory.getConnectionStatus().put(username, 1);
            commandResult.put("message",  username + " has changed status successfully.");
        }

        return commandResult;
    }
}
