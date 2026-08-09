package com.example.gamersrl_meetup.utility;

/**
 * AdminRoleHelper class
 *
 * This class is used to have a consolidated location to store the method that
 * determines if the user is an admin or not.
 */
public class AdminRoleHelper
{
    private Boolean isAdmin = true;

    /**
     * Constructor for the Admin Role Helper class
     * Used to access the Admin Role Helper methods.
     */
    public AdminRoleHelper() {}

    /**
     * Determine if the user is an admin or not.
     * @return Whether the user is an admin or not
     */
    public Boolean getIsAdmin()
    {
        return isAdmin;
    }
}