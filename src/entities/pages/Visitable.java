package entities.pages;

import entities.Library;
import user.memory.UserMemory;

public interface Visitable {
    /**
     * @param v        - visitor
     * @param username - current user
     * @param memory   - database
     * @param library  - contains songs, users, podcasts
     */
    String accept(Visitor v, String username, UserMemory memory, Library library);
}
