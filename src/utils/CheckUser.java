package utils;

import entities.Library;
import entities.User;

public abstract class CheckUser {
    /**
     * Check if a user exists
     * @param username - check if this user exists
     * @param library - contains all users
     */
    public static boolean checkUser(final String username, final Library library) {
        for (User user : library.getUsers()) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
