package com.example.social_network_gui_v2.domain;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.w3c.dom.events.MouseEvent;

import java.time.LocalDateTime;

public class FriendRequestDTO {
    String from;
    String to;
    LocalDateTime date;
    String status;
    ImageView imageAcc;
    ImageView imageDec;

    public FriendRequestDTO(String from,String to, LocalDateTime date, String status) {
        this.from=from;
        this.to=to;
        this.date = date;
        this.status = status;
    }

    public ImageView getImageAcc() { return imageAcc; }

    public void setImageAcc(ImageView imageAcc) { this.imageAcc = imageAcc; }

    public ImageView getImageDec() { return imageDec; }

    public void setImageDec(ImageView imageDec) { this.imageDec = imageDec; }

    public String getFrom() { return from; }

    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }

    public void setTo(String to) { this.to = to; }

    public LocalDateTime getDate() { return date; }

    public void setDate(LocalDateTime date) { this.date = date; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}
