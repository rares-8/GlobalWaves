package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.LibraryInput;
import fileio.input.SongInput;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class SearchCommand {

    public static JsonNode search(String username, String type, List<String> tags, Map<String, String> otherFilters, LibraryInput library, Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "search");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        switch (type) {
            case "song":
                List<SongInput> songsResult = new ArrayList<SongInput>();
                searchSongs(tags, library, otherFilters, songsResult);
                if (songsResult.size() > 5)
                    songsResult = songsResult.subList(0, 5);
                commandResult.put("message", "Search returned " + songsResult.size() + " results");
                ArrayNode auxNode = commandResult.putArray("results");
                for (SongInput song : songsResult)
                    auxNode.add(song.getName());
                break;
            case "podcast":
                break;
            case "playlist":
                // implement later
                break;
            default:
                System.out.println("Unknown type");
        }
        return commandResult;
    }

    private static void searchSongs(List<String> tags, LibraryInput library, Map<String, String> otherFilters, List<SongInput> songsResult) {
        // get all songs with given filters, except tags, and put them in songs result
        for (SongInput currentSong : library.getSongs()) {
            for (Map.Entry<String, String> element : otherFilters.entrySet())
                searchByFilter(element, currentSong, otherFilters, songsResult);
        }
        // if songResult is empty, then search by tags
        if (songsResult.isEmpty() && !tags.isEmpty()) {
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
                    songsResult.add(currentSong);
            }
        }
        // if songResult is not empty, then remove the songs that don't contain the given tags
        if (!songsResult.isEmpty() && !tags.isEmpty()) {
            List<SongInput> songsToRemove = new ArrayList<SongInput>();
            for (SongInput currentSong : songsResult) {
                int ok = 1;
                List<String> currentSongTags = currentSong.getTags();
                for (String tag : tags) {
                    if (!currentSongTags.contains(tag)) {
                        ok = 0;
                        break;
                    }
                }
                if (ok == 0)
                    songsToRemove.add(currentSong);
            }
            songsResult.removeAll(songsToRemove);
        }
    }

    private static void searchByFilter(Map.Entry<String, String> element, SongInput currentSong, Map<String, String> filters, List<SongInput> result) {
        String keyword = element.getKey();
        switch (keyword) {
            case "name":
                if (currentSong.getName().startsWith(element.getValue()))
                    result.add(currentSong);
                break;
            case "album":
                if (currentSong.getAlbum().equalsIgnoreCase(element.getValue()))
                    result.add(currentSong);
                break;
            case "lyrics":
                if (currentSong.getLyrics().contains(element.getValue()))
                    result.add(currentSong);
                break;
            case "genre":
                if (currentSong.getGenre().equalsIgnoreCase(element.getValue()))
                    result.add(currentSong);
                break;
            case "releaseYear":
                char operator = element.getValue().charAt(0);
                String releaseYearString = element.getValue().substring(1);
                int releaseYear = Integer.parseInt(releaseYearString);
                if (releaseYear < currentSong.getReleaseYear() && operator == '>' ||
                        releaseYear > currentSong.getReleaseYear() && operator == '<')
                    result.add(currentSong);
                break;
            case "artist":
                if (currentSong.getArtist().equalsIgnoreCase(element.getValue()))
                    result.add(currentSong);
                break;
            default:
                System.out.println("Unknown filter");
        }
    }
}
