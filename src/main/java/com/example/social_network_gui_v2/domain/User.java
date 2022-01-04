package com.example.social_network_gui_v2.domain;

import java.util.Objects;

/**
 * define a User that extends Entity
 * firstName-String
 * lastName-String
 * friends-List of Users
 */
public class User extends Entity<Long> {

    /**
     * First and last nama of an user
     */
    private String firstName, lastName;
    private String username;
    private String password;
    /**
     * list of all user friends
     */

    /**
     * constructor
     * @param firstName oof the user
     * @param lastName of the user
     */
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    /**
     * getter funtion
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * getter function
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }


    /**
     * set the first name of an user
     * @param firstName the new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * set the last name of an user
     * @param lastName the new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        String s;
        s = "User{ id='" + this.getId() + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + "' }" ;
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getFirstName(), user.getFirstName()) && Objects.equals(getLastName(), user.getLastName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName());
    }
}
