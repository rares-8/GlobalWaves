package commandsParser;

import java.util.HashMap;
import java.util.Map;

public final class SelectCommand extends Command {
    private Integer itemNumber;

    public SelectCommand(String command, String username, Integer timestamp, Integer itemNumber) {
        super(command, username, timestamp);
        this.itemNumber = itemNumber;
    }

    public Integer getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getCommand() {
        return super.getCommand();
    }

    public String getUsername() {
        return super.getUsername();
    }

    public Integer getTimestamp() {
        return super.getTimestamp();
    }
}
