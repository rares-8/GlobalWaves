package fileio.input;

import java.util.ArrayList;
import java.util.Map;

public final class PlaylistInput implements Audio {
    private String name;
    private int isPrivate;
    private ArrayList<SongInput> playlistSongs;
    private String owner;
    private Integer timeCreated;

    public PlaylistInput(final String name, final int isPrivate,
                         final ArrayList<SongInput> playlistSongs,
                         final String owner, final int timeCreated) {
        this.name = name;
        this.isPrivate = isPrivate;
        this.playlistSongs = playlistSongs;
        this.owner = owner;
        this.timeCreated = timeCreated;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Integer getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(final Integer isPrivate) {
        this.isPrivate = isPrivate;
    }

    public ArrayList<SongInput> getPlaylistSongs() {
        return playlistSongs;
    }

    public void setPlaylistSongs(final ArrayList<SongInput> playlistSongs) {
        this.playlistSongs = playlistSongs;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    // not used, but have to be implemented
    @Override
    public ArrayList<EpisodeInput> getEpisodes() {
        return null;
    }

    @Override
    public void setEpisodes(final ArrayList<EpisodeInput> episodes) {

    }

    @Override
    public Integer getDuration() {
        return null;
    }

    @Override
    public void setDuration(final Integer duration) {

    }

    @Override
    public String getAlbum() {
        return null;
    }

    @Override
    public void setAlbum(final String album) {

    }

    @Override
    public ArrayList<String> getTags() {
        return null;
    }

    @Override
    public void setTags(final ArrayList<String> tags) {

    }

    @Override
    public String getLyrics() {
        return null;
    }

    @Override
    public void setLyrics(final String lyrics) {

    }

    @Override
    public String getGenre() {
        return null;
    }

    @Override
    public void setGenre(final String genre) {

    }

    @Override
    public int getReleaseYear() {
        return 0;
    }

    @Override
    public void setReleaseYear(final int releaseYear) {

    }

    @Override
    public String getArtist() {
        return null;
    }

    @Override
    public void setArtist(final String artist) {

    }

    public int getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(final int timeCreated) {
        this.timeCreated = timeCreated;
    }

    @Override
    public String getAudioType() {
        return "playlist";
    }

    @Override
    public Map<String, ArrayList<Audio>> getFollowedPlaylists() {
        return null;
    }

    /**
     * @return episode description
     */
    @Override
    public String getDescription() {
        return null;
    }

    /**
     * @param description
     */
    @Override
    public void setDescription(final String description) {

    }
}
