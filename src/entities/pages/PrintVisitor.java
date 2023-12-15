package entities.pages;

import entities.Album;
import entities.Announcement;
import entities.Audio;
import entities.Episode;
import entities.Event;
import entities.Library;
import entities.Merch;
import entities.Playlist;
import entities.Podcast;
import entities.Song;
import entities.User;
import user.memory.UserMemory;

import java.util.ArrayList;
import java.util.Comparator;

import static utils.Constants.RESULT_MAX_SIZE;

public final class PrintVisitor implements Visitor {

    @Override
    public String visit(final HomePage homePage, final String username,
                        final UserMemory memory, final Library library) {
        StringBuilder result = new StringBuilder();
        if (memory.getLikedSongs().containsKey(username)
        && !memory.getLikedSongs().get(username).isEmpty()) {
            ArrayList<Song> likedSongs = sortedSongs(memory, username);

            result.append("Liked songs:\n\t[");
            for (Song likedSong : likedSongs) {
                result.append(likedSong.getName()).append(", ");
            }
            result = new StringBuilder(result.substring(0, result.length() - 2));
            result.append("]\n\n");
        } else {
            result.append("Liked songs:\n\t[]\n\n");
        }

        if (memory.getFollowedPlaylists().containsKey(username)
        && !memory.getFollowedPlaylists().get(username).isEmpty()) {
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

            result.append("Followed playlists:\n\t[");
            for (int i = 0; i < followedPlaylists.size(); i++) {
                int maxLikes = -1;
                for (Integer iterator : likes) {
                    if (maxLikes < iterator) {
                        maxLikes = iterator;
                    }
                }
                int index = likes.indexOf(maxLikes);
                result.append(followedPlaylists.get(index).getName()).append(", ");
                followedPlaylists.remove(index);
                likes.remove(index);
            }
            result = new StringBuilder(result.substring(0, result.length() - 2));
            result.append("]");
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
            result.append("Events:\n\t[]");
        }
        return result.toString();
    }

    @Override
    public String visit(final HostPage hostPage, final String owner,
                        final UserMemory memory, final Library library) {
        StringBuilder result = new StringBuilder();
        User host = null;
        for (User user : library.getUsers()) {
            if (user.getUsername().equals(owner)) {
                host = user;
                break;
            }
        }

        if (!host.getPodcasts().isEmpty()) {
            result.append("Podcasts:\n\t[");

            for (Podcast podcast : host.getPodcasts()) {
                result.append(podcast.getName()).append(":\n\t");
                if (!podcast.getEpisodes().isEmpty()) {
                    result.append("[");
                    for (Episode episode : podcast.getEpisodes()) {
                        result.append(episode.getName()).append(" - ");
                        result.append(episode.getDescription()).append(", ");
                    }
                    result = new StringBuilder(result.substring(0, result.length() - 2));
                    result.append("]\n, ");
                } else {
                    result.append("[]\n\n, ");
                }
            }
        } else {
            result.append("Podcasts:\n\t[, ");
        }
        result = new StringBuilder(result.substring(0, result.length() - 2));
        result.append("]\n\n");

        if (!host.getAnnouncements().isEmpty()) {
            result.append("Announcements:\n\t[");
            for (Announcement announcement : host.getAnnouncements()) {
                result.append(announcement.getName()).append(":\n\t");
                result.append(announcement.getDescription());
            }
            result.append("\n");
            result.append("]");
        } else {
            result.append("Announcements:\n\t[]\n\n");
        }

        return result.toString();
    }

    @Override
    public String visit(final LikedContentPage likedContentPage, final String username,
                        final UserMemory memory, final Library library) {
        StringBuilder result = new StringBuilder();
        User user = null;
        for (User currentUser : library.getUsers()) {
            if (currentUser.getUsername().equals(username)) {
                user = currentUser;
                break;
            }
        }


        if (memory.getLikedSongs().containsKey(username)
        && !memory.getLikedSongs().get(username).isEmpty()) {
            ArrayList<Song> likedSongs = memory.getLikedSongs().get(username);
            result.append("Liked songs:\n\t[");
            for (Song likedSong : likedSongs) {
                result.append(likedSong.getName()).append(" - ");
                result.append(likedSong.getArtist()).append(", ");
            }
            result = new StringBuilder(result.substring(0, result.length() - 2));
            result.append("]\n\n");
        } else {
            result.append("Liked songs:\n\t[]\n\n");
        }

        if (memory.getFollowedPlaylists().containsKey(username)
                && !memory.getFollowedPlaylists().get(username).isEmpty()) {
            ArrayList<Playlist> followedPlaylists = new ArrayList<>();
            ArrayList<Integer> likes = new ArrayList<>();
            for (Audio playlist : memory.getFollowedPlaylists().get(username)) {
                int playlistLikes = 0;
                for (Song song : playlist.getPlaylistSongs()) {
                    playlistLikes += song.getLikes();
                }
                Playlist newPlaylist = new Playlist();
                newPlaylist.setName(playlist.getName());
                newPlaylist.setOwner(playlist.getOwner());
                likes.add(playlistLikes);
                followedPlaylists.add(newPlaylist);
            }

            result.append("Followed playlists:\n\t[");
            for (int i = 0; i < followedPlaylists.size(); i++) {
                int maxLikes = -1;
                for (Integer iterator : likes) {
                    if (maxLikes < iterator) {
                        maxLikes = iterator;
                    }
                }
                int index = likes.indexOf(maxLikes);
                result.append(followedPlaylists.get(index).getName()).append(" - ");
                result.append(followedPlaylists.get(index).getOwner()).append(", ");
                followedPlaylists.remove(index);
                likes.remove(index);
            }
            result = new StringBuilder(result.substring(0, result.length() - 2));
            result.append("]");
        } else {
            result.append("Followed playlists:\n\t[]");
        }

        return result.toString();
    }

    /**
     *
     * @param memory - database
     * @return - songs sorted by number of likes
     */
    public ArrayList<Song> sortedSongs(final UserMemory memory, final String username) {
        ArrayList<Song> likedSongs = new ArrayList<>();
        for (Song song : memory.getLikedSongs().get(username)) {
            Song newSong = new Song();
            newSong.setLikes(song.getLikes());
            newSong.setName(song.getName());
            likedSongs.add(newSong);
        }
        // sort songs by total number of likes
        Comparator<Song> byLikes = (Song o1, Song o2) -> o2.getLikes() - o1.getLikes();
        likedSongs.sort(byLikes);
        if (likedSongs.size() > RESULT_MAX_SIZE) {
            likedSongs = new ArrayList<>(likedSongs.subList(0, RESULT_MAX_SIZE));
        }

        return likedSongs;
    }

    @Override
    public String visit(final Page page, final String username,
                        final UserMemory memory, final Library library) {
        return null;
    }
}
