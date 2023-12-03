package entities;

import java.util.ArrayList;
import java.util.Map;

public final class Podcast implements Audio {
    private String name;
    private String owner;
    private ArrayList<Episode> episodes = new ArrayList<>();

    public Podcast() {
    }

    public Podcast(final String name, final String owner) {
        this.name = name;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public ArrayList<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(final ArrayList<Episode> episodes) {
        this.episodes = episodes;
    }

    @Override
    public String getAudioType() {
        return "podcast";
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

    @Override
    public Integer getIsPrivate() {
        return null;
    }

    @Override
    public void setIsPrivate(final Integer isPrivate) {

    }

    @Override
    public ArrayList<Song> getPlaylistSongs() {
        return null;
    }

    @Override
    public void setPlaylistSongs(final ArrayList<Song> playlistSongs) {

    }

    @Override
    public int getTimeCreated() {
        return 0;
    }

    @Override
    public void setTimeCreated(final int timeCreated) {

    }

    @Override
    public Map<String, ArrayList<Audio>> getFollowedPlaylists() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(final String description) {

    }
}
