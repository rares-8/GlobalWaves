package commands.artist;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Merch;
import entities.Library;
import entities.User;
import utils.CheckUser;

public abstract class AddMerch {
    /** Add merch for an artist
     * @param username  - user
     * @param timestamp - current timestamp
     * @param newMerch - merch to be added
     * @param library - contains songs, playlists, podcasts, users
     * @return add merch status
     */
    public static JsonNode addMerch(final String username, final Library library,
                                    final Merch newMerch, final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "addMerch");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        // check if users exists
        boolean ok = CheckUser.checkUser(username, library);
        if (!ok) {
            commandResult.put("message", "The username " + username + " doesn't exist.");
            return commandResult;
        }

        User artist = null;
        for (User user : library.getUsers()) {
            if (user.getUsername().equals(username)) {
                artist = user;
            }
        }

        if (!artist.getType().equals("artist")) {
            commandResult.put("message", username + " is not an artist.");
            return commandResult;
        }

        for (Merch merch : artist.getMerchandise()) {
            if (merch.getName().equals(newMerch.getName())) {
                commandResult.put("message", username + " has merchandise with the same name.");
                return commandResult;
            }
        }

        if (newMerch.getPrice() < 0) {
            commandResult.put("message", "Price for merchandise can not be negative.");
            return commandResult;
        }

        commandResult.put("message", username + " has added new merchandise successfully.");
        artist.getMerchandise().add(newMerch);
        return commandResult;
    }
}
