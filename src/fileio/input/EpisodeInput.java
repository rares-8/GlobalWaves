package fileio.input;

import java.util.ArrayList;
import java.util.Map;

public final class EpisodeInput implements Audio {
    private String name;
    private Integer duration;
    private String description;

    public EpisodeInput() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(final Integer duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @return audio type
     */
    @Override
    public String getAudioType() {
        return "podcastEpisode";
    }

    // not used

    /**
     * @return podcast owner
     */
    @Override
    public String getOwner() {
        return null;
    }

    /**
     * @param owner
     */
    @Override
    public void setOwner(final String owner) {

    }

    /**
     * @return episodes
     */
    @Override
    public ArrayList<EpisodeInput> getEpisodes() {
        return null;
    }

    /**
     * @param episodes
     */
    @Override
    public void setEpisodes(final ArrayList<EpisodeInput> episodes) {

    }

    /**
     * @return album
     */
    @Override
    public String getAlbum() {
        return null;
    }

    /**
     * @param album
     */
    @Override
    public void setAlbum(final String album) {

    }

    /**
     * @return tags
     */
    @Override
    public ArrayList<String> getTags() {
        return null;
    }

    /**
     * @param tags
     */
    @Override
    public void setTags(final ArrayList<String> tags) {

    }

    /**
     * @return lyrics
     */
    @Override
    public String getLyrics() {
        return null;
    }

    /**
     * @param lyrics
     */
    @Override
    public void setLyrics(final String lyrics) {

    }

    /**
     * @return genre
     */
    @Override
    public String getGenre() {
        return null;
    }

    /**
     * @param genre
     */
    @Override
    public void setGenre(final String genre) {

    }

    /**
     * @return releaseYear
     */
    @Override
    public int getReleaseYear() {
        return 0;
    }

    /**
     * @param releaseYear
     */
    @Override
    public void setReleaseYear(final int releaseYear) {

    }

    /**
     * @return artist
     */
    @Override
    public String getArtist() {
        return null;
    }

    /**
     * @param artist
     */
    @Override
    public void setArtist(final String artist) {

    }

    /**
     * @return isPrivate
     */
    @Override
    public Integer getIsPrivate() {
        return null;
    }

    /**
     * @param isPrivate
     */
    @Override
    public void setIsPrivate(final Integer isPrivate) {

    }

    /**
     * @return playlistSongs
     */
    @Override
    public ArrayList<SongInput> getPlaylistSongs() {
        return null;
    }

    /**
     * @param playlistSongs
     */
    @Override
    public void setPlaylistSongs(final ArrayList<SongInput> playlistSongs) {

    }

    /**
     * @return timeCreated
     */
    @Override
    public int getTimeCreated() {
        return 0;
    }

    /**
     * @param timeCreated
     */
    @Override
    public void setTimeCreated(final int timeCreated) {

    }

    /**
     * @return followed playlists
     */
    @Override
    public Map<String, ArrayList<Audio>> getFollowedPlaylists() {
        return null;
    }
}
