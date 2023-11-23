package command.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.player.*;
import commands.playlist.CreatePlaylist;
import commands.playlist.FollowPlaylist;
import commands.playlist.ShowPlaylists;
import commands.playlist.SwitchVisibility;
import commands.search.Search;
import commands.search.SearchFilters;
import commands.search.Select;
import commands.statistics.ShowPreferredSongs;
import commands.statistics.TopPlaylists;
import commands.statistics.TopSongs;
import entities.Library;
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
     * @param memory         - database for users
     */
    public void parse(final JsonNode currentCommand, final UserMemory memory) {
        String command = getCommand(currentCommand);
        Integer timestamp = getTimestamp(currentCommand);

        if (currentCommand.has("username")) {
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
            case "getTop5Songs":
                outputs.add(TopSongs.topSongs(timestamp, memory, library));
                break;
            case "getTop5Playlists":
                outputs.add(TopPlaylists.topPlaylists(timestamp, memory));
                break;
            default:
                System.out.println("Unknown command : " + command);
        }
    }

    /**
     * Get the rest of the fields from "backward" command and call method
     * to solve the command
     *
     * @param currentCommand - command from input file
     * @param memory         - memory database for users
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
     * @param memory         - memory database for users
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
     * @param memory         - memory database for users
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
     * @param memory         - memory database for users
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
     * @param memory         - memory database for users
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
     * @param memory         - memory database for users
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
     * @param memory         - memory database for users
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
     * @param memory         - memory database for users
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
     * @param memory         - memory database for users
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
     * @param memory         - memory database for users
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
     * @param memory         - memory database for users
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
     * @param memory         - memory database for users
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
     * @param memory         - memory database for users
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
     * @param memory         - memory database for users
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
     * @param memory         - memory database for users
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
     * @param memory         - memory database for users
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
     * @param memory         - memory database for users
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

    private String getPlaylistName(final JsonNode currentCommand) {
        return currentCommand.get("playlistName").toString().replace("\"", "");
    }

    private String getType(final JsonNode currentCommand) {
        return currentCommand.get("type").toString().replace("\"", "");
    }

    private String getUsername(final JsonNode currentCommand) {
        return currentCommand.get("username").toString().replace("\"", "");
    }

    private String getCommand(final JsonNode currentCommand) {
        return currentCommand.get("command").toString().replace("\"", "");
    }

    private Integer getItemNumber(final JsonNode currentCommand) {
        return currentCommand.get("itemNumber").asInt();
    }

    private Integer getTimestamp(final JsonNode currentCommand) {
        return currentCommand.get("timestamp").asInt();
    }

    private Integer getPlaylistID(final JsonNode currentCommand) {
        return currentCommand.get("playlistId").asInt();
    }

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
