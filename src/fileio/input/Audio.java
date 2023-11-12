package fileio.input;

import java.util.ArrayList;

/**
 * Interface used in order to be able to create lists that contain podcasts, playlists, and songs.
 */
public interface Audio {
    public String getName();

    public void setName(String name);

    public String getOwner();

    public void setOwner(String owner);

    public ArrayList<EpisodeInput> getEpisodes();

    public void setEpisodes(final ArrayList<EpisodeInput> episodes);

    public Integer getDuration();

    public void setDuration(final Integer duration);

    public String getAlbum();

    public void setAlbum(final String album);

    public ArrayList<String> getTags();

    public void setTags(final ArrayList<String> tags);

    public String getLyrics();

    public void setLyrics(final String lyrics);

    public String getGenre();

    public void setGenre(final String genre);

    public int getReleaseYear();

    public void setReleaseYear(final int releaseYear);

    public String getArtist();

    public void setArtist(final String artist);
}
