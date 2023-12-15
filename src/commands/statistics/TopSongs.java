package commands.statistics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.Song;
import user.memory.UserMemory;
import java.util.Map;
import java.util.ArrayList;

import static utils.Constants.RESULT_MAX_SIZE;


public abstract class TopSongs {
    /**
     * Top songs by likes
     * @param timestamp - current timestamp
     * @param memory - database
     * @return top songs
     */
    public static JsonNode topSongs(final Integer timestamp, final UserMemory memory,
                                    final Library library) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "getTop5Songs");
        commandResult.put("timestamp", timestamp);

        ArrayList<Song> allSongs = new ArrayList<>();

        // copy song names in new array
        for (Song song : library.getSongs()) {
            Song newSong = new Song();
            newSong.setName(song.getName());
            allSongs.add(newSong);
        }
        ArrayNode resultNode = commandResult.putArray("result");
        ArrayList<Integer> likes = new ArrayList<>();
        Map<String, ArrayList<Song>> likedSongs = memory.getLikedSongs();

        for (Song song : library.getSongs()) {
            int counter = 0;
            for (ArrayList<Song> userLikedSongs : likedSongs.values()) {
                if (userLikedSongs.contains(song)) {
                    counter++;
                }
            }
            likes.add(counter);
        }

        ArrayList<Song> sortedSongs = new ArrayList<>();
        for (int i = 0; i < library.getSongs().size(); i++) {
            int maxLikes = -1;
            for (Integer iterator : likes) {
                if (maxLikes < iterator) {
                    maxLikes = iterator;
                }
            }
            int index = likes.indexOf(maxLikes);
            sortedSongs.add(allSongs.get(index));
            allSongs.remove(index);
            likes.remove(index);
        }

        for (int i = 0; i < RESULT_MAX_SIZE; i++) {
            resultNode.add(sortedSongs.get(i).getName());
        }

        return commandResult;
    }
}
