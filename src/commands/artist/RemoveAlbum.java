package commands.artist;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.*;
import user.memory.UserMemory;
import utils.CheckUser;

import java.util.ArrayList;
import java.util.Map;

public abstract class RemoveAlbum {
    /**
     * @param username  - username of artist owning the album
     * @param albumName - album that should be deleted
     * @param memory    - database
     * @param timestamp - current timestamp
     * @param library   - contains songs, playlists, podcasts, users
     * @return delete album status
     */
    public static JsonNode removeAlbum(final String username, final String albumName,
                                       final Integer timestamp, final UserMemory memory,
                                       final Library library) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "removeAlbum");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        boolean ok = CheckUser.checkUser(username, library);

        if (!ok) {
            commandResult.put("message", "The username " + username + " doesn't exist.");
            return commandResult;
        }

        User artist = null;
        for (User user : library.getUsers()) {
            if (user.getUsername().equals(username)) {
                artist = user;
            }
        }

        if (!artist.getType().equals("artist")) {
            commandResult.put("message", username + " is not an artist.");
            return commandResult;
        }

        ok = false;
        for (Album album : artist.getAlbums()) {
            if (album.getName().equals(albumName)) {
                ok = true;
                break;
            }
        }

        if (!ok) {
            commandResult.put("message", username + " doesn't have an album with the given name.");
            return commandResult;
        }

        /* album cannot be deleted if user is listening it */
        for (User user : library.getUsers()) {
            if (memory.getLoadedAudio().containsKey(user.getUsername())) {
                Audio loadedAudio = memory.getLoadedAudio().get(user.getUsername());
                boolean check = loadedAudio.getAudioType().equals("album")
                        && loadedAudio.getOwner().equals(username)
                        && loadedAudio.getName().equals(albumName);
                if (check) {
                    ok = false;
                    break;
                }
            }
        }

        // check if loaded song is from the album
        for (User user : library.getUsers()) {
            if (memory.getLoadedAudio().containsKey(user.getUsername())) {
                Audio loadedAudio = memory.getLoadedAudio().get(user.getUsername());
                boolean check = loadedAudio.getAudioType().equals("song")
                        && loadedAudio.getArtist().equals(username)
                        && loadedAudio.getAlbum().equals(albumName);
                if (check) {
                    ok = false;
                    break;
                }
            }
        }

        // check if loaded playlist contains song from album
        for (User user : library.getUsers()) {
            if (memory.getLoadedAudio().containsKey(user.getUsername())) {
                Audio loadedAudio = memory.getLoadedAudio().get(user.getUsername());
                if (loadedAudio.getAudioType().equals("playlist")) {
                    for (Song song : loadedAudio.getPlaylistSongs()) {
                        boolean check = song.getArtist().equals(username)
                                && song.getAlbum().equals(albumName);
                        if (check) {
                            ok = false;
                            break;
                        }
                    }
                }
            }
        }

        if (!ok) {
            commandResult.put("message", username + " can't delete this album.");
        } else {
            commandResult.put("message", username + "deleted the album successfully.");
            clearAlbum(artist, albumName, memory, library);
        }

        return commandResult;
    }

    /**
     * Clear everything associated with an album
     *
     * @param artist  - username of artist owning the album
     * @param albumName - album that should be deleted
     * @param memory    - database
     * @param library   - contains songs, playlists, podcasts, users
     */
    private static void clearAlbum(final User artist, final String albumName,
                                   final UserMemory memory, final Library library) {
        // clear album songs from library
        library.getSongs().removeIf(song -> song.getArtist().equals(artist.getUsername())
                && song.getAlbum().equals(albumName));

        // remove songs from playlists
        for (Map.Entry<String, ArrayList<Playlist>> entry : memory.getUserPlaylists().entrySet()) {
            String key = entry.getKey();
            ArrayList<Playlist> playlists = entry.getValue();
            for (Playlist playlist : playlists) {
                playlist.getPlaylistSongs().removeIf(song -> song.getAlbum().equals(albumName) && song.getArtist().equals(artist.getUsername()));
            }
        }
        artist.getAlbums().removeIf(album -> album.getName().equals(albumName));
    }
}
