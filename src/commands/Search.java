package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import utils.Constants;
import fileio.input.*;
import user.memory.UserMemory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public abstract class Search implements Constants {

    /**
     * @param username     - user that issued the command
     * @param type         - search type (podcast / song / playlist)
     * @param tags         - tags in case of search for a song
     * @param otherFilters - all other filters except tags
     * @param library      - library with all users, podcasts, songs
     * @param timestamp    - timestamp for command
     * @param memory       - database for users
     * @return command result
     */
    public static JsonNode search(final String username, final String type,
                                  final List<String> tags, final Map<String, String> otherFilters,
                                  final LibraryInput library, final Integer timestamp,
                                  final UserMemory memory) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "search");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        ArrayNode auxNode;

        ArrayList<Audio> audioResult = new ArrayList<>();
        searchAudio(username, tags, library, otherFilters, audioResult, type, memory);

        if (audioResult.size() > SEARCH_MAX_SIZE) {
            audioResult = new ArrayList<>(audioResult.subList(0, SEARCH_MAX_SIZE));
        }

        memory.getLastSearch().put(username, audioResult);
        commandResult.put("message", "Search returned " + audioResult.size() + " results");
        auxNode = commandResult.putArray("results");
        for (Audio audio : audioResult) {
            auxNode.add(audio.getName());
        }

        return commandResult;
    }

    /**
     * @param username
     * @param tags         - tags in case of search for a song
     * @param library      - library with all users, podcasts, songs
     * @param otherFilters - all other filters except tags
     * @param audioResult  - list that contains all files that match the search filters
     * @param type         - search type
     * @param memory       - memory for users
     */
    private static void searchAudio(String username, final List<String> tags,
                                    final LibraryInput library,
                                    final Map<String, String> otherFilters,
                                    final ArrayList<Audio> audioResult, final String type,
                                    final UserMemory memory) {
        /*
            First search for podcasts, easier to do
         */
        if (type.equals("podcast")) {
            for (PodcastInput currentPodcast : library.getPodcasts()) {
                for (Map.Entry<String, String> element : otherFilters.entrySet()) {
                    searchAudioByFilter(element, currentPodcast, audioResult);
                }
            }
            return;
        }

        /*
            Next search for playlists
         */
        if (type.equals("playlist")) {
            for (PlaylistInput currentPlaylist : memory.getPublicPlaylists()) {
                for (Map.Entry<String, String> element : otherFilters.entrySet()) {
                    searchAudioByFilter(element, currentPlaylist, audioResult);
                }
            }
            if (!memory.getUserPlaylists().containsKey(username))
                return;

            for (PlaylistInput currentPlaylist : memory.getUserPlaylists().get(username))
                if (currentPlaylist.getIsPrivate() == 1) {
                    for (Map.Entry<String, String> element : otherFilters.entrySet()) {
                        searchAudioByFilter(element, currentPlaylist, audioResult);
                    }
                }
            audioResult.sort(new Comparator<Audio>() {
                @Override
                public int compare(Audio o1, Audio o2) {
                    return o1.getReleaseYear() - o2.getReleaseYear();
                }
            });
            return;
        }

        // search for songs
        for (SongInput currentSong : library.getSongs()) {
            for (Map.Entry<String, String> element : otherFilters.entrySet()) {
                searchAudioByFilter(element, currentSong, audioResult);
            }
        }

        /*
            If songResult is empty, then search by tags, and put all results in songResult
         */
        if (audioResult.isEmpty() && !tags.isEmpty()) {
            for (SongInput currentSong : library.getSongs()) {
                int ok = 1;
                List<String> currentSongTags = currentSong.getTags();
                for (String tag : tags) {
                    if (!currentSongTags.contains(tag)) {
                        ok = 0;
                        break;
                    }
                }
                if (ok == 1) {
                    audioResult.add(currentSong);
                }
            }
        }
        /*
            If songResult is not empty, then remove the songs that don't contain the given tags
         */
        if (!audioResult.isEmpty() && !tags.isEmpty()) {
            List<Audio> audiosToRemove = new ArrayList<>();
            for (Audio currentSong : audioResult) {
                int ok = 1;
                List<String> currentSongTags = currentSong.getTags();
                for (String tag : tags) {
                    if (!currentSongTags.contains(tag)) {
                        ok = 0;
                        break;
                    }
                }
                if (ok == 0) {
                    audiosToRemove.add(currentSong);
                }
            }
            audioResult.removeAll(audiosToRemove);
        }
    }

    /**
     * Loop through all the possible filters and check if they match with audio values
     *
     * @param element      - [key, value] pair for a filter
     * @param currentAudio - checked to see if it matches filters
     * @param result       - search result
     */
    private static void searchAudioByFilter(final Map.Entry<String, String> element,
                                            final Audio currentAudio,
                                            final ArrayList<Audio> result) {
        String keyword = element.getKey();
        switch (keyword) {
            case "name":
                if (currentAudio.getName().startsWith(element.getValue())) {
                    result.add(currentAudio);
                }
                break;
            case "album":
                if (currentAudio.getAlbum().equalsIgnoreCase(element.getValue())) {
                    result.add(currentAudio);
                }
                break;
            case "lyrics":
                if (currentAudio.getLyrics().contains(element.getValue())) {
                    result.add(currentAudio);
                }
                break;
            case "genre":
                if (currentAudio.getGenre().equalsIgnoreCase(element.getValue())) {
                    result.add(currentAudio);
                }
                break;
            case "releaseYear":
                char operator = element.getValue().charAt(0);
                String releaseYearString = element.getValue().substring(1);
                int releaseYear = Integer.parseInt(releaseYearString);
                if (releaseYear < currentAudio.getReleaseYear() && operator == '>'
                        || releaseYear > currentAudio.getReleaseYear() && operator == '<') {
                    result.add(currentAudio);
                }
                break;
            case "artist":
                if (currentAudio.getArtist().equalsIgnoreCase(element.getValue())) {
                    result.add(currentAudio);
                }
                break;
            case "owner":
                if (currentAudio.getOwner().equalsIgnoreCase(element.getValue())) {
                    result.add(currentAudio);
                }
                break;
            default:
                System.out.println("Unknown filter");
        }
    }
}
