package command.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.CreatePlaylist;
import commands.SearchCommand;
import commands.SelectCommand;
import commands.SwitchVisibility;
import fileio.input.LibraryInput;
import user.memory.UserMemory;

import java.util.EnumSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CommandParser {
    private LibraryInput library;
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

        switch (command) {
            case "search":
                searchParse(currentCommand, memory, timestamp);
                break;
            case "select":
                selectParse(currentCommand, memory, timestamp);
                break;
            case "createPlaylist":
                createPlaylistParse(currentCommand, memory, timestamp);
                break;
            case "switchVisibility":
                switchParse(currentCommand, memory, timestamp);
                break;
            default:
                System.out.println("Unknown command : " + command);
        }
    }

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
        outputs.add(SelectCommand.select(username, itemNumber, timestamp, memory));
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
        outputs.add(SearchCommand.search(username, type, tags, otherFilters,
                library, getTimestamp(currentCommand), memory));
    }

    private void createPlaylistParse(final JsonNode currentCommand, final UserMemory memory,
                                     final Integer timestamp) {
        ObjectMapper objectMapper = new ObjectMapper();
        String playlistName = getPlaylistName(currentCommand);
        outputs.add(CreatePlaylist.createPlaylist(getUsername(currentCommand),
                playlistName, timestamp, memory));
    }

    private String getPlaylistName(JsonNode currentCommand) {
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

    private Integer getPlaylistID(JsonNode currentCommand) {
        return currentCommand.get("playlistId").asInt();
    }

    public CommandParser(final LibraryInput library, final ArrayNode outputs) {
        this.library = library;
        this.outputs = outputs;
    }

    public LibraryInput getLibrary() {
        return library;
    }

    public void setLibrary(final LibraryInput library) {
        this.library = library;
    }

    public ArrayNode getOutputs() {
        return outputs;
    }

    public void setOutputs(final ArrayNode outputs) {
        this.outputs = outputs;
    }
}
