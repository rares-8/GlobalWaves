package entities;

public final class Host extends User {
    public Host() {
    }

    public Host(final String username, final int age, final String city) {
        super(username, age, city, "host");
    }

    public Host(final String username, final int age, final String city, final String type) {
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
}
