package utils;

import entities.Audio;
import user.memory.UserMemory;

public abstract class UpdateRemainingTime {
    /**
     * Update remaining time for an audio file
     *
     * @param username  - user that issued the command
     * @param timestamp - current timestamp
     * @param audio     - currently loaded audio file
     * @param memory    - database
     */
    public static void updateRemainingTime(final String username, final Integer timestamp,
                                           final Audio audio, final UserMemory memory) {
        if (!memory.getLastTimestamp().containsKey(username)) {
            memory.getLastTimestamp().put(username, timestamp);
        }
        Integer lastTimestamp = memory.getLastTimestamp().get(username);
        Integer timePassed = timestamp - lastTimestamp;
        Integer remainingTime = memory.getRemainingTime().get(username);
        memory.getRemainingTime().put(username, remainingTime - timePassed);
    }
}
