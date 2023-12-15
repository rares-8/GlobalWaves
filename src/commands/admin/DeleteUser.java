package commands.admin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Audio;
import entities.Library;
import entities.Podcast;
import entities.Song;
import entities.User;
import entities.pages.Page;
import user.memory.UserMemory;
import utils.CheckUser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public abstract class DeleteUser {
    /** Deletes a user and all things associated with user
     * @param username  - user that should be deleted
     * @param memory    - database
     * @param timestamp - current timestamp
     * @param library   - contains songs, playlists, podcasts, users
     * @return delete user status
     */
    public static JsonNode deleteUser(final String username, final Integer timestamp,
                                      final UserMemory memory, final Library library) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandResult = mapper.createObjectNode();
        commandResult.put("command", "deleteUser");
        commandResult.put("user", username);
        commandResult.put("timestamp", timestamp);

        boolean ok = CheckUser.checkUser(username, library);

        if (!ok) {
            commandResult.put("message", "The username " + username + " doesn't exist.");
            return commandResult;
        }

        User user = null;
        for (User currentUser : library.getUsers()) {
            if (currentUser.getUsername().equals(username)) {
                user = currentUser;
            }
        }

        switch (user.getType()) {
            case "user":
                deleteNormalUser(user, library, memory, commandResult);
                break;
            case "host":
                deleteHost(user, library, memory, commandResult);
                break;
            case "artist":
                deleteArtist(user, library, memory, commandResult);
                break;
            default:
                System.out.println("Error deleting user - invalid type");
        }

        return commandResult;
    }

    /**
     * Delete method for an artist
     *
     * @param deleteUser    - user that should be deleted
     * @param library       - contains songs, playlists, podcasts, users
     * @param memory        - database
     * @param commandResult - result for deleteUser command
     */
    private static void deleteArtist(final User deleteUser, final Library library,
                                     final UserMemory memory, final ObjectNode commandResult) {
        boolean ok = true;
        /* artist cannot be deleted if user is listening to owned album */
        for (User user : library.getUsers()) {
            if (memory.getLoadedAudio().containsKey(user.getUsername())) {
                Audio loadedAudio = memory.getLoadedAudio().get(user.getUsername());
                if (loadedAudio.getAudioType().equals("album")
                        && loadedAudio.getOwner().equals(deleteUser.getUsername())) {
                    ok = false;
                    break;
                }
            }
        }

        // check if loaded song is a song from artist
        for (User user : library.getUsers()) {
            if (memory.getLoadedAudio().containsKey(user.getUsername())) {
                Audio loadedAudio = memory.getLoadedAudio().get(user.getUsername());
                if (loadedAudio.getAudioType().equals("song")
                        && loadedAudio.getArtist().equals(deleteUser.getUsername())) {
                    ok = false;
                    break;
                }
            }
        }

        // check if loaded playlist contains song from artist
        for (User user : library.getUsers()) {
            if (memory.getLoadedAudio().containsKey(user.getUsername())) {
                Audio loadedAudio = memory.getLoadedAudio().get(user.getUsername());
                if (loadedAudio.getAudioType().equals("playlist")) {
                    for (Song song : loadedAudio.getPlaylistSongs()) {
                        if (song.getArtist().equals(deleteUser.getUsername())) {
                            ok = false;
                            break;
                        }
                    }
                }
            }
        }

        /* check if user is currently on artist page */
        for (User currentUser : library.getUsers()) {
            Page currentPage = memory.getCurrentPage().get(currentUser.getUsername());
            if (currentPage.getOwner().equals(deleteUser.getUsername())) {
                ok = false;
                break;
            }
        }

        if (!ok) {
            commandResult.put("message", deleteUser.getUsername() + " can't be deleted.");
        } else {
            clearUser(deleteUser, library, memory);
            commandResult.put("message", deleteUser.getUsername() + " was successfully deleted.");
        }
    }

    /**
     * Clears everything associated to an artist or a user, they are the same
     *
     * @param deleteUser - user that should be deleted
     * @param library    - contains songs, playlists, podcasts, users
     * @param memory     - database
     */
    private static void clearUser(final User deleteUser, final Library library,
                                  final UserMemory memory) {
        library.getUsers().remove(deleteUser);
        library.getSongs().removeIf(song -> song.getArtist().equals(deleteUser.getUsername()));

        // remove user playlists from followed playlists
        for (Map.Entry<String, ArrayList<Audio>> entry
                : memory.getFollowedPlaylists().entrySet()) {
            String key = entry.getKey();
            ArrayList<Audio> playlists = entry.getValue();
            playlists.removeIf(playlistBackup ->
                    playlistBackup.getOwner().equals(deleteUser.getUsername()));
        }

        // remove user playlists from public playlists
        memory.getPublicPlaylists().removeIf(playlist ->
                playlist.getOwner().equals(deleteUser.getUsername()));

        // remove liked songs
        for (Map.Entry<String, ArrayList<Song>> entry : memory.getLikedSongs().entrySet()) {
            String key = entry.getKey();
            ArrayList<Song> songs = entry.getValue();
            Iterator<Song> iterator = songs.iterator();
            while (iterator.hasNext()) {
                Song songBackup = iterator.next();
                if (songBackup.getArtist().equals(deleteUser.getUsername())) {
                    iterator.remove();
                    songBackup.setLikes(songBackup.getLikes() - 1);
                }
            }
        }

        // clear everything associated with a user
        memory.getLastSearchAudio().remove(deleteUser.getUsername());
        memory.getFollowedPlaylists().remove(deleteUser.getUsername());
        memory.getLastSearchUser().remove(deleteUser.getUsername());
        memory.getLastTimestamp().remove(deleteUser.getUsername());
        memory.getCurrentSelect().remove(deleteUser.getUsername());
        memory.getUserPlaylists().remove(deleteUser.getUsername());
        memory.getLikedSongs().remove(deleteUser.getUsername());
        memory.getLoadedAudio().remove(deleteUser.getUsername());
        memory.getRemainingTime().remove(deleteUser.getUsername());
        memory.getLoadedPodcasts().remove(deleteUser.getUsername());
        memory.getLastEpisodes().remove(deleteUser.getUsername());
        memory.getEpisodeRemainingTime().remove(deleteUser.getUsername());
        memory.getCollectionIndexes().remove(deleteUser.getUsername());
        memory.getCurrentIndex().remove(deleteUser.getUsername());
        memory.getIsPaused().remove(deleteUser.getUsername());
        memory.getIsShuffled().remove(deleteUser.getUsername());
        memory.getIsRepeating().remove(deleteUser.getUsername());
        memory.getConnectionStatus().remove(deleteUser.getUsername());
        memory.getCurrentPage().remove(deleteUser.getUsername());
    }

    /**
     * Delete method for a host
     *
     * @param deleteUser    - user that should be deleted
     * @param library       - contains songs, playlists, podcasts, users
     * @param memory        - database
     * @param commandResult - result for deleteUser command
     */
    private static void deleteHost(final User deleteUser, final Library library,
                                   final UserMemory memory, final ObjectNode commandResult) {
        boolean ok = true;
        /* host cannot be deleted if user is listening to owned podcast */
        for (User currentUser : library.getUsers()) {
            if (memory.getLoadedAudio().containsKey(currentUser.getUsername())) {
                Audio loadedAudio = memory.getLoadedAudio().get(currentUser.getUsername());
                if (loadedAudio.getAudioType().equals("podcast")
                        && loadedAudio.getOwner().equals(deleteUser.getUsername())) {
                    ok = false;
                    break;
                }
            }
        }

        /* check if user is currently on host page */
        for (User currentUser : library.getUsers()) {
            Page currentPage = memory.getCurrentPage().get(currentUser.getUsername());
            if (currentPage.getOwner().equals(deleteUser.getUsername())) {
                ok = false;
                break;
            }
        }

        if (!ok) {
            commandResult.put("message", deleteUser.getUsername() + " can't be deleted.");
        } else {
            clearHost(deleteUser, library, memory);
            commandResult.put("message", deleteUser.getUsername() + " was successfully deleted.");
        }
    }

    /**
     * Clears everything associated to a host
     *
     * @param deleteUser - user that should be deleted
     * @param library    - contains songs, playlists, podcasts, users
     * @param memory     - database
     */
    private static void clearHost(final User deleteUser, final Library library,
                                  final UserMemory memory) {
        library.getUsers().remove(deleteUser);
        for (User user : library.getUsers()) {
            if (memory.getLoadedPodcasts().containsKey(user.getUsername())) {
                ArrayList<Podcast> loadedPodcasts =
                        memory.getLoadedPodcasts().get(user.getUsername());
                for (Podcast podcast : loadedPodcasts) {
                    if (podcast.getOwner().equals(deleteUser.getUsername())) {
                        int idx = loadedPodcasts.indexOf(podcast);
                        memory.getLoadedPodcasts().get(user.getUsername()).remove(idx);
                        memory.getEpisodeRemainingTime().get(user.getUsername()).remove(idx);
                        memory.getLastEpisodes().get(user.getUsername()).remove(idx);
                    }
                }
            }
        }
        library.getPodcasts().removeIf(podcast ->
                podcast.getOwner().equals(deleteUser.getUsername()));
    }

    /**
     * Delete method for a normal user
     *
     * @param deleteUser    - user that should be deleted
     * @param library       - contains songs, playlists, podcasts, users
     * @param memory        - database
     * @param commandResult - result for deleteUser command
     */
    private static void deleteNormalUser(final User deleteUser, final Library library,
                                         final UserMemory memory, final ObjectNode commandResult) {
        boolean ok = true;
        /* a normal user cannot be deleted only if another user is
         listening to a playlist created by given user */
        for (User currentUser : library.getUsers()) {
            if (memory.getLoadedAudio().containsKey(currentUser.getUsername())) {
                Audio loadedAudio = memory.getLoadedAudio().get(currentUser.getUsername());
                if (loadedAudio.getAudioType().equals("playlist")
                        && loadedAudio.getOwner().equals(deleteUser.getUsername())) {
                    ok = false;
                    break;
                }
            }
        }
        if (!ok) {
            commandResult.put("message", deleteUser.getUsername() + " can't be deleted.");
        } else {
            clearUser(deleteUser, library, memory);
            commandResult.put("message", deleteUser.getUsername() + " was successfully deleted.");
        }
    }
}
