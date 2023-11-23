package entities;

import java.util.ArrayList;
import java.util.Map;

public final class Episode implements Audio {
    private String name;
    private Integer duration;
    private String description;

    public Episode() {
    }

    public Episode(final String name, final Integer duration, final String description) {
        this.name = name;
        this.duration = duration;
        this.description = description;
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

    @Override
    public String getAudioType() {
        return "podcastEpisode";
    }


    @Override
    public String getOwner() {
        return null;
    }

    @Override
    public void setOwner(final String owner) {

    }

    @Override
    public ArrayList<Episode> getEpisodes() {
        return null;
    }

    @Override
    public void setEpisodes(final ArrayList<Episode> episodes) {

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
}
