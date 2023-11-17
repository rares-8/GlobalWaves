package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.Audio;
import fileio.input.PlaylistInput;
import user.memory.UserMemory;

import java.util.ArrayList;

public abstract class FollowPlaylist {
    /**
     *
     * @param username - user that issued the command
     * @param memory - database for users
     * @param timestamp - timestamp for command
     * @return command result
     */
    public static JsonNode follow(final String username, final UserMemory memory, final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "follow");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        Audio currentSelectUser = memory.getCurrentSelect().get(username);

        if (!memory.getCurrentSelect().containsKey(username)) {
            commandResult.put("message", "Please select a source before following or unfollowing.");
            return commandResult;
        } else if (!currentSelectUser.getAudioType().equals("playlist")) {
            commandResult.put("message", "The selected source is not a playlist.");
            return commandResult;
        } else if (currentSelectUser.getOwner().equals(username)) {
            commandResult.put("message", "You cannot follow or unfollow your own playlist.");
            return commandResult;
        }

        ArrayList<Audio> followedPlaylists = memory.getFollowedPlaylists().get(username);
        int is_followed = 0;

        if (followedPlaylists == null) {
            followedPlaylists = new ArrayList<>();
            memory.getFollowedPlaylists().put(username, followedPlaylists);
        }

        for (Audio playlist : followedPlaylists) {
            // check if user is already following the selected playlists (compare references)
            if (currentSelectUser == playlist) {
                is_followed = 1;
                break;
            }
        }

        if (is_followed == 0) {
            commandResult.put("message", "Playlist followed successfully.");
            memory.getFollowedPlaylists().get(username).add(currentSelectUser);
        } else {
            commandResult.put("message", "Playlist unfollowed successfully.");
            memory.getFollowedPlaylists().get(username).remove(currentSelectUser);
        }

        return commandResult;
    }
}
