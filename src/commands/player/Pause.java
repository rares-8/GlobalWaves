package commands.player;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import user.memory.UserMemory;
import utils.UpdateTimestamp;

public abstract class Pause {
    /**
     * @param username  - user that issued the command
     * @param memory    - database
     * @param timestamp - current timestamp
     * @return command result
     */
    public static JsonNode pause(final String username, final UserMemory memory,
                                 final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "playPause");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        if (!memory.getLoadedAudio().containsKey(username)) {
            commandResult.put("message", "Please load a source before attempting to pause "
                    + "or resume playback.");

            return commandResult;
        }

        if (memory.getIsPaused().containsKey(username)) {
            memory.getIsPaused().remove(username);
            UpdateTimestamp.updateTimestamp(username, timestamp, memory);
            commandResult.put("message", "Playback resumed successfully.");
        } else {
            memory.getIsPaused().put(username, 1);
            commandResult.put("message", "Playback paused successfully.");
        }

        return commandResult;
    }
}
