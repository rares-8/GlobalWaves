package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.LibraryInput;
import fileio.input.SongInput;
import user.memory.UserMemory;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.ArrayList;

import static utils.Constants.RESULT_MAX_SIZE;

public abstract class TopSongs {
    public static JsonNode topSongs(final Integer timestamp, final UserMemory memory,
                                    final LibraryInput library) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "getTop5Songs");
        commandResult.put("timestamp", timestamp);

        ArrayList<SongInput> allSongs = new ArrayList<>();

        // copy song names in new array
        for (SongInput song : library.getSongs()) {
            SongInput newSong = new SongInput();
            newSong.setName(song.getName());
            allSongs.add(newSong);
        }
        ArrayNode resultNode = commandResult.putArray("result");
        ArrayList<Integer> likes = new ArrayList<>();
        Map<String, ArrayList<SongInput>> likedSongs = memory.getLikedSongs();

        for (SongInput song : library.getSongs()) {
            int counter = 0;
            for (ArrayList<SongInput> userLikedSongs : likedSongs.values()) {
                if (userLikedSongs.contains(song))
                    counter++;
            }
            likes.add(counter);
        }

        ArrayList<SongInput> sortedSongs = new ArrayList<>();
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

        for (int i = 0; i < 5; i++) {
            resultNode.add(sortedSongs.get(i).getName());
        }

        return commandResult;
    }
}
