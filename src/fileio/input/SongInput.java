package fileio.input;

import java.util.ArrayList;
import java.util.Map;

public final class SongInput implements Audio {
    private String name;
    private Integer duration;
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private Integer releaseYear;
    private String artist;

    public SongInput() {
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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(final String album) {
        this.album = album;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(final ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(final String lyrics) {
        this.lyrics = lyrics;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(final String genre) {
        this.genre = genre;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(final int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(final String artist) {
        this.artist = artist;
    }

    @Override
    public String getAudioType() {
        return "song";
    }

    // these methods are not used, only declared because it is not an abstract class
    @Override
    public Map<String, ArrayList<Audio>> getFollowedPlaylists() {
        return null;
    }

    @Override
    public Integer getIsPrivate() {
        return null;
    }

    @Override
    public void setIsPrivate(Integer isPrivate) {

    }

    @Override
    public ArrayList<SongInput> getPlaylistSongs() {
        return null;
    }

    @Override
    public void setPlaylistSongs(ArrayList<SongInput> playlistSongs) {

    }

    @Override
    public int getTimeCreated() {
        return 0;
    }

    @Override
    public void setTimeCreated(int timeCreated) {

    }

    @Override
    public String getOwner() {
        return null;
    }

    @Override
    public void setOwner(final String owner) {

    }

    @Override
    public ArrayList<EpisodeInput> getEpisodes() {
        return null;
    }

    @Override
    public void setEpisodes(final ArrayList<EpisodeInput> episodes) {

    }
}
