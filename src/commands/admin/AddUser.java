package commands.admin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Artist;
import entities.Host;
import entities.Library;
import entities.User;
import entities.pages.HomePage;
import user.memory.UserMemory;
import utils.CheckUser;

public abstract class AddUser {
    /** Adds a user to library
     * @param username  - user that should be added
     * @param memory    - database
     * @param timestamp - current timestamp
     * @return add user status
     */
    public static JsonNode addUser(final String username, final String city,
                                   final Integer age, final String type, final UserMemory memory,
                                   final Integer timestamp, final Library library) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "addUser");
        commandResult.put("timestamp", timestamp);
        commandResult.put("user", username);

        boolean ok = CheckUser.checkUser(username, library);

        // user already exists
        if (ok) {
            commandResult.put("message", "The username " + username + " is already taken.");
        } else {
            commandResult.put("message", "The username " + username
                    + " has been added successfully.");
            if (type.equals("user")) {
                User newUser = new User(username, age, city, type);
                library.getUsers().add(newUser);
            } else if (type.equals("host")) {
                Host newHost = new Host(username, age, city);
                library.getUsers().add(newHost);
            } else {
                Artist newArtist = new Artist(username, age, city);
                library.getUsers().add(newArtist);
            }
            memory.getConnectionStatus().put(username, 1);
            memory.getCurrentPage().put(username, new HomePage());
        }

        return commandResult;
    }
}
