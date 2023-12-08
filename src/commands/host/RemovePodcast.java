package commands.host;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.*;
import entities.pages.Page;
import user.memory.UserMemory;
import utils.CheckUser;

import java.util.ArrayList;
import java.util.Map;

public abstract class RemovePodcast {
    /**
     * @param username    - username of artist owning the album
     * @param podcastName - podcast that should be deleted
     * @param memory      - database
     * @param timestamp   - current timestamp
     * @param library     - contains songs, playlists, podcasts, users
     * @return delete album status
     */
    public static JsonNode removePodcast(final String username, final String podcastName,
                                         final Integer timestamp, final UserMemory memory,
                                         final Library library) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "removePodcast");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        boolean ok = CheckUser.checkUser(username, library);

        if (!ok) {
            commandResult.put("message", "The username " + username + " doesn't exist.");
            return commandResult;
        }

        User host = null;
        for (User user : library.getUsers()) {
            if (user.getUsername().equals(username)) {
                host = user;
            }
        }

        if (!host.getType().equals("host")) {
            commandResult.put("message", username + " is not an artist.");
            return commandResult;
        }

        ok = false;
        for (Podcast podcast : host.getPodcasts()) {
            if (podcast.getName().equals(podcastName)) {
                ok = true;
                break;
            }
        }

        if (!ok) {
            commandResult.put("message", username + " doesn't have a podcast with the given name.");
            return commandResult;
        }

        /* podcast cannot be deleted if user is listening it*/
        for (User currentUser : library.getUsers()) {
            if (memory.getLoadedAudio().containsKey(currentUser.getUsername())) {
                Audio loadedAudio = memory.getLoadedAudio().get(currentUser.getUsername());
                if (loadedAudio.getAudioType().equals("podcast")
                        && loadedAudio.getOwner().equals(username)) {
                    ok = false;
                    break;
                }
            }
        }

        if (!ok) {
            commandResult.put("message", username + " can't delete this podcast.");
        } else {
            clearPodcast(host, podcastName, memory, library);
            commandResult.put("message", username + " deleted the podcast successfully.");
        }

        return commandResult;
    }

    /**
     * Clear everything associated with a podcast
     *
     * @param host        - username of host that owns the album
     * @param podcastName - podcast that should be deleted
     * @param memory      - database
     * @param library     - contains songs, playlists, podcasts, users
     */
    private static void clearPodcast(final User host, final String podcastName,
                                   final UserMemory memory, final Library library) {
        for (User user : library.getUsers()) {
            if (memory.getLoadedPodcasts().containsKey(user.getUsername())) {
                ArrayList<Podcast> loadedPodcasts =
                        memory.getLoadedPodcasts().get(user.getUsername());
                for (Podcast podcast : loadedPodcasts) {
                    if (podcast.getOwner().equals(host.getUsername())
                            && podcast.getName().equals(podcastName)) {
                        int idx = loadedPodcasts.indexOf(podcast);
                        memory.getLoadedPodcasts().get(user.getUsername()).remove(idx);
                        memory.getEpisodeRemainingTime().get(user.getUsername()).remove(idx);
                        memory.getLastEpisodes().get(user.getUsername()).remove(idx);
                    }
                }
            }
        }
        library.getPodcasts().removeIf(podcast -> podcast.getOwner().equals(host.getUsername()) && podcast.getName().equals(podcastName));
        host.getPodcasts().removeIf(podcast -> podcast.getName().equals(podcastName));
    }
}
