package commands.player;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Podcast;
import user.memory.UserMemory;

public abstract class Status {
    /**
     * @param username  - user that issued the command
     * @param memory    - database
     * @param timestamp - current timestamp
     * @return command result
     */
    public static JsonNode status(final String username, final UserMemory memory,
                                  final Integer timestamp) {
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

        String name = null;
        if (memory.getLoadedAudio().get(username).getAudioType().equals("playlist")) {
            Integer index = memory.getCurrentIndex().get(username);
            name = memory.getLoadedAudio().get(username).getPlaylistSongs().get(index).getName();
        } else if (memory.getLoadedAudio().get(username).getAudioType().equals("song")) {
            name = memory.getLoadedAudio().get(username).getName();
        } else {
            Integer index = memory.getCurrentIndex().get(username);
            name = memory.getLoadedAudio().get(username).getEpisodes().get(index).getName();
        }
        statsNode.put("name", name);
        if (!memory.getLoadedAudio().get(username).getAudioType().equals("podcast")) {
            statsNode.put("remainedTime", memory.getRemainingTime().get(username));
        } else {
            Podcast currentPodcast = (Podcast) memory.getLoadedAudio().get(username);
            int podcastIndex = memory.getLoadedPodcasts().get(username).indexOf(currentPodcast);
            statsNode.put("remainedTime",
                    memory.getEpisodeRemainingTime().get(username).get(podcastIndex));
        }

        return putStats(username, memory, commandResult, statsNode);
    }

    /**
     * @param statsNode - contains the stats field
     * @return command result, now containing the stats
     */
    private static JsonNode putStats(final String username, final UserMemory memory,
                                     final ObjectNode commandResult, final ObjectNode statsNode) {
        if (!memory.getIsRepeating().containsKey(username)) {
            statsNode.put("repeat", "No Repeat");
        } else if (memory.getLoadedAudio().containsKey(username)) {
            Integer repeatingMode = memory.getIsRepeating().get(username);
            if (memory.getLoadedAudio().get(username).getAudioType().equals("playlist")) {
                if (repeatingMode == 1) {
                    statsNode.put("repeat", "Repeat All");
                } else {
                    statsNode.put("repeat", "Repeat Current Song");
                }
            } else {
                if (repeatingMode == 1) {
                    statsNode.put("repeat", "Repeat Once");
                } else {
                    statsNode.put("repeat", "Repeat Infinite");
                }
            }
        } else {
            statsNode.put("repeat", "No Repeat");
        }

        if (!memory.getIsShuffled().containsKey(username)) {
            statsNode.put("shuffle", (boolean) false);
        } else {
            statsNode.put("shuffle", memory.getIsShuffled().get(username) == 1);
        }

        if (statsNode.get("name").toString().equals("\"\"")) {
            statsNode.put("paused", (boolean) true);
        } else {
            statsNode.put("paused", memory.getIsPaused().containsKey(username));
        }

        return commandResult;
    }
}
