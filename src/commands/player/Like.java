package commands.player;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Song;
import user.memory.UserMemory;

import java.util.ArrayList;

public abstract class Like {
    /**Like/Unlike currently loaded song
     * @param username  - user that issued the command
     * @param memory    - database
     * @param timestamp - current timestamp
     * @return like status
     */
    public static JsonNode like(final String username, final UserMemory memory,
                                final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "like");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        if (!memory.getLoadedAudio().containsKey(username)) {
            commandResult.put("message", "Please load a source before liking or unliking.");
            return commandResult;
        } else if (memory.getLoadedAudio().get(username).getAudioType().equals("podcast")) {
            commandResult.put("message", "Loaded source is not a song.");
            return commandResult;
        } else if (!memory.getConnectionStatus().containsKey(username)) {
            commandResult.put("message", username + " is offline.");
            return commandResult;
        }

        Song currentSong;
        if (memory.getLoadedAudio().get(username).getAudioType().equals("playlist")) {
            // get currently loaded song from playlist
            Integer index = memory.getCurrentIndex().get(username);
            currentSong = memory.getLoadedAudio().get(username).getPlaylistSongs().get(index);
        } else if (memory.getLoadedAudio().get(username).getAudioType().equals("album")) {
            Integer index = memory.getCurrentIndex().get(username);
            currentSong = memory.getLoadedAudio().get(username).getPlaylistSongs().get(index);
        } else {
            currentSong = (Song) memory.getLoadedAudio().get(username);
        }
        if (!memory.getLikedSongs().containsKey(username)) {
            memory.getLikedSongs().put(username, new ArrayList<>());
            memory.getLikedSongs().get(username).add(currentSong);
            commandResult.put("message", "Like registered successfully.");
            currentSong.setLikes(currentSong.getLikes() + 1);
            return commandResult;
        }

        int isLiked = 0, songIndex;
        // check if song is already liked
        for (songIndex = 0; songIndex < memory.getLikedSongs().get(username).size(); songIndex++) {
            if (memory.getLikedSongs().get(username).get(songIndex) == currentSong) {
                isLiked = 1;
                break;
            }
        }

        if (isLiked == 0) {
            commandResult.put("message", "Like registered successfully.");
            currentSong.setLikes(currentSong.getLikes() + 1);
            memory.getLikedSongs().get(username).add(currentSong);
        } else {
            commandResult.put("message", "Unlike registered successfully.");
            currentSong.setLikes(currentSong.getLikes() - 1);
            memory.getLikedSongs().get(username).remove(songIndex);
        }

        return commandResult;
    }
}
