package commandParser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.SearchCommand;
import fileio.input.LibraryInput;
import fileio.input.SongInput;

import java.util.*;

public final class CommandParser {
    private static CommandParser uniqueInstance = null;
    private LibraryInput library;
    private ArrayNode outputs;

    public void parse(JsonNode currentCommand) {
        String command = getCommand(currentCommand);
        Integer timestamp = getTimestamp(currentCommand);

        switch (command) {
            case "search":
                searchParse(currentCommand);
                break;
            default:
                System.out.println("Unknown " + command);
        }
    }

    /**
     * Get the rest of the fields from "search" command and call method
     * to solve the command.
     */
    private void searchParse(JsonNode currentCommand) {
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
            if (filtersNode.has(filter.toString()) && !filter.toString().equals("tags"))
                otherFilters.put(filter.toString(), filtersNode.get(filter.toString()).toString().replace("\"", ""));
        }

        String username = getUsername(currentCommand);
        outputs.add(SearchCommand.search(username, type, tags, otherFilters, library, getTimestamp(currentCommand)));
    }

    private String getType(JsonNode currentCommand) {
        return currentCommand.get("type").toString().replace("\"", "");
    }

    private String getUsername(JsonNode currentCommand) {
        return currentCommand.get("username").toString().replace("\"", "");
    }

    private String getCommand(JsonNode currentCommand) {
        return currentCommand.get("command").toString().replace("\"", "");
    }

    private Integer getTimestamp(JsonNode currentCommand) {
        return currentCommand.get("timestamp").asInt();
    }

    public CommandParser(LibraryInput library, ArrayNode outputs) {
        this.library = library;
        this.outputs = outputs;
    }

    public LibraryInput getLibrary() {
        return library;
    }

    public void setLibrary(LibraryInput library) {
        this.library = library;
    }

    public ArrayNode getOutputs() {
        return outputs;
    }

    public void setOutputs(ArrayNode outputs) {
        this.outputs = outputs;
    }
}
