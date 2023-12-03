package commands.artist;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Album;
import entities.Library;
import entities.Song;
import entities.User;
import user.memory.UserMemory;
import utils.CheckUser;

public abstract class AddAlbum {
    /**
     * @param username  - user that should be added
     * @param memory    - database
     * @param timestamp - current timestamp
     * @return add album status
     */
    public static JsonNode addAlbum(final String username, final Integer timestamp,
                                    final Album newAlbum, final UserMemory memory,
                                    final Library library) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "addAlbum");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

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

        for (Album album : artist.getAlbums()) {
            if (album.getName().equals(newAlbum.getName())) {
                commandResult.put("message", username + " has another album with the same name.");
                return commandResult;
            }
        }

        for (int i = 0; i < newAlbum.getPlaylistSongs().size() - 1; i++) {
            String firstName = newAlbum.getPlaylistSongs().get(i).getName();
            for (int j = i + 1; j < newAlbum.getPlaylistSongs().size(); j++) {
                String secondName = newAlbum.getPlaylistSongs().get(j).getName();
                if (firstName.equals(secondName)) {
                    commandResult.put("message", username + " has the same song at "
                            + "least twice in this album.");
                    return commandResult;
                }
            }
        }

        commandResult.put("message", username + " has added new album successfully.");
        artist.getAlbums().add(newAlbum);
        for (Song song : newAlbum.getPlaylistSongs()) {
            library.getSongs().add(song);
        }

        return commandResult;
    }
}
