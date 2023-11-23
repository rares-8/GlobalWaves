package commands.player;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import user.memory.UserMemory;
import entities.Audio;

public abstract class Repeat {
    /**
     * @param username  - user that issued the command
     * @param memory    - database
     * @param timestamp - current timestamp
     * @return command result
     */
    public static JsonNode repeat(final String username, final UserMemory memory,
                                 final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "repeat");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);
        if (!memory.getLoadedAudio().containsKey(username)) {
            commandResult.put("message", "Please load a source before setting the repeat status.");
            return commandResult;
        }

        Audio loadedAudio = memory.getLoadedAudio().get(username);
        String audioType = loadedAudio.getAudioType();
        int currentRepeatMode = 0;
        if (!memory.getIsRepeating().containsKey(username)) {
            currentRepeatMode = 1;
            memory.getIsRepeating().put(username, 1);
        } else if (memory.getIsRepeating().get(username) == 1) {
            currentRepeatMode = 2;
            memory.getIsRepeating().put(username, 2);
        } else if (memory.getIsRepeating().get(username) == 2) {
            memory.getIsRepeating().remove(username);
        }

        if (audioType.equals("podcast") || audioType.equals("song")) {
            switch (currentRepeatMode) {
                case 0:
                    commandResult.put("message", "Repeat mode changed to no repeat.");
                    break;
                case 1:
                    commandResult.put("message", "Repeat mode changed to repeat once.");
                    break;
                case 2:
                    commandResult.put("message", "Repeat mode changed to repeat infinite.");
                    break;
                default:
            }
        } else {
            switch (currentRepeatMode) {
                case 0:
                    commandResult.put("message", "Repeat mode changed to no repeat.");
                    break;
                case 1:
                    commandResult.put("message", "Repeat mode changed to repeat all.");
                    break;
                case 2:
                    commandResult.put("message", "Repeat mode changed to repeat current song.");
                    break;
                default:
            }
        }

        return commandResult;
    }
}
