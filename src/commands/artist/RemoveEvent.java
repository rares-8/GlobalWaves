package commands.artist;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Event;
import entities.Library;
import entities.User;
import utils.CheckUser;

public abstract class RemoveEvent {
    /**
     * @param username  - user
     * @param timestamp - current timestamp
     * @param library - contains songs, playlists, podcasts, users
     * @return add event status
     */
    public static JsonNode removeEvent(final String username, final Library library,
                                     final String eventName, final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "removeEvent");
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

        ok = false;
        for (Event event : artist.getEvents()) {
            if (event.getName().equals(eventName)) {
                ok = true;
                break;
            }
        }

        if (!ok) {
            commandResult.put("message", username + " doesn't have an event with the given name.");
            return commandResult;
        }

        for (Event event : artist.getEvents()) {
            if (event.getName().equals(eventName)) {
                artist.getEvents().remove(event);
                break;
            }
        }
        commandResult.put("message", username + " deleted the event successfully.");
        return commandResult;
    }
}
