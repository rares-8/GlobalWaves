package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.PlaylistInput;
import user.memory.UserMemory;

import java.util.ArrayList;

public abstract class CreatePlaylist {
    /**
     *
     * @param username - owner of the playlist
     * @param playlistName
     * @param timestamp - current timestamp
     * @param memory - database
     * @return command result
     */
    public static JsonNode createPlaylist(final String username, final String playlistName,
                                          final Integer timestamp, final UserMemory memory) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "createPlaylist");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        //Search playlist in memory to check if it exists
        int ok = 1;
        if (memory.getUserPlaylists().containsKey(username)) {
            ArrayList<PlaylistInput> usersPlaylists = memory.getUserPlaylists().get(username);
            for (PlaylistInput playlist : usersPlaylists) {
                if (playlist.getName().equals(playlistName)) {
                    ok = 0;
                    break;
                }
            }
        }

        if (ok == 1) {
            commandResult.put("message", "Playlist created successfully.");
            PlaylistInput newPlaylist = new PlaylistInput(playlistName, 0, new ArrayList<>(),
                    username, timestamp);
            if (memory.getUserPlaylists().containsKey(username)) {
                memory.getUserPlaylists().get(username).add(newPlaylist);
            } else {
                memory.getUserPlaylists().put(username, new ArrayList<>());
                memory.getUserPlaylists().get(username).add(newPlaylist);
            }
            memory.getPublicPlaylists().add(newPlaylist);
        } else {
            commandResult.put("message", "A playlist with the same name already exists.");
        }

        return commandResult;
    }
}
