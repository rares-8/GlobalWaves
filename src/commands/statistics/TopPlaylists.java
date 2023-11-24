package commands.statistics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Playlist;
import user.memory.UserMemory;
import utils.CountFollowers;
import java.util.ArrayList;
import java.util.Collections;

import static utils.Constants.RESULT_MAX_SIZE;

public abstract class TopPlaylists {
    /**
     *
     * @param timestamp - current timestamp
     * @param memory - database
     * @return top 5 playlists
     */
    public static JsonNode topPlaylists(final Integer timestamp, final UserMemory memory) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "getTop5Playlists");
        commandResult.put("timestamp", timestamp);
        ArrayNode resultNode = commandResult.putArray("result");

        ArrayList<Playlist> allPlaylists = new ArrayList<>();

        // copy playlist names and owners (needed to count followers)
        for (Playlist playlist : memory.getPublicPlaylists()) {
            Playlist newPlaylist = new Playlist();
            newPlaylist.setName(playlist.getName());
            newPlaylist.setOwner(playlist.getOwner());
            newPlaylist.setTimeCreated(playlist.getTimeCreated());
            allPlaylists.add(newPlaylist);
        }

        // sort playlists by oldest to newest
        for (int i = 0; i < allPlaylists.size() - 1; i++) {
            for (int j = i + 1; j < allPlaylists.size(); j++) {
                if (allPlaylists.get(i).getTimeCreated() > allPlaylists.get(j).getTimeCreated()) {
                    Collections.swap(allPlaylists, i, j);
                }
            }
        }

        // get followers for all playlists
        ArrayList<Integer> followersCount = new ArrayList<>();
        for (Playlist playlist : allPlaylists) {
            followersCount.add(CountFollowers.countFollowers(memory.getFollowedPlaylists(),
                    playlist));
        }

        ArrayList<Playlist> sortedPlaylists = new ArrayList<>();
        for (int i = 0; i < memory.getPublicPlaylists().size(); i++) {
            int maxFollowers = -1;
            for (Integer iterator : followersCount) {
                if (maxFollowers < iterator) {
                    maxFollowers = iterator;
                }
            }
            int index = followersCount.indexOf(maxFollowers);
            sortedPlaylists.add(allPlaylists.get(index));
            allPlaylists.remove(index);
            followersCount.remove(index);
        }

        if (sortedPlaylists.size() > RESULT_MAX_SIZE) {
            sortedPlaylists = new ArrayList<>(sortedPlaylists.subList(0, RESULT_MAX_SIZE));
        }

        for (Playlist sortedPlaylist : sortedPlaylists) {
            resultNode.add(sortedPlaylist.getName());
        }
        return commandResult;
    }
}
