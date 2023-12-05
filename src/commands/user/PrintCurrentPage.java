package commands.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.pages.PrintVisitor;
import user.memory.UserMemory;

public abstract class PrintCurrentPage {
    /**
     * @param username  - user that issued the command
     * @param memory    - database
     * @param timestamp - current timestamp
     * @param library   - library containing every user, song and podcast
     */
    public static JsonNode print(final String username, final UserMemory memory,
                                 final Integer timestamp, final Library library) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "printCurrentPage");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        if (!memory.getConnectionStatus().containsKey(username)) {
            commandResult.put("message", username + " is offline.");
            return commandResult;
        }

        PrintVisitor printVisitor = new PrintVisitor();
        String result = memory.getCurrentPage().get(username).accept(printVisitor,
                username, memory, library);

        commandResult.put("message", result);
        return commandResult;
    }
}
