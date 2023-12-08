package entities;


import java.util.ArrayList;
import java.util.Map;

public class Playlist implements Audio {
    private String name;
    private int isPrivate;
    private ArrayList<Song> songs = new ArrayList<>();
    private String owner;
    private Integer timeCreated;
    private Integer totalLikes;

    public Playlist(final String name, final int isPrivate,
                    final ArrayList<Song> songs,
                    final String owner, final int timeCreated) {
        this.name = name;
        this.isPrivate = isPrivate;
        this.songs = songs;
        this.owner = owner;
        this.timeCreated = timeCreated;
    }

    public Playlist() {

    }

    /**
     *
     * @return total likes for all songs
     */
    public Integer getTotalLikes() {
        return totalLikes;
    }

    /**
     *
     * @param totalLikes total likes for all songs
     */
    public void setTotalLikes(final Integer totalLikes) {
        this.totalLikes = totalLikes;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public Integer getIsPrivate() {
        return isPrivate;
    }

    /**
     *
     * @param isPrivate
     */
    public void setIsPrivate(final Integer isPrivate) {
        this.isPrivate = isPrivate;
    }

    /**
     *
     * @return
     */
    public ArrayList<Song> getPlaylistSongs() {
        return songs;
    }

    /**
     *
     * @param playlistSongs
     */
    public void setPlaylistSongs(final ArrayList<Song> playlistSongs) {
        this.songs = playlistSongs;
    }

    /**
     *
     * @return
     */
    public String getOwner() {
        return owner;
    }

    /**
     *
     * @param owner
     */
    public void setOwner(final String owner) {
        this.owner = owner;
    }

    /**
     *
     * @return
     */
    @Override
    public ArrayList<Episode> getEpisodes() {
        return null;
    }

    /**
     *
     * @param episodes
     */
    @Override
    public void setEpisodes(final ArrayList<Episode> episodes) {

    }

    /**
     *
     * @return
     */
    @Override
    public Integer getDuration() {
        return null;
    }

    /**
     *
     * @param duration
     */
    @Override
    public void setDuration(final Integer duration) {

    }

    /**
     *
     * @return
     */
    @Override
    public String getAlbum() {
        return null;
    }

    /**
     *
     * @param album
     */
    @Override
    public void setAlbum(final String album) {

    }

    /**
     *
     * @return
     */
    @Override
    public ArrayList<String> getTags() {
        return null;
    }

    /**
     *
     * @param tags
     */
    @Override
    public void setTags(final ArrayList<String> tags) {

    }

    /**
     *
     * @return
     */
    @Override
    public String getLyrics() {
        return null;
    }

    /**
     *
     * @param lyrics
     */
    @Override
    public void setLyrics(final String lyrics) {

    }

    /**
     *
     * @return
     */
    @Override
    public String getGenre() {
        return null;
    }

    /**
     *
     * @param genre
     */
    @Override
    public void setGenre(final String genre) {

    }

    /**
     *
     * @return
     */
    @Override
    public int getReleaseYear() {
        return 0;
    }

    /**
     *
     * @param releaseYear
     */
    @Override
    public void setReleaseYear(final int releaseYear) {

    }

    /**
     *
     * @return
     */
    @Override
    public String getArtist() {
        return null;
    }

    /**
     *
     * @param artist
     */
    @Override
    public void setArtist(final String artist) {

    }

    /**
     *
     * @return
     */
    public int getTimeCreated() {
        return timeCreated;
    }

    /**
     *
     * @param timeCreated
     */
    public void setTimeCreated(final int timeCreated) {
        this.timeCreated = timeCreated;
    }

    /**
     *
     * @return
     */
    @Override
    public String getAudioType() {
        return "playlist";
    }

    /**
     *
     * @return
     */
    @Override
    public Map<String, ArrayList<Audio>> getFollowedPlaylists() {
        return null;
    }

    /**
     *
     * @return
     */
    @Override
    public String getDescription() {
        return null;
    }

    /**
     *
     * @param description
     */
    @Override
    public void setDescription(final String description) {

    }
}
