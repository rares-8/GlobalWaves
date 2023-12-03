package entities.pages;

import entities.Library;
import entities.Song;
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

            result = new StringBuilder("Liked songs:\n\t[");
            for (Song likedSong : likedSongs) {
                result.append(likedSong.getName()).append(", ");
            }
            result = new StringBuilder(result.substring(0, result.length() - 2));
            result.append("]\n\n");
        }

        return result.toString();
    }

    @Override
    public String visit(final Page page, final String username,
                      final UserMemory memory, final Library library) {
        return null;
    }
}
