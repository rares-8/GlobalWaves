package utils;

import fileio.input.Audio;
import user.memory.UserMemory;

import java.util.Map;

public abstract class UpdateRemainingTime {
    public static void updateRemainingTime(String username, Integer timestamp, Audio audio, UserMemory memory) {
        if (!memory.getLastTimestamp().containsKey(username)) {
            memory.getLastTimestamp().put(username, timestamp);
        }
        Integer lastTimestamp = memory.getLastTimestamp().get(username);
        Integer timePassed = timestamp - lastTimestamp;
        audio.setDuration(audio.getDuration() - timePassed);
        memory.getRemainingTime().put(username, audio.getDuration());
    }
}
