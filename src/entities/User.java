package entities;

import java.util.ArrayList;

public class User {
    private String username;
    private int age;
    private String city;
    private String type;

    public User() {
    }

    public User(final String username, final int age, final String city, final String type) {
        this.username = username;
        this.age = age;
        this.city = city;
        this.type = type;
    }

    /**
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     *
     * @return age
     */
    public int getAge() {
        return age;
    }

    /**
     *
     * @param age
     */
    public void setAge(final int age) {
        this.age = age;
    }

    /**
     *
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @param city
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     *
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public ArrayList<Album> getAlbums() {
        return null;
    }

    /**
     *
     * @return
     */
    public ArrayList<Event> getEvents() {
        return null;
    }

    /**
     *
     * @return
     */
    public ArrayList<Merch> getMerchandise() {
        return null;
    }

    /**
     *
     * @return
     */
    public ArrayList<Podcast> getPodcasts() {
        return null;
    }

    /**
     *
     * @return
     */
    public ArrayList<Announcement> getAnnouncements() {
        return null;
    }
}
