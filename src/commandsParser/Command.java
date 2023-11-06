package commandsParser;

/**
 * All commands, except general statistics use these fields
 */
public abstract class Command {
    private String command;
    private String username;
    private Integer timestamp;

    protected Command(String command, String username, Integer timestamp) {
        this.command = command;
        this.username = username;
        this.timestamp = timestamp;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }
}
