package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.Audio;
import fileio.input.PlaylistInput;
import fileio.input.SongInput;
import user.memory.UserMemory;

import java.util.ArrayList;
import java.util.Map;

public abstract class ShowPlaylists {
    public static JsonNode show(final String username, final UserMemory memory, final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "showPlaylists");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        if (!memory.getUserPlaylists().containsKey(username)) {
            commandResult.put("result", "[]");
            return commandResult;
        }

        ArrayList<PlaylistInput> userPlaylists = memory.getUserPlaylists().get(username);
        if (userPlaylists == null) {
            commandResult.put("result", "[]");
            return commandResult;
        }

        ArrayNode auxNode = commandResult.putArray("result");
        for (PlaylistInput playlist : userPlaylists) {
            ArrayList<SongInput> playlistSongs = playlist.getPlaylistSongs();

            ObjectNode playlistNode = mapper.createObjectNode();
            playlistNode.put("name", playlist.getName());
            ArrayNode songs = playlistNode.putArray("songs");
            for (SongInput song : playlistSongs) {
                songs.add(song.getName());
            }

            if (playlist.getIsPrivate() == 0) {
                playlistNode.put("visibility", "public");
            } else {
                playlistNode.put("visibility", "private");
            }
            playlistNode.put("followers", countFollowers(memory.getFollowedPlaylists(), playlist));
            auxNode.add(playlistNode);
        }

        return commandResult;
    }

    public static Integer countFollowers(Map<String, ArrayList<Audio>> followedPlaylists, PlaylistInput playlist) {
        Integer count = 0;
        for (Map.Entry<String, ArrayList<Audio>> element : followedPlaylists.entrySet()) {
            ArrayList<Audio> userFollows = element.getValue();
            if (userFollows != null) {
                for (Audio currentPlaylist : userFollows) {
                    if (currentPlaylist.getName().equals(playlist.getName())
                            && currentPlaylist.getOwner().equals(playlist.getOwner())) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
