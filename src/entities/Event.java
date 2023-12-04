package entities;

public final class Event {
    private String description;
    private String date;
    private String name;

    public Event(final String description, final String date,
                 final String name) {
        this.description = description;
        this.date = date;
        this.name = name;
    }

    public Event() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
