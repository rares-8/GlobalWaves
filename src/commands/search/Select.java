package commands.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Audio;
import entities.User;
import entities.pages.ArtistPage;
import entities.pages.HostPage;
import user.memory.UserMemory;

import java.util.ArrayList;

public abstract class Select {

    /**
     * @param username   - user that issued the command
     * @param itemNumber - item to select from search result
     * @param timestamp  - timestamp for command
     * @param memory     - database for users
     * @return select status
     */
    public static JsonNode select(final String username, final Integer itemNumber,
                                  final Integer timestamp, final UserMemory memory) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "select");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);
        memory.getCurrentSelect().remove(username);

        if (memory.getLastSearchAudio().containsKey(username)) {
            ArrayList<Audio> lastSearch = memory.getLastSearchAudio().get(username);
            if (lastSearch.size() < itemNumber) {
                commandResult.put("message", "The selected ID is too high.");
            } else {
                commandResult.put("message", "Successfully selected "
                        + lastSearch.get(itemNumber - 1).getName() + ".");
                memory.getCurrentSelect().put(username, lastSearch.get(itemNumber - 1));
            }
            memory.getLastSearchAudio().remove(username);
        } else if (memory.getLastSearchUser().containsKey(username)) {
            ArrayList<User> lastSearch = memory.getLastSearchUser().get(username);
            if (lastSearch.size() < itemNumber) {
                commandResult.put("message", "The selected ID is too high.");
            } else {
                User selectedUser = memory.getLastSearchUser().get(username).get(itemNumber - 1);
                if (selectedUser.getType().equals("artist")) {
                    memory.getCurrentPage().put(username,
                            new ArtistPage(selectedUser.getUsername()));
                } else if (selectedUser.getType().equals("host")) {
                    memory.getCurrentPage().put(username,
                            new HostPage(selectedUser.getUsername()));
                }

                commandResult.put("message", "Successfully selected "
                        + selectedUser.getUsername() + "'s page.");
            }
        } else {
            commandResult.put("message", "Please conduct a search before making a selection.");
        }
        return commandResult;
    }
}
