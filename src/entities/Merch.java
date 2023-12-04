package entities;

public final class Merch {
    String description;
    String name;
    Integer price;

    public Merch(String description, String name, Integer price) {
        this.description = description;
        this.name = name;
        this.price = price;
    }

    public Merch() {
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(final Integer price) {
        this.price = price;
    }
}
