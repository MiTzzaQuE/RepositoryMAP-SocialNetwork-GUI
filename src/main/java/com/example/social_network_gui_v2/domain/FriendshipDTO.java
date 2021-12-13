package com.example.social_network_gui_v2.domain;

import java.time.LocalDateTime;

public class FriendshipDTO {
    private String userFrom;
    private String userTo;
    private String status;
    private LocalDateTime date;

    public FriendshipDTO(String userFrom, String userTo, String status, LocalDateTime date) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.status = status;
        this.date = date;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "FriendshipDTO{" +
                "userFrom='" + userFrom + '\'' +
                ", userTo='" + userTo + '\'' +
                ", status='" + status + '\'' +
                ", date=" + date +
                '}';
    }
}
