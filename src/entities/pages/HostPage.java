package entities.pages;

import entities.Library;
import user.memory.UserMemory;

public final class HostPage extends Page {
    private String owner;

    public HostPage(final String owner) {
        this.owner = owner;
    }

    @Override
    public String accept(final Visitor v, final String username,
                         final UserMemory memory, final Library library) {
        return v.visit(this, owner, memory, library);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }
}
