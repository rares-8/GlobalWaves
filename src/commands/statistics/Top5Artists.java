package commands.statistics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Album;
import entities.Library;
import entities.Song;
import entities.User;
import user.memory.UserMemory;

import java.util.ArrayList;
import java.util.Comparator;

import static utils.Constants.RESULT_MAX_SIZE;

public abstract class Top5Artists {
    /** Top 5 artists by total likes
     * @param memory    - database
     * @param timestamp - current timestamp
     * @param library - contains podcasts, songs, users
     * @return top albums
     */
    public static JsonNode topArtists(final UserMemory memory, final Integer timestamp,
                                     final Library library) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "getTop5Artists");
        commandResult.put("timestamp", timestamp);

        ArrayList<User> artists = new ArrayList<>();
        for (User user : library.getUsers()) {
            if (!user.getType().equals("artist")) {
                continue;
            }
            artists.add(user);
            user.setTotalLikes(0);
            for (Album album : user.getAlbums()) {
                int playlistLikes = 0;
                for (Song song : album.getPlaylistSongs()) {
                    playlistLikes += song.getLikes();
                }
                user.setTotalLikes(user.getTotalLikes() + playlistLikes);
            }
        }

        Comparator<User> byName = Comparator.comparing(User::getUsername);
        Comparator<User> byLikes = (User o1, User o2) -> o2.getTotalLikes() - o1.getTotalLikes();

        artists.sort(byName);
        artists.sort(byLikes);

        if (artists.size() > RESULT_MAX_SIZE) {
            artists = new ArrayList<>(artists.subList(0, RESULT_MAX_SIZE));
        }

        ArrayNode topArtists = commandResult.putArray("result");
        for (User user : artists) {
            topArtists.add(user.getUsername());
        }

        return commandResult;
    }
}
