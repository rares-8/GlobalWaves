package commands;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import user.memory.UserMemory;

public class Status {
    public static JsonNode status(String username, UserMemory memory, Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "status");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        if (!memory.getLoadedAudio().containsKey(username)) {
            ObjectNode statsNode = commandResult.putObject("stats");
            statsNode.put("name", "");
            statsNode.put("remainedTime", 0);

            return putStats(username, memory, commandResult, statsNode);
        }

        ObjectNode statsNode = commandResult.putObject("stats");
        statsNode.put("name", memory.getLoadedAudio().get(username).getName());
        statsNode.put("remainedTime", memory.getRemainingTime().get(username));

        return putStats(username, memory, commandResult, statsNode);
    }

    private static JsonNode putStats(String username, UserMemory memory, ObjectNode commandResult, ObjectNode statsNode) {
        if (!memory.getIsRepeating().containsKey(username)) {
            statsNode.put("repeat", "No Repeat");
        } else if (memory.getIsRepeating().get(username) == 0) {
            statsNode.put("repeat", "No Repeat");
        } else {
            statsNode.put("repeat", "Repeat");
        }

        if (!memory.getIsShuffled().containsKey(username)) {
            statsNode.put("shuffle", (boolean) false);
        } else statsNode.put("shuffle", memory.getIsShuffled().get(username) != 0);

        if (!memory.getIsPaused().containsKey(username)) {
            statsNode.put("paused", (boolean) false);
        } else statsNode.put("paused", memory.getIsPaused().get(username) != 0);

        return commandResult;
    }
}
