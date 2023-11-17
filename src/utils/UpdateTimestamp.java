package utils;

import user.memory.UserMemory;

import java.util.ArrayList;

public abstract class UpdateTimestamp {
    public static void updateTimestamp(String username, Integer timestamp, UserMemory memory) {
        memory.getLastTimestamp().put(username, timestamp);
    }
}
