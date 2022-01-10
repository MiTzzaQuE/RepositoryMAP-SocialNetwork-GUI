package com.example.social_network_gui_v2.domain;

import java.time.LocalDateTime;

public class UserFriendDTO {

    private String firtsName;
    private String lastName;
    private LocalDateTime friendshipDate;

    public UserFriendDTO(String firtsName, String lastName, LocalDateTime friendshipDate) {
        this.firtsName = firtsName;
        this.lastName = lastName;
        this.friendshipDate = friendshipDate;
    }

    public String getFirtsName() {
        return firtsName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDateTime getFriendshipDate() {
        return friendshipDate;
    }

    @Override
    public String toString() {
        return firtsName + " " + lastName + " " + friendshipDate;
    }
}
