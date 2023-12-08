package entities.pages;

import entities.Library;
import user.memory.UserMemory;

public interface Visitor {
    /**
     * Print the homePage
     *
     * @param homePage - print this
     * @param username - current user
     * @param memory   - database
     * @param library  - contains podcasts, users, songs
     */
    String visit(HomePage homePage, String username, UserMemory memory, Library library);

    /**
     * Print the artistPage
     *
     * @param artistPage - print this
     * @param owner      - owner of page
     * @param memory     - database
     * @param library    - contains podcasts, users, songs
     */
    String visit(ArtistPage artistPage, String owner, UserMemory memory, Library library);

    /**
     * Print the hostPage
     *
     * @param hostPage - print this
     * @param owner    - owner of page
     * @param memory   - database
     * @param library  - contains podcasts, users, songs
     */
    String visit(HostPage hostPage, String owner, UserMemory memory, Library library);

    /**
     * Print the likedContentPage
     *
     * @param likedContentPage - print this
     * @param owner    - owner of page
     * @param memory   - database
     * @param library  - contains podcasts, users, songs
     */
    String visit(LikedContentPage likedContentPage, String owner, UserMemory memory,
                 Library library);

    /**
     * Print a generic page
     *
     * @param page     - print this
     * @param username - current user
     * @param memory   - database
     * @param library  - contains podcasts, users, songs
     */
    String visit(Page page, String username, UserMemory memory, Library library);
}
