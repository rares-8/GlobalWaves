package commands.statistics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.*;
import user.memory.UserMemory;

import java.util.ArrayList;
import java.util.Comparator;

import static utils.Constants.RESULT_MAX_SIZE;

public abstract class Top5Albums {
    /**
     * @param memory    - database
     * @param timestamp - current timestamp
     * @param library - contains podcasts, songs, users
     * @return top albums
     */
    public static JsonNode topAlbums(final UserMemory memory, final Integer timestamp,
                                     final Library library) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "getTop5Albums");
        commandResult.put("timestamp", timestamp);

        ArrayList<Playlist> playlists = new ArrayList<>();
        for (User user : library.getUsers()) {
            if (!user.getType().equals("artist")) {
                continue;
            }

            for (Album album : user.getAlbums()) {
                int playlistLikes = 0;
                for (Song song : album.getPlaylistSongs()) {
                    playlistLikes += song.getLikes();
                }
                Playlist newPlaylist = new Playlist();
                newPlaylist.setName(album.getName());
                newPlaylist.setTotalLikes(playlistLikes);
                playlists.add(newPlaylist);
            }
        }

        Comparator<Playlist> byName = Comparator.comparing(Playlist::getName);
        Comparator<Playlist> byLikes = (Playlist o1, Playlist o2) -> o2.getTotalLikes() - o1.getTotalLikes();

        playlists.sort(byName);
        playlists.sort(byLikes);

        if (playlists.size() > RESULT_MAX_SIZE) {
            playlists = new ArrayList<>(playlists.subList(0, RESULT_MAX_SIZE));
        }

        ArrayNode likedPlaylists = commandResult.putArray("result");
        for (Playlist playlist : playlists) {
            likedPlaylists.add(playlist.getName());
        }
        return commandResult;
    }
}
