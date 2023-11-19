package utils;

import user.memory.UserMemory;

public abstract class UpdateTimestamp {
    /**
     * Put the current timestamp in a hashtable, for the user that issued the command
     *
     * @param username  - user that issued the command
     * @param timestamp - current timestamp
     * @param memory    - database for users
     */
    public static void updateTimestamp(final String username, final Integer timestamp,
                                       final UserMemory memory) {
        memory.getLastTimestamp().put(username, timestamp);
    }
}
