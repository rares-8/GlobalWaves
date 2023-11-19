package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.Audio;
import fileio.input.PlaylistInput;
import fileio.input.SongInput;
import user.memory.UserMemory;

public abstract class AddRemoveInPlaylist {
    /**
     * @param username   - user that issued the command
     * @param playlistId - playlist id
     * @param memory     - database
     * @param timestamp  - current timestamp
     * @return command result
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
        } else if (!memory.getLoadedAudio().get(username).getAudioType().equals("song")) {
            commandResult.put("message", "The loaded source is not a song.");
            return commandResult;
        } else if (!memory.getUserPlaylists().containsKey(username)) {
            commandResult.put("message", "The specified playlist does not exist.");
            return commandResult;
        } else if (memory.getUserPlaylists().get(username).size() > playlistId) {
            commandResult.put("message", "The specified playlist does not exist.");
            return commandResult;
        }

        PlaylistInput selectedPlaylist =
                memory.getUserPlaylists().get(username).get(playlistId - 1);

        Audio loadedSong = memory.getLoadedAudio().get(username);
        int songExists = 0, songIndex;
        for (songIndex = 0; songIndex < selectedPlaylist.getPlaylistSongs().size(); songIndex++) {
            if (loadedSong == selectedPlaylist.getPlaylistSongs().get(songIndex)) {
                songExists = 1;
                break;
            }
        }

        if (songExists == 1) {
            commandResult.put("message", "Successfully removed from playlist.");
            selectedPlaylist.getPlaylistSongs().remove(songIndex);
        } else {
            commandResult.put("message", "Successfully added to playlist.");
            selectedPlaylist.getPlaylistSongs().add((SongInput) loadedSong);
        }
        return commandResult;
    }
}
