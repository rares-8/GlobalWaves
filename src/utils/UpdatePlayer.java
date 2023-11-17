package utils;

import fileio.input.Audio;
import user.memory.UserMemory;

public class UpdatePlayer {
    public static void updatePlayer(String username, Integer timestamp, UserMemory memory) {
        if (!memory.getLoadedAudio().containsKey(username)) {
            UpdateTimestamp.updateTimestamp(username, timestamp, memory);
            return;
        }
        if (!memory.getLoadedAudio().get(username).getAudioType().equals("song"))
            return;

        UpdateRemainingTime.updateRemainingTime(username, timestamp, memory.getLoadedAudio().get(username), memory);
        UpdateTimestamp.updateTimestamp(username, timestamp, memory);

        if (memory.getRemainingTime().get(username) < 0) {
            memory.getLoadedAudio().remove(username);
        }
    }
}
