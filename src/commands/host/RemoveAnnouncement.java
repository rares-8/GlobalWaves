package commands.host;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Announcement;
import entities.Library;
import entities.User;
import utils.CheckUser;

public abstract class RemoveAnnouncement {
    /** Remove announcement from host
     * @param username  - user
     * @param timestamp - current timestamp
     * @param announcementName - announcement to be deleted
     * @param library - contains songs, playlists, podcasts, users
     * @return remove announcement status
     */
    public static JsonNode removeAnnouncement(final String username, final Library library,
                                           final String announcementName,
                                           final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "removeAnnouncement");
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

        ok = false;
        int index = 0;
        for (Announcement currentAnnouncement : host.getAnnouncements()) {
            if (currentAnnouncement.getName().equals(announcementName)) {
                ok = true;
                index = host.getAnnouncements().indexOf(currentAnnouncement);
                break;
            }
        }

        if (!ok) {
            commandResult.put("message", username + " has no"
                    + " announcement with the given name.");
            return commandResult;
        }

        commandResult.put("message", username + " has successfully deleted the announcement.");
        host.getAnnouncements().remove(index);

        return commandResult;
    }

}
