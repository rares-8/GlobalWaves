package main;

import checker.Checker;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import command.parser.CommandParser;
import entities.Library;
import entities.Episode;
import entities.User;
import entities.Song;
import entities.Podcast;
import entities.pages.HomePage;
import fileio.input.EpisodeInput;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import user.memory.UserMemory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
     * @param filePathInput for input file
     * @param filePathOutput for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePathInput,
                              final String filePathOutput) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        LibraryInput library = objectMapper.readValue(new File(LIBRARY_PATH), LibraryInput.class);

        ArrayNode outputs = objectMapper.createArrayNode();

        UserMemory memory = UserMemory.getInstance();
        Library myLibrary = new Library();
        for (UserInput user : library.getUsers()) {
            User newUser = new User(user.getUsername(), user.getAge(), user.getCity(), "user");
            memory.getConnectionStatus().put(newUser.getUsername(), 1);
            myLibrary.getUsers().add(newUser);
            memory.getCurrentPage().put(user.getUsername(), new HomePage());
        }

        for (SongInput song : library.getSongs()) {
            Song newSong = new Song(song.getName(), song.getDuration(), song.getAlbum(),
                    song.getTags(), song.getLyrics(), song.getGenre(), song.getReleaseYear(),
                    song.getArtist());
            myLibrary.getSongs().add(newSong);
        }

        for (PodcastInput podcast : library.getPodcasts()) {
            Podcast newPodcast = new Podcast(podcast.getName(), podcast.getOwner());
            for (EpisodeInput episode : podcast.getEpisodes()) {
                Episode newEpisode = new Episode(episode.getName(), episode.getDuration(),
                        episode.getDescription());
                newPodcast.getEpisodes().add(newEpisode);
            }
            myLibrary.getPodcasts().add(newPodcast);
        }

        JsonNode commands = objectMapper.readTree(new File("input/" + filePathInput));
        CommandParser commandParser = new CommandParser(myLibrary, outputs);

        for (int i = 0; i < commands.size(); i++) {
            JsonNode currentCommand = commands.get(i);
            commandParser.parse(currentCommand, memory);
        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePathOutput), outputs);
        memory.removeInstance();
    }
}
