package commands.host;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.Podcast;
import entities.User;
import user.memory.UserMemory;
import utils.CheckUser;

public abstract class AddPodcast {
    /** Add podcast for given user
     * @param username   - user
     * @param memory     - database
     * @param timestamp  - current timestamp
     * @param newPodcast - podcast that should be added
     * @param library    - contains songs, playlists, podcasts, users
     * @return add podcast status
     */
    public static JsonNode addPodcast(final String username, final Integer timestamp,
                                      final Podcast newPodcast, final UserMemory memory,
                                      final Library library) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "addPodcast");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

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

        for (Podcast podcast : host.getPodcasts()) {
            if (podcast.getName().equals(newPodcast.getName())) {
                commandResult.put("message", username + " has another podcast "
                        + "with the same name.");
                return commandResult;
            }
        }

        for (int i = 0; i < newPodcast.getEpisodes().size() - 1; i++) {
            String firstName = newPodcast.getEpisodes().get(i).getName();
            for (int j = i + 1; j < newPodcast.getEpisodes().size(); j++) {
                String secondName = newPodcast.getEpisodes().get(j).getName();
                if (firstName.equals(secondName)) {
                    commandResult.put("message", username + " has the same"
                            + "episode in this podcast.");
                    return commandResult;
                }
            }
        }

        commandResult.put("message", username + " has added new podcast successfully.");
        host.getPodcasts().add(newPodcast);
        library.getPodcasts().add(newPodcast);

        return commandResult;
    }
}
