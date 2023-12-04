package command.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.admin.AddUser;
import commands.admin.ShowAlbums;
import commands.artist.AddAlbum;
import commands.artist.AddEventArtist;
import commands.artist.AddMerchArtist;
import commands.artist.RemoveEventArtist;
import commands.player.*;
import commands.playlist.CreatePlaylist;
import commands.playlist.FollowPlaylist;
import commands.playlist.ShowPlaylists;
import commands.playlist.SwitchVisibility;
import commands.search.Search;
import commands.search.SearchFilters;
import commands.search.Select;
import commands.statistics.OnlineUsers;
import commands.statistics.ShowPreferredSongs;
import commands.statistics.TopPlaylists;
import commands.statistics.TopSongs;
import commands.user.PrintCurrentPage;
import commands.user.SwitchConnectionStatus;
import entities.*;
import user.memory.UserMemory;
import utils.UpdatePlayer;
import utils.UpdateTimestamp;

import java.util.EnumSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CommandParser {
    private Library library;
    private ArrayNode outputs;

    /**
     * Method used to parse command and call methods to solve the commands
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     */
    public void parse(final JsonNode currentCommand, final UserMemory memory) {
        String command = getCommand(currentCommand);
        Integer timestamp = getTimestamp(currentCommand);

        if (currentCommand.has("username")
                && memory.getConnectionStatus().containsKey(getUsername(currentCommand))) {
            // TODO DELETE THIS LATER
            int ok = 0;
            for (User user : library.getUsers()) {
                if (user.getUsername().equals(getUsername(currentCommand))) {
                    ok = 1;
                    break;
                }
            }
            if (ok == 0) {
                return;
            }
            boolean isPaused = memory.getIsPaused().containsKey(getUsername(currentCommand));
            boolean update = command.equals("load") || command.equals("next")
                    || command.equals("prev") || command.equals("forward")
                    || command.equals("backward");
            if (!isPaused) {
                UpdatePlayer.updatePlayer(getUsername(currentCommand), timestamp, memory);
            } else if (update) {
                UpdateTimestamp.updateTimestamp(getUsername(currentCommand), timestamp, memory);
            }
        }

        switch (command) {
            case "search":
                searchParse(currentCommand, memory, timestamp);
                break;
            case "select":
                selectParse(currentCommand, memory, timestamp);
                break;
            case "load":
                loadParse(currentCommand, memory, timestamp);
                break;
            case "repeat":
                repeatParse(currentCommand, memory, timestamp);
                break;
            case "playPause":
                pauseParse(currentCommand, memory, timestamp);
                break;
            case "createPlaylist":
                createPlaylistParse(currentCommand, memory, timestamp);
                break;
            case "addRemoveInPlaylist":
                addRemoveParse(currentCommand, memory, timestamp);
                break;
            case "like":
                likeParse(currentCommand, memory, timestamp);
                break;
            case "switchVisibility":
                switchParse(currentCommand, memory, timestamp);
                break;
            case "follow":
                followParse(currentCommand, memory, timestamp);
                break;
            case "showPlaylists":
                showParse(currentCommand, memory, timestamp);
                break;
            case "status":
                statusParse(currentCommand, memory, timestamp);
                break;
            case "showPreferredSongs":
                showPreferredParse(currentCommand, memory, timestamp);
                break;
            case "shuffle":
                shuffleParse(currentCommand, memory, timestamp);
                break;
            case "next":
                nextParse(currentCommand, memory, timestamp);
                break;
            case "prev":
                prevParse(currentCommand, memory, timestamp);
                break;
            case "forward":
                forwardParse(currentCommand, memory, timestamp);
                break;
            case "backward":
                backwardParse(currentCommand, memory, timestamp);
                break;
            case "addUser":
                addUserParse(currentCommand, memory, timestamp);
                break;
            case "printCurrentPage":
                printPageParse(currentCommand, memory, timestamp);
                break;
            case "addAlbum":
                addAlbumParse(currentCommand, memory, timestamp);
                break;
            case "showAlbums":
                showAlbumsParse(currentCommand, memory, timestamp);
                break;
            case "addEvent":
                addEventParse(currentCommand, memory, timestamp);
                break;
            case "removeEvent":
                removeEventParse(currentCommand, memory, timestamp);
                break;
            case "addMerch":
                addMerchParse(currentCommand, memory, timestamp);
                break;
            case "getTop5Songs":
                outputs.add(TopSongs.topSongs(timestamp, memory, library));
                break;
            case "getTop5Playlists":
                outputs.add(TopPlaylists.topPlaylists(timestamp, memory));
                break;
            case "switchConnectionStatus":
                connectionParse(currentCommand, memory, timestamp);
                break;
            case "getOnlineUsers":
                outputs.add(OnlineUsers.getOnlineUsers(memory, timestamp, library));
                break;
            default:
                //System.out.println("Unknown command : " + command);
        }
    }

    /**
     * Get the rest of the fields from "addMerch" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void addMerchParse(JsonNode currentCommand, UserMemory memory, Integer timestamp) {
        String username = getUsername(currentCommand);
        String description = getDescription(currentCommand);
        String name = getName(currentCommand);
        Integer price = getPrice(currentCommand);
        Merch newMerch = new Merch(description, name, price);
        outputs.add(AddMerchArtist.addMerch(username, library, newMerch, timestamp));
    }

    /**
     * Get the rest of the fields from "addEvent" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void removeEventParse(final JsonNode currentCommand, final UserMemory memory,
                                  final Integer timestamp) {
        String username = getUsername(currentCommand);
        String name = getName(currentCommand);
        outputs.add(RemoveEventArtist.removeEvent(username, library, name, timestamp));
    }

    /**
     * Get the rest of the fields from "addEvent" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void addEventParse(final JsonNode currentCommand, final UserMemory memory,
                               final Integer timestamp) {
        String username = getUsername(currentCommand);
        String description = getDescription(currentCommand);
        String name = getName(currentCommand);
        String date = getDate(currentCommand);
        Event newEvent = new Event(description, date, name);
        outputs.add(AddEventArtist.addEvent(username, library, newEvent, timestamp));
    }

    /**
     * Get the rest of the fields from "printCurrentPage" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void printPageParse(final JsonNode currentCommand, final UserMemory memory,
                                final Integer timestamp) {
        String username = getUsername(currentCommand);
        outputs.add(PrintCurrentPage.print(username, memory, timestamp, library));
    }

    /**
     * Get the rest of the fields from "showAlbums" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void showAlbumsParse(final JsonNode currentCommand, final UserMemory memory,
                                 final Integer timestamp) {
        String username = getUsername(currentCommand);
        outputs.add(ShowAlbums.addUser(username, library, timestamp));
    }

    /**
     * Get the rest of the fields from "addAlbum" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void addAlbumParse(final JsonNode currentCommand, final UserMemory memory,
                               final Integer timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        String username = getUsername(currentCommand);
        String name = getName(currentCommand);
        Integer releaseYear = getReleaseYear(currentCommand);
        String description = getDescription(currentCommand);

        JsonNode songs = currentCommand.get("songs");
        ArrayList<Song> newSongs = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++) {
            JsonNode currentSong = songs.get(i);
            String songName = getName(currentSong);
            Integer duration = getDuration(currentSong);
            String albumName = getAlbum(currentSong);
            String lyrics = getLyrics(currentSong);
            String genre = getGenre(currentSong);
            Integer songReleaseYear = getReleaseYear(currentSong);
            String artist = getArtist(currentSong);
            ArrayList<String> tags = new ArrayList<String>();
            tags = mapper.convertValue(currentSong.get("tags"),
                    new TypeReference<ArrayList<String>>() {
                    });
            Song newSong = new Song(songName, duration, albumName,
                    tags, lyrics, genre, songReleaseYear, artist);
            newSongs.add(newSong);
        }
        Album newAlbum = new Album(name, 0, newSongs,
                username, timestamp, description, releaseYear);
        outputs.add(AddAlbum.addAlbum(username, timestamp, newAlbum, memory, library));
    }

    /**
     * Get the rest of the fields from "addUser" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void addUserParse(final JsonNode currentCommand, final UserMemory memory,
                              final Integer timestamp) {
        String username = getUsername(currentCommand);
        Integer age = getAge(currentCommand);
        String city = getCity(currentCommand);
        String type = getType(currentCommand);
        outputs.add(AddUser.addUser(username, city, age, type, memory, timestamp, library));
    }

    /**
     * Get the rest of the fields from "changeConnectionStatus" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void connectionParse(final JsonNode currentCommand, final UserMemory memory,
                                 final Integer timestamp) {
        String username = getUsername(currentCommand);
        outputs.add(SwitchConnectionStatus.switchStatus(username, memory, timestamp, library));
    }

    /**
     * Get the rest of the fields from "backward" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void backwardParse(final JsonNode currentCommand,
                               final UserMemory memory, final Integer timestamp) {
        String username = getUsername(currentCommand);
        outputs.add(Backward.backward(username, memory, timestamp));

    }

    /**
     * Get the rest of the fields from "forward" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void forwardParse(final JsonNode currentCommand,
                              final UserMemory memory, final Integer timestamp) {
        String username = getUsername(currentCommand);
        outputs.add(Forward.forward(username, memory, timestamp));
    }

    /**
     * Get the rest of the fields from "prev" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void prevParse(final JsonNode currentCommand,
                           final UserMemory memory, final Integer timestamp) {
        String username = getUsername(currentCommand);
        outputs.add(Prev.prev(username, memory, timestamp));
    }

    /**
     * Get the rest of the fields from "next" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void nextParse(final JsonNode currentCommand,
                           final UserMemory memory, final Integer timestamp) {
        String username = getUsername(currentCommand);
        outputs.add(Next.next(username, memory, timestamp));
    }

    /**
     * Get the rest of the fields from "shuffle" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void shuffleParse(final JsonNode currentCommand, final UserMemory memory,
                              final Integer timestamp) {
        String username = getUsername(currentCommand);
        int seed = 0;
        if (currentCommand.has("seed")) {
            seed = getSeed(currentCommand);
        }
        outputs.add(Shuffle.shuffle(username, seed, memory, timestamp));
    }

    /**
     * Get the rest of the fields from "repeat" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void repeatParse(final JsonNode currentCommand, final UserMemory memory,
                             final Integer timestamp) {
        String username = getUsername(currentCommand);
        outputs.add(Repeat.repeat(username, memory, timestamp));
    }

    /**
     * Get the rest of the fields from "showPreferredSongs" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void showPreferredParse(final JsonNode currentCommand, final UserMemory memory,
                                    final Integer timestamp) {
        String username = getUsername(currentCommand);
        outputs.add(ShowPreferredSongs.showPreferred(username, memory, timestamp));
    }

    /**
     * Get the rest of the fields from "like" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void likeParse(final JsonNode currentCommand, final UserMemory memory,
                           final Integer timestamp) {
        String username = getUsername(currentCommand);
        outputs.add(Like.like(username, memory, timestamp));
    }

    /**
     * Get the rest of the fields from "addRemoveInPlaylist" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void addRemoveParse(final JsonNode currentCommand, final UserMemory memory,
                                final Integer timestamp) {
        String username = getUsername(currentCommand);
        Integer playlistId = getPlaylistID(currentCommand);
        outputs.add(AddRemoveInPlaylist.addRemove(username, playlistId, memory, timestamp));
    }

    /**
     * Get the rest of the fields from "pause" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void pauseParse(final JsonNode currentCommand, final UserMemory memory,
                            final Integer timestamp) {
        String username = getUsername(currentCommand);
        outputs.add(Pause.pause(username, memory, timestamp));
    }

    /**
     * Get the rest of the fields from "status" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void statusParse(final JsonNode currentCommand, final UserMemory memory,
                             final Integer timestamp) {
        String username = getUsername(currentCommand);
        outputs.add(Status.status(username, memory, timestamp));
    }

    /**
     * Get the rest of the fields from "load" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void loadParse(final JsonNode currentCommand, final UserMemory memory,
                           final Integer timestamp) {
        String username = getUsername(currentCommand);
        outputs.add(Load.load(username, memory, timestamp));
    }

    /**
     * Get the rest of the fields from "show" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void showParse(final JsonNode currentCommand, final UserMemory memory,
                           final Integer timestamp) {
        String username = getUsername(currentCommand);
        outputs.add(ShowPlaylists.show(username, memory, timestamp));
    }

    /**
     * Get the rest of the fields from "follow" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void followParse(final JsonNode currentCommand, final UserMemory memory,
                             final Integer timestamp) {
        String username = getUsername(currentCommand);
        outputs.add(FollowPlaylist.follow(username, memory, timestamp));
    }

    /**
     * Get the rest of the fields from "switchVisibility" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void switchParse(final JsonNode currentCommand, final UserMemory memory,
                             final Integer timestamp) {
        String username = getUsername(currentCommand);
        Integer playlistID = getPlaylistID(currentCommand);
        outputs.add(SwitchVisibility.switchVisibility(username, playlistID, timestamp, memory));
    }

    /**
     * Get the rest of the fields from "select" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void selectParse(final JsonNode currentCommand, final UserMemory memory,
                             final Integer timestamp) {
        String username = getUsername(currentCommand);
        Integer itemNumber = getItemNumber(currentCommand);
        outputs.add(Select.select(username, itemNumber, timestamp, memory));
    }

    /**
     * Get the rest of the fields from "search" command and call method
     * to solve the command.
     *
     * @param currentCommand - command from input file
     * @param memory         - database
     * @param timestamp      - timestamp from command
     */
    private void searchParse(final JsonNode currentCommand, final UserMemory memory,
                             final Integer timestamp) {
        ObjectMapper objectMapper = new ObjectMapper();
        String type = getType(currentCommand);
        JsonNode filtersNode = currentCommand.get("filters");

        List<String> tags = new ArrayList<String>();
        boolean checkTags = filtersNode.has("tags");

        if (checkTags) {
            JsonNode tagsNode = filtersNode.get("tags");
            tags = objectMapper.convertValue(tagsNode, new TypeReference<List<String>>() {
            });
        }

        Map<String, String> otherFilters = new HashMap<>();
        for (SearchFilters filter : EnumSet.allOf(SearchFilters.class)) {
            if (filtersNode.has(filter.toString()) && !filter.toString().equals("tags")) {
                otherFilters.put(filter.toString(),
                        filtersNode.get(filter.toString()).toString().replace("\"", ""));
            }
        }

        String username = getUsername(currentCommand);
        outputs.add(Search.search(username, type, tags, otherFilters,
                library, getTimestamp(currentCommand), memory));
    }

    private void createPlaylistParse(final JsonNode currentCommand, final UserMemory memory,
                                     final Integer timestamp) {
        ObjectMapper objectMapper = new ObjectMapper();
        String playlistName = getPlaylistName(currentCommand);
        outputs.add(CreatePlaylist.createPlaylist(getUsername(currentCommand),
                playlistName, timestamp, memory));
    }

    /**
     * @return value for name field
     */
    private String getName(final JsonNode currentCommand) {
        return currentCommand.get("name").toString().replace("\"", "");
    }

    /**
     * @return value for artist field
     */
    private String getArtist(final JsonNode currentCommand) {
        return currentCommand.get("artist").toString().replace("\"", "");
    }

    /**
     * @return value for lyrics field
     */
    private String getLyrics(final JsonNode currentCommand) {
        return currentCommand.get("lyrics").toString().replace("\"", "");
    }

    /**
     * @return value for playlistName field
     */
    private String getPlaylistName(final JsonNode currentCommand) {
        return currentCommand.get("playlistName").toString().replace("\"", "");
    }

    /**
     * @return value for genre field
     */
    private String getGenre(final JsonNode currentCommand) {
        return currentCommand.get("genre").toString().replace("\"", "");
    }

    /**
     * @return value for releaseYear field
     */
    private Integer getReleaseYear(final JsonNode currentCommand) {
        return currentCommand.get("releaseYear").asInt();
    }

    /**
     * @return value for price field
     */
    private Integer getPrice(final JsonNode currentCommand) {
        return currentCommand.get("price").asInt();
    }

    /**
     * @return value for type field
     */
    private String getType(final JsonNode currentCommand) {
        return currentCommand.get("type").toString().replace("\"", "");
    }

    /**
     * @return value for username field
     */
    private String getUsername(final JsonNode currentCommand) {
        return currentCommand.get("username").toString().replace("\"", "");
    }

    /**
     * @return value for age field
     */
    private Integer getAge(final JsonNode currentCommand) {
        return currentCommand.get("age").asInt();
    }

    /**
     * @return value for duration field
     */
    private Integer getDuration(final JsonNode currentCommand) {
        return currentCommand.get("duration").asInt();
    }

    /**
     * @return value for city field
     */
    private String getCity(final JsonNode currentCommand) {
        return currentCommand.get("city").toString().replace("\"", "");
    }

    /**
     * @return value for command field
     */
    private String getCommand(final JsonNode currentCommand) {
        return currentCommand.get("command").toString().replace("\"", "");
    }

    /**
     * @return value for date field
     */
    private String getDate(final JsonNode currentCommand) {
        return currentCommand.get("date").toString().replace("\"", "");
    }

    /**
     * @return value for itemNumber field
     */
    private Integer getItemNumber(final JsonNode currentCommand) {
        return currentCommand.get("itemNumber").asInt();
    }

    /**
     * @return value for timestamp field
     */
    private Integer getTimestamp(final JsonNode currentCommand) {
        return currentCommand.get("timestamp").asInt();
    }

    /**
     * @return value for playlistId field
     */
    private Integer getPlaylistID(final JsonNode currentCommand) {
        return currentCommand.get("playlistId").asInt();
    }

    /**
     * @return value for description field
     */
    private String getDescription(final JsonNode currentCommand) {
        return currentCommand.get("description").toString().replace("\"", "");
    }

    /**
     * @return value for album field
     */
    private String getAlbum(final JsonNode currentCommand) {
        return currentCommand.get("album").toString().replace("\"", "");
    }

    /**
     * @return value for seed field
     */
    private Integer getSeed(final JsonNode currentCommand) {
        return currentCommand.get("seed").asInt();
    }

    public CommandParser(final Library library, final ArrayNode outputs) {
        this.library = library;
        this.outputs = outputs;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(final Library library) {
        this.library = library;
    }

    public ArrayNode getOutputs() {
        return outputs;
    }

    public void setOutputs(final ArrayNode outputs) {
        this.outputs = outputs;
    }
}
