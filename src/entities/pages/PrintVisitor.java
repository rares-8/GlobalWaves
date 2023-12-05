package entities.pages;

import entities.*;
import user.memory.UserMemory;

import java.util.ArrayList;
import java.util.Comparator;

import static utils.Constants.RESULT_MAX_SIZE;

public final class PrintVisitor implements Visitor {

    @Override
    public String visit(final HomePage homePage, final String username,
                        final UserMemory memory, final Library library) {
        ArrayList<Song> likedSongs = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        if (memory.getLikedSongs().containsKey(username)) {
            for (Song song : memory.getLikedSongs().get(username)) {
                Song newSong = new Song();
                newSong.setLikes(song.getLikes());
                newSong.setName(song.getName());
                likedSongs.add(newSong);
            }
            // sort songs by total number of likes
            Comparator<Song> byLikes = Comparator.comparingInt(Song::getLikes);
            likedSongs.sort(byLikes);
            if (likedSongs.size() > RESULT_MAX_SIZE) {
                likedSongs = new ArrayList<>(likedSongs.subList(0, RESULT_MAX_SIZE));
            }

            result.append("Liked songs:\n\t[");
            for (Song likedSong : likedSongs) {
                result.append(likedSong.getName()).append(", ");
            }
            result = new StringBuilder(result.substring(0, result.length() - 2));
            result.append("]\n\n");
        } else {
            result.append("Liked songs:\n\t[]");
        }

        if (memory.getFollowedPlaylists().containsKey(username)) {
            ArrayList<Playlist> followedPlaylists = new ArrayList<>();
            ArrayList<Integer> likes = new ArrayList<>();
            for (Audio playlist : memory.getFollowedPlaylists().get(username)) {
                int playlistLikes = 0;
                for (Song song : playlist.getPlaylistSongs()) {
                    playlistLikes += song.getLikes();
                }
                Playlist newPlaylist = new Playlist();
                newPlaylist.setName(playlist.getName());
                likes.add(playlistLikes);
                followedPlaylists.add(newPlaylist);
            }

            for (int i = 0; i < followedPlaylists.size(); i++) {
                int maxLikes = -1;
                for (Integer iterator : likes) {
                    if (maxLikes < iterator) {
                        maxLikes = iterator;
                    }
                }
                int index = likes.indexOf(maxLikes);
                result.append(followedPlaylists.get(index)).append(", ");
                followedPlaylists.remove(index);
                likes.remove(index);
            }
            result = new StringBuilder(result.substring(0, result.length() - 2));
            result.append("]\n\n");
        } else {
            result.append("Followed playlists:\n\t[]");
        }

        return result.toString();
    }


    @Override
    public String visit(final ArtistPage artistPage, final String owner,
                        final UserMemory memory, final Library library) {
        StringBuilder result = new StringBuilder();
        User artist = null;
        for (User user : library.getUsers()) {
            if (user.getUsername().equals(owner)) {
                artist = user;
                break;
            }
        }

        if (!artist.getAlbums().isEmpty()) {
            result.append("Albums:\n\t[");
            for (Album album : artist.getAlbums()) {
                result.append(album.getName()).append(", ");
            }
            result = new StringBuilder(result.substring(0, result.length() - 2));
            result.append("]\n\n");
        } else {
            result.append("Albums:\n\t[]\n\n");
        }

        if (!artist.getMerchandise().isEmpty()) {
            result.append("Merch:\n\t[");
            for (Merch merch : artist.getMerchandise()) {
                result.append(merch.getName()).append(" - ").append(merch.getPrice());
                result.append(":\n\t").append(merch.getDescription()).append(", ");
            }
            result = new StringBuilder(result.substring(0, result.length() - 2));
            result.append("]\n\n");
        } else {
            result.append("Merch:\n\t[]\n\n");
        }

        if (!artist.getEvents().isEmpty()) {
            result.append("Events:\n\t[");
            for (Event event : artist.getEvents()) {
                result.append(event.getName()).append(" - ").append(event.getDate());
                result.append(":\n\t").append(event.getDescription()).append(", ");
            }
            result = new StringBuilder(result.substring(0, result.length() - 2));
            result.append("]");
        } else {
            result.append("Event:\n\t[]");
        }
        return result.toString();
    }

    @Override
    public String visit(final Page page, final String username,
                        final UserMemory memory, final Library library) {
        return null;
    }
}
