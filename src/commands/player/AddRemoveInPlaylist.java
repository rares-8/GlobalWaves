package commands.player;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Audio;
import entities.Playlist;
import entities.Song;
import user.memory.UserMemory;

public abstract class AddRemoveInPlaylist {
    /**Add or remove songs for playlist
     * @param username   - user that issued the command
     * @param playlistId - playlist id
     * @param memory     - database
     * @param timestamp  - current timestamp
     * @return add/remove status
     */
    public static JsonNode addRemove(final String username, final Integer playlistId,
                                     final UserMemory memory, final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "addRemoveInPlaylist");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        if (!memory.getLoadedAudio().containsKey(username)) {
            commandResult.put("message", "Please load a source before adding to"
                    + " or removing from the playlist.");
            return commandResult;
        } else if (memory.getLoadedAudio().get(username).getAudioType().equals("podcast")) {
            commandResult.put("message", "The loaded source is not a song.");
            return commandResult;
        } else if (!memory.getUserPlaylists().containsKey(username)) {
            commandResult.put("message", "The specified playlist does not exist.");
            return commandResult;
        } else if (memory.getUserPlaylists().get(username).size() < playlistId) {
            commandResult.put("message", "The specified playlist does not exist.");
            return commandResult;
        }

        Playlist selectedPlaylist =
                memory.getUserPlaylists().get(username).get(playlistId - 1);

        Audio loadedAudio;
        if (memory.getLoadedAudio().get(username).getAudioType().equals("song")) {
            loadedAudio = memory.getLoadedAudio().get(username);
        } else {
            Integer index = memory.getCurrentIndex().get(username);
            loadedAudio = memory.getLoadedAudio().get(username).getPlaylistSongs().get(index);
        }
        int songExists = 0, songIndex;
        // check if loaded Song is already in the playlist
        for (songIndex = 0; songIndex < selectedPlaylist.getPlaylistSongs().size(); songIndex++) {
            if (loadedAudio == selectedPlaylist.getPlaylistSongs().get(songIndex)) {
                songExists = 1;
                break;
            }
        }

        if (songExists == 1) {
            commandResult.put("message", "Successfully removed from playlist.");
            selectedPlaylist.getPlaylistSongs().remove(songIndex);
        } else {
            commandResult.put("message", "Successfully added to playlist.");
            selectedPlaylist.getPlaylistSongs().add((Song) loadedAudio);
        }
        return commandResult;
    }
}
