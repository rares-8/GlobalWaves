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

    /**
     *
     * @return isPrivate
     */
    Integer getIsPrivate();

    /**
     *
     * @param isPrivate
     */
    void setIsPrivate(Integer isPrivate);

    /**
     *
     * @return playlistSongs
     */
    ArrayList<SongInput> getPlaylistSongs();

    /**
     *
     * @param playlistSongs
     */
    void setPlaylistSongs(ArrayList<SongInput> playlistSongs);

    /**
     *
     * @return timeCreated
     */
    int getTimeCreated();

    /**
     *
     * @param timeCreated
     */
    void setTimeCreated(int timeCreated);

    /**
     *
     * @return audio type
     */
    String getAudioType();

    /**
     *
     * @return followed playlists
     */
    Map<String, ArrayList<Audio>> getFollowedPlaylists();

    /**
     *
     * @return episode description
     */
    String getDescription();

    /**
     *
     * @param description
     */
    void setDescription(String description);
}
