package commandsParser;

import java.util.HashMap;
import java.util.Map;

public final class SearchCommand extends Command {
    private String type;
    private Map<String, Object> filters;

    public SearchCommand(String command, String username, Integer timestamp, String type, Map<String, Object> filters) {
        super(command, username, timestamp);
        this.type = type;
        this.filters = new HashMap<>(filters);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
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
