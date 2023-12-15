package commands.admin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Album;
import entities.Artist;
import entities.Library;
import entities.Song;
import entities.User;

public abstract class ShowAlbums {
    /** Show albums for given users
     * @param username  - artist name
     * @param library - library containing songs, users, podcasts
     * @param timestamp - current timestamp
     * @return show albums status
     */
    public static JsonNode showAlbums(final String username, final Library library,
                                   final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "showAlbums");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        Artist artist = null;
        for (User user : library.getUsers()) {
            if (user.getUsername().equals(username)) {
                artist = (Artist) user;
            }
        }

        ArrayNode albums = commandResult.putArray("result");
        for (Album album : artist.getAlbums()) {
            ObjectNode currentAlbum = mapper.createObjectNode();
            currentAlbum.put("name", album.getName());
            ArrayNode albumSongs = currentAlbum.putArray("songs");
            for (Song song : album.getPlaylistSongs()) {
                albumSongs.add(song.getName());
            }
            albums.add(currentAlbum);
        }
        return commandResult;
    }
}
