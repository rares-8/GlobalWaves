package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.Audio;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import userMemory.UserMemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class SearchCommand {

    public static JsonNode search(String username, String type, List<String> tags, Map<String, String> otherFilters, LibraryInput library, Integer timestamp, UserMemory memory) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "search");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        ArrayNode auxNode;
        if (type.equals("podcast") || type.equals("song")) {
            ArrayList<Audio> audioResult = new ArrayList<>();
            searchAudio(tags, library, otherFilters, audioResult, type);
            if (audioResult.size() > 5)
                audioResult = new ArrayList<>(audioResult.subList(0, 5));
            memory.getLastSearch().put(username, audioResult);
            commandResult.put("message", "Search returned " + audioResult.size() + " results");
            auxNode = commandResult.putArray("results");
            for (Audio audio : audioResult)
                auxNode.add(audio.getName());
        } else if (type.equals("playlist")) {
            // do this later
        }

        return commandResult;
    }

    private static void searchAudio(List<String> tags, LibraryInput library, Map<String, String> otherFilters, ArrayList<Audio> audioResult, String type) {
        // first search for podcasts, easier to do
        if (type.equals("podcast")) {
            for (PodcastInput currentPodcast : library.getPodcasts()) {
                for (Map.Entry<String, String> element : otherFilters.entrySet())
                    searchAudioByFilter(element, currentPodcast, audioResult);
            }
            return;
        }
        // get all songs with given filters, except tags, and put them in songs result
        for (SongInput currentSong : library.getSongs()) {
            for (Map.Entry<String, String> element : otherFilters.entrySet())
                searchAudioByFilter(element, currentSong, audioResult);
        }
        // if songResult is empty, then search by tags
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
                if (ok == 1)
                    audioResult.add(currentSong);
            }
        }
        // if songResult is not empty, then remove the songs that don't contain the given tags
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
                if (ok == 0)
                    audiosToRemove.add(currentSong);
            }
            audioResult.removeAll(audiosToRemove);
        }
    }

    private static void searchAudioByFilter(Map.Entry<String, String> element, Audio currentAudio, ArrayList<Audio> result) {
        String keyword = element.getKey();
        switch (keyword) {
            case "name":
                if (currentAudio.getName().startsWith(element.getValue()))
                    result.add(currentAudio);
                break;
            case "album":
                if (currentAudio.getAlbum().equalsIgnoreCase(element.getValue()))
                    result.add(currentAudio);
                break;
            case "lyrics":
                if (currentAudio.getLyrics().contains(element.getValue()))
                    result.add(currentAudio);
                break;
            case "genre":
                if (currentAudio.getGenre().equalsIgnoreCase(element.getValue()))
                    result.add(currentAudio);
                break;
            case "releaseYear":
                char operator = element.getValue().charAt(0);
                String releaseYearString = element.getValue().substring(1);
                int releaseYear = Integer.parseInt(releaseYearString);
                if (releaseYear < currentAudio.getReleaseYear() && operator == '>' ||
                        releaseYear > currentAudio.getReleaseYear() && operator == '<')
                    result.add(currentAudio);
                break;
            case "artist":
                if (currentAudio.getArtist().equalsIgnoreCase(element.getValue()))
                    result.add(currentAudio);
                break;
            case "owner":
                if (currentAudio.getOwner().equalsIgnoreCase(element.getValue()))
                    result.add(currentAudio);
                break;
            default:
                System.out.println("Unknown filter");
        }
    }
}
