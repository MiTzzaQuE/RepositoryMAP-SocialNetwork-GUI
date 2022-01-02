package com.example.social_network_gui_v2.domain;

public class Notification extends Entity<Long>{
    String notif;
    Long iduser;

    public Notification(String notif, Long iduser) {

        this.notif = notif;
        this.iduser = iduser;
    }

    public Long getIduser() {
        return iduser;
    }

    public void setIduser(Long iduser) {
        this.iduser = iduser;
    }

    public String getNotif() {
        return notif;
    }

    public void setNotif(String notif) {
        this.notif = notif;
    }
}