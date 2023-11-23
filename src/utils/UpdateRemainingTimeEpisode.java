package utils;

import entities.Episode;
import user.memory.UserMemory;

public abstract class UpdateRemainingTimeEpisode {
    /**
     * Update remaining time for a pdocast episode
     *
     * @param username  - user that issued the command
     * @param timestamp - current timestamp
     * @param episode   - currently loaded episode file
     * @param memory    - user database
     */
    public static void updateEp(final String username, final Integer timestamp,
                                final Episode episode, final int podcastIndex,
                                final UserMemory memory) {
        if (!memory.getLastTimestamp().containsKey(username)) {
            memory.getLastTimestamp().put(username, timestamp);
        }

        Integer lastTimestamp = memory.getLastTimestamp().get(username);
        Integer timePassed = timestamp - lastTimestamp;
        Integer remainingTime = memory.getEpisodeRemainingTime().get(username).get(podcastIndex);
        memory.getEpisodeRemainingTime().get(username).set(podcastIndex,
                remainingTime - timePassed);
    }
}
