package utils;

import entities.Audio;
import entities.Playlist;

import java.util.ArrayList;
import java.util.Map;

public abstract class CountFollowers {
    /**
     * Count followers for a given playlist
     *
     * @param followedPlaylists - playlists a user follows
     * @param playlist          - playlist to check followers for
     * @return number of followers
     */
    public static Integer countFollowers(final Map<String, ArrayList<Audio>> followedPlaylists,
                                         final Playlist playlist) {
        Integer count = 0;
        for (Map.Entry<String, ArrayList<Audio>> element : followedPlaylists.entrySet()) {
            ArrayList<Audio> userFollows = element.getValue();
            if (userFollows != null) {
                for (Audio currentPlaylist : userFollows) {
                    if (currentPlaylist.getName().equals(playlist.getName())
                            && currentPlaylist.getOwner().equals(playlist.getOwner())) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
