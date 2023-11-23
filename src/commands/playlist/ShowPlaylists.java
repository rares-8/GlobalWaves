package commands.playlist;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Playlist;
import entities.Song;
import user.memory.UserMemory;
import utils.CountFollowers;

import java.util.ArrayList;

public abstract class ShowPlaylists {
    /**
     * @param username  - user that issued the command
     * @param memory    - database
     * @param timestamp - current timestamp
     * @return command result
     */
    public static JsonNode show(final String username, final UserMemory memory,
                                final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "showPlaylists");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        if (!memory.getUserPlaylists().containsKey(username)) {
            commandResult.put("result", "[]");
            return commandResult;
        }

        ArrayList<Playlist> userPlaylists = memory.getUserPlaylists().get(username);
        if (userPlaylists == null) {
            commandResult.put("result", "[]");
            return commandResult;
        }

        ArrayNode auxNode = commandResult.putArray("result");
        for (Playlist playlist : userPlaylists) {
            ArrayList<Song> playlistSongs = playlist.getPlaylistSongs();

            ObjectNode playlistNode = mapper.createObjectNode();
            playlistNode.put("name", playlist.getName());
            ArrayNode songs = playlistNode.putArray("songs");
            for (Song song : playlistSongs) {
                songs.add(song.getName());
            }

            if (playlist.getIsPrivate() == 0) {
                playlistNode.put("visibility", "public");
            } else {
                playlistNode.put("visibility", "private");
            }
            playlistNode.put("followers",
                    CountFollowers.countFollowers(memory.getFollowedPlaylists(), playlist));
            auxNode.add(playlistNode);
        }

        return commandResult;
    }
}
