package fileio.input;

import java.util.ArrayList;
import java.util.Map;

/**
 * Interface used by songs and podcasts, useful for polymorphic arrays
 */
public interface Audio {
    /**
     *
     * @return name parameter for podcast/song
     */
    String getName();

    /**
     *
     * @param name
     */
    void setName(String name);

    /**
     *
     * @return podcast owner
     */
    String getOwner();

    /**
     *
     * @param owner
     */
    void setOwner(String owner);

    /**
     *
     * @return episodes
     */
    ArrayList<EpisodeInput> getEpisodes();

    /**
     *
     * @param episodes
     */
    void setEpisodes(ArrayList<EpisodeInput> episodes);

    /**
     *
     * @return duration
     */
    Integer getDuration();

    /**
     *
     * @param duration
     */
    void setDuration(Integer duration);

    /**
     *
     * @return album
     */
    String getAlbum();

    /**
     *
     * @param album
     */
    void setAlbum(String album);

    /**
     *
     * @return tags
     */
    ArrayList<String> getTags();

    /**
     *
     * @param tags
     */
    void setTags(ArrayList<String> tags);

    /**
     *
     * @return lyrics
     */
    String getLyrics();

    /**
     *
     * @param lyrics
     */
    void setLyrics(String lyrics);

    /**
     *
     * @return genre
     */
    String getGenre();

    /**
     *
     * @param genre
     */
    void setGenre(String genre);

    /**
     *
     * @return releaseYear
     */
    int getReleaseYear();

    /**
     *
     * @param releaseYear
     */
    void setReleaseYear(int releaseYear);

    /**
     *
     * @return artist
     */
    String getArtist();

    /**
     *
     * @param artist
     */
    void setArtist(String artist);

    Integer getIsPrivate();

    void setIsPrivate(Integer isPrivate);

    ArrayList<SongInput> getPlaylistSongs();

    void setPlaylistSongs(ArrayList<SongInput> playlistSongs);

    int getTimeCreated();

    void setTimeCreated(int timeCreated);

    String getAudioType();

    public Map<String, ArrayList<Audio>> getFollowedPlaylists();
}
