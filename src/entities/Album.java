package entities;

import java.util.ArrayList;

public final class Album extends Playlist {

    private String description;
    private Integer releaseYear;
    public Album(final String name, final int isPrivate, final ArrayList<Song> songs,
                 final String owner, final int timeCreated) {
        super(name, isPrivate, songs, owner, timeCreated);
    }

    public Album(final String name, final int isPrivate, final ArrayList<Song> songs,
                 final String owner, final int timeCreated, final String description,
                 final Integer releaseYear) {
        super(name, isPrivate, songs, owner, timeCreated);
        this.description = description;
        this.releaseYear = releaseYear;
    }

    public Album() {
    }

    @Override
    public Integer getTotalLikes() {
        return super.getTotalLikes();
    }

    @Override
    public void setTotalLikes(Integer totalLikes) {
        super.setTotalLikes(totalLikes);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(final Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    @Override
    public String getAudioType() {
        return "album";
    }
}
