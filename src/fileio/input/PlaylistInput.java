package fileio.input;

import java.util.ArrayList;
import java.util.Map;

public class PlaylistInput implements Audio {
    private String name;
    private int isPrivate;
    private ArrayList<SongInput> playlistSongs;
    private String owner;
    private Integer timeCreated;

    public PlaylistInput(String name, int isPrivate, ArrayList<SongInput> playlistSongs, String owner, int timeCreated) {
        this.name = name;
        this.isPrivate = isPrivate;
        this.playlistSongs = playlistSongs;
        this.owner = owner;
        this.timeCreated = timeCreated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Integer isPrivate) {
        this.isPrivate = isPrivate;
    }

    public ArrayList<SongInput> getPlaylistSongs() {
        return playlistSongs;
    }

    public void setPlaylistSongs(ArrayList<SongInput> playlistSongs) {
        this.playlistSongs = playlistSongs;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    // not used, but have to be implemented
    @Override
    public ArrayList<EpisodeInput> getEpisodes() {
        return null;
    }

    @Override
    public void setEpisodes(ArrayList<EpisodeInput> episodes) {

    }

    @Override
    public Integer getDuration() {
        return null;
    }

    @Override
    public void setDuration(Integer duration) {

    }

    @Override
    public String getAlbum() {
        return null;
    }

    @Override
    public void setAlbum(String album) {

    }

    @Override
    public ArrayList<String> getTags() {
        return null;
    }

    @Override
    public void setTags(ArrayList<String> tags) {

    }

    @Override
    public String getLyrics() {
        return null;
    }

    @Override
    public void setLyrics(String lyrics) {

    }

    @Override
    public String getGenre() {
        return null;
    }

    @Override
    public void setGenre(String genre) {

    }

    @Override
    public int getReleaseYear() {
        return 0;
    }

    @Override
    public void setReleaseYear(int releaseYear) {

    }

    @Override
    public String getArtist() {
        return null;
    }

    @Override
    public void setArtist(String artist) {

    }

    public int getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(int timeCreated) {
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


}
