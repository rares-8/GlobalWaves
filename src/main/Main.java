package main;

import checker.Checker;
import checker.CheckerConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commandsParser.SearchCommand;
import commandsParser.SelectCommand;
import fileio.input.LibraryInput;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    static final String LIBRARY_PATH = CheckerConstants.TESTS_PATH + "library/library.json";

    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     *
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.getName().startsWith("library")) {
                continue;
            }

            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        LibraryInput library = objectMapper.readValue(new File(LIBRARY_PATH), LibraryInput.class);

        ArrayNode outputs = objectMapper.createArrayNode();

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), outputs);

        JsonNode obj = objectMapper.readTree(new File("input/" + filePath1));

        String username;
        String commandValue;
        Integer timestamp;
        for (int i = 0; i < obj.size(); i++) {
            JsonNode currentNode = obj.get(i);

            commandValue = currentNode.get("command").toString();
            commandValue = commandValue.replace("\"", "");

            if (commandValue.equals("getTop5Songs") || commandValue.equals("getTop5Playlists"))
                continue;

            username = currentNode.get("username").toString();
            username = username.replace("\"", "");

            timestamp = currentNode.get("timestamp").asInt();
            switch (commandValue) {
                case "search":
                    String type = currentNode.get("type").toString().replace("\"", "");
                    Map<String, Object> filters = objectMapper.convertValue(currentNode.get("filters"), new TypeReference<Map<String, Object>>(){});
                    SearchCommand search = new SearchCommand(commandValue, username, timestamp, type, filters);
                    break;
                case "select":
                    Integer itemNumber = currentNode.get("itemNumber").asInt();
                    SelectCommand selectCommand = new SelectCommand(commandValue, username, timestamp, itemNumber);
                    break;
                default:
                    //System.out.println("Unknown command");
            }
        }

        System.out.println(filePath1);
        //System.out.println(obj.size());
        System.out.println("------------------------------");

    }
}
