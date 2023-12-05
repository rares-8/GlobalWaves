package commands.player;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Playlist;
import user.memory.UserMemory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public abstract class Shuffle {
    /**
     *
     * @param username - user that issued the command
     * @param seed - seed to randomize after
     * @param memory - database
     * @param timestamp - current timestamp
     * @return shuffle status
     */
    public static JsonNode shuffle(final String username, final Integer seed,
                                   final UserMemory memory, final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "shuffle");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        if (!memory.getLoadedAudio().containsKey(username)) {
            commandResult.put("message",
                    "Please load a source before using the shuffle function.");
            return commandResult;
        } else if (!memory.getLoadedAudio().get(username).getAudioType().equals("playlist")
        && !memory.getLoadedAudio().get(username).getAudioType().equals("album")) {
            commandResult.put("message",
                    "The loaded source is not a playlist.");
            return commandResult;
        }

        if (!memory.getIsShuffled().containsKey(username)) {
            commandResult.put("message", "Shuffle function activated successfully.");
            Random random = new Random(seed);
            Collections.shuffle(memory.getCollectionIndexes().get(username), random);
            memory.getIsShuffled().put(username, 1);
        } else {
            commandResult.put("message", "Shuffle function deactivated successfully.");
            Playlist playlist = (Playlist) memory.getLoadedAudio().get(username);
            ArrayList<Integer> indexes = new ArrayList<>();
            // put songs in original order
            for (int index = 0; index < playlist.getPlaylistSongs().size(); index++) {
                indexes.add(index);
            }
            Integer currentIndex = memory.getCurrentIndex().get(username);
            memory.getCollectionIndexes().put(username, indexes);
            memory.getCurrentIndex().put(username, indexes.indexOf(currentIndex));
            memory.getIsShuffled().remove(username);
        }

        return commandResult;
    }
}
