package entities;

import java.util.ArrayList;

public final class Artist extends User {
    private final ArrayList<Album> albums = new ArrayList<>();
    private final ArrayList<Event> events = new ArrayList<>();
    private final ArrayList<Merch> merchandise = new ArrayList<>();
    public Artist() {
    }

    public Artist(final String username, final int age, final String city) {
        super(username, age, city, "artist");
    }

    public Artist(final String username, final int age, final String city, final String type) {
        super(username, age, city, type);
    }

    @Override
    public String getType() {
        return super.getType();
    }

    @Override
    public void setType(final String type) {
        super.setType(type);
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public void setUsername(final String username) {
        super.setUsername(username);
    }

    @Override
    public int getAge() {
        return super.getAge();
    }

    @Override
    public void setAge(final int age) {
        super.setAge(age);
    }

    @Override
    public String getCity() {
        return super.getCity();
    }

    @Override
    public void setCity(final String city) {
        super.setCity(city);
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public ArrayList<Merch> getMerchandise() {
        return merchandise;
    }

    @Override
    public Integer getTotalLikes() {
        return super.getTotalLikes();
    }

    @Override
    public void setTotalLikes(final Integer totalLikes) {
        super.setTotalLikes(totalLikes);
    }
}
