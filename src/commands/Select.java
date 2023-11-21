package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.Audio;
import user.memory.UserMemory;

import java.util.ArrayList;

public abstract class Select {

    /**
     *
     * @param username - user that issued the command
     * @param itemNumber - item to select from search result
     * @param timestamp - timestamp for command
     * @param memory - database for users
     * @return command result
     */
    public static JsonNode select(final String username, final Integer itemNumber,
                                  final Integer timestamp, final UserMemory memory) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "select");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);
        memory.getCurrentSelect().remove(username);

        if (memory.getLastSearch().containsKey(username)) {
            ArrayList<Audio> lastSearch = memory.getLastSearch().get(username);
            if (lastSearch.size() < itemNumber) {
                commandResult.put("message", "The selected ID is too high.");
            } else {
                commandResult.put("message", "Successfully selected "
                        + lastSearch.get(itemNumber - 1).getName() + ".");
                memory.getCurrentSelect().put(username, lastSearch.get(itemNumber - 1));
            }
            memory.getLastSearch().remove(username);
        } else {
            commandResult.put("message", "Please conduct a search before making a selection.");
        }
        return commandResult;
    }
}
