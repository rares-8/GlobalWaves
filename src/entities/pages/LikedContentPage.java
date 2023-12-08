package entities.pages;

import entities.Library;
import user.memory.UserMemory;

public final class LikedContentPage extends Page {
    @Override
    public String accept(final Visitor v, final String username,
                         final UserMemory memory, final Library library) {
        return v.visit(this, username, memory, library);
    }
}
