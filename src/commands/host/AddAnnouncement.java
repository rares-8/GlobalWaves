package commands.host;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Announcement;
import entities.Library;
import entities.User;
import utils.CheckUser;

public abstract class AddAnnouncement {
    /**
     * @param username  - user
     * @param timestamp - current timestamp
     * @param newAnnouncement - announcement to be added
     * @param library - contains songs, playlists, podcasts, users
     * @return add announcement status
     */
    public static JsonNode addAnnouncement(final String username, final Library library,
                                           final Announcement newAnnouncement,
                                           final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "addAnnouncement");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        // check if users exists
        boolean ok = CheckUser.checkUser(username, library);
        if (!ok) {
            commandResult.put("message", "The username " + username + " doesn't exist.");
            return commandResult;
        }

        User host = null;
        for (User user : library.getUsers()) {
            if (user.getUsername().equals(username)) {
                host = user;
            }
        }

        if (!host.getType().equals("host")) {
            commandResult.put("message", username + " is not a host.");
            return commandResult;
        }

        for (Announcement announcement : host.getAnnouncements()) {
            if (announcement.getName().equals(newAnnouncement.getName())) {
                commandResult.put("message", username + " has already"
                        + " added an announcement with this name.");
                return commandResult;
            }
        }

        commandResult.put("message", username + " has successfully added new announcement.");
        host.getAnnouncements().add(newAnnouncement);
        return commandResult;
    }
}
