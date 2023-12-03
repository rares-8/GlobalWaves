package entities.pages;

import entities.Library;
import user.memory.UserMemory;

public class Page implements Visitable {
    /**
     * @param v        - visitor
     * @param username - current user
     * @param memory   - database
     * @param library  - contains songs, users, podcasts
     */
    @Override
    public String accept(final Visitor v, final String username,
                         final UserMemory memory, final Library library) {
        return v.visit(this, username, memory, library);
    }
}
