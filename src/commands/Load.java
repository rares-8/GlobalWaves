package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.Audio;
import user.memory.UserMemory;

import java.util.Map;

public class Load {
    public static JsonNode load(String username, UserMemory memory, Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "load");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        Map<String, Audio> map = memory.getCurrentSelect();
        if (!map.containsKey(username)) {
            commandResult.put("message", "Please select a source before attempting to load.");
            return commandResult;
        } else if (map.get(username).getAudioType().equals("playlist")
        && map.get(username).getPlaylistSongs().isEmpty()) {
            commandResult.put("message", "You can't load an empty audio collection!");
            return commandResult;
        } else if (map.get(username).getAudioType().equals("podcast")
        && map.get(username).getEpisodes().isEmpty()) {
            commandResult.put("message", "You can't load an empty audio collection!");
            return commandResult;
        }

        Audio currentSelect = memory.getCurrentSelect().get(username);
        // for now, do only songs
        if (currentSelect.getAudioType().equals("song")) {
            map.put(username, currentSelect);
            memory.getRemainingTime().put(username, currentSelect.getDuration());
            memory.getLoadedAudio().put(username, currentSelect);
            commandResult.put("message", "Playback loaded successfully.");
        }

        return commandResult;
    }
}
