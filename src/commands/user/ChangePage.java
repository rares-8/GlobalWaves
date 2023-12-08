package commands.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.pages.HomePage;
import entities.pages.LikedContentPage;
import user.memory.UserMemory;

public abstract class ChangePage {
    /**
     * @param username  - user that issued the command
     * @param memory    - database
     * @param timestamp - current timestamp
     * @param library   - library containing every user, song and podcast
     */
    public static JsonNode changePage(final String username, final String newPage,
                                 final UserMemory memory, final Integer timestamp,
                                 final Library library) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "changePage");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        if (newPage.equals("Home")) {
            memory.getCurrentPage().put(username, new HomePage());
            commandResult.put("message", username + " accessed " + newPage + " successfully.");
        } else if (newPage.equals("LikedContent")) {
            memory.getCurrentPage().put(username, new LikedContentPage());
            commandResult.put("message", username + " accessed " + newPage + " successfully.");
        } else {
            commandResult.put("message", username + " is trying to access a non-existent page.");
        }

        return commandResult;
    }
}
