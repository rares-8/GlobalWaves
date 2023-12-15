package commands.artist;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Event;
import entities.Library;
import entities.User;
import utils.CheckUser;

import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;

public abstract class AddEvent {
    /** Add event for an artist
     * @param username  - user
     * @param timestamp - current timestamp
     * @param newEvent - event to be added
     * @param library - contains songs, playlists, podcasts, users
     * @return add event status
     */
    public static JsonNode addEvent(final String username, final Library library,
                                     final Event newEvent, final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "addEvent");
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

        for (Event event : artist.getEvents()) {
            if (event.getName().equals(newEvent.getName())) {
                commandResult.put("message", username + " has another event with the same name.");
                return commandResult;
            }
        }

        ok = AddEvent.isValid(newEvent.getDate());
        if (!ok) {
            commandResult.put("message", "Event for " + username + " does not have a valid date.");
            return commandResult;
        }

        commandResult.put("message", username + " has added new event successfully.");
        artist.getEvents().add(newEvent);
        return commandResult;
    }

    private static boolean isValid(final String date) {
        String customPattern = "dd-MM-yyyy";
        DateTimeFormatter checker = DateTimeFormatter.ofPattern(customPattern);
        try {
            checker.parse(date);
        } catch (DateTimeException e) {
            return false;
        }
        return true;
    }
}
