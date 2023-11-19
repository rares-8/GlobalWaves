package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.PlaylistInput;
import user.memory.UserMemory;

public abstract class SwitchVisibility {
    /**
     *
     * @param username - user that issued the command
     * @param playlistId - id for playlist that needs to switch visibility
     * @param timestamp - current playlist
     * @param memory - database
     * @return command result
     */
    public static JsonNode switchVisibility(final String username, final Integer playlistId,
                                            final Integer timestamp, final UserMemory memory) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "switchVisibility");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        // if user has no playlists
        if (!memory.getUserPlaylists().containsKey(username)) {
            commandResult.put("message", "The specified playlist ID is too high");
            return commandResult;
        } else if (memory.getUserPlaylists().get(username).size() < playlistId) {
            commandResult.put("message", "The specified playlist ID is too high");
            return commandResult;
        }

        PlaylistInput playlist = memory.getUserPlaylists().get(username).get(playlistId - 1);
        if (playlist.getIsPrivate() == 1) {
            playlist.setIsPrivate(0);
            memory.getPublicPlaylists().add(playlist);
            commandResult.put("message", "Visibility status updated successfully to public.");
        } else {
            playlist.setIsPrivate(1);
            memory.getPublicPlaylists().remove(playlist);
            commandResult.put("message", "Visibility status updated successfully to private.");
        }
        return commandResult;
    }
}
