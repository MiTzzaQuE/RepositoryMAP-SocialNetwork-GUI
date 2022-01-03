package com.example.social_network_gui_v2.domain;

import javafx.scene.control.Button;

import java.time.LocalDateTime;

public class FriendshipDTO {
    private Long idFrom;
    private String userFrom;
    private Long idTo;
    private String userTo;
    private String status;
    private LocalDateTime date;

    private Button acceptButton;
    private Button rejectButton;
    private Button cancelButton;

    public FriendshipDTO(Long idFrom, String userFrom, Long idTo, String userTo, String status, LocalDateTime date, Button acceptButton, Button rejectButton, Button cancelButton) {
        this.idFrom = idFrom;
        this.userFrom = userFrom;
        this.idTo = idTo;
        this.userTo = userTo;
        this.status = status;
        this.date = date;
        this.acceptButton = acceptButton;
        this.rejectButton = rejectButton;
        this.cancelButton = cancelButton;
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

    public Long getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(Long idFrom) {
        this.idFrom = idFrom;
    }

    public Long getIdTo() {
        return idTo;
    }

    public void setIdTo(Long idTo) {
        this.idTo = idTo;
    }

    public Button getAcceptButton() {
        return acceptButton;
    }

    public void setAcceptButton(Button acceptButton) {
        this.acceptButton = acceptButton;
    }

    public Button getRejectButton() {
        return rejectButton;
    }

    public void setRejectButton(Button rejectButton) {
        this.rejectButton = rejectButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
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
