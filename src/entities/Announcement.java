package entities;

public final class Announcement {
    private String description;
    private String name;

    public Announcement(final String description, final String name) {
        this.description = description;
        this.name = name;
    }

    public Announcement() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
