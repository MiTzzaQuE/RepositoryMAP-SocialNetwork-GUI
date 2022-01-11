package com.example.social_network_gui_v2.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Page extends User{

    List<Message> messages;
    List<Friendship> friendRequestsReceived;
    List<Friendship> friendRequestsSent;
    List<User> friends;
    /**
     * constructor
     *
     * @param firstName oof the user
     * @param lastName  of the user
     */
    public Page(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public List<Message> getMessages() { return messages; }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Friendship> getFriendRequestsReceived() {
        return friendRequestsReceived;
    }

    public void setFriendRequestsReceived(List<Friendship> friendRequestsReceived) { this.friendRequestsReceived = friendRequestsReceived; }

    public List<Friendship> getFriendRequestsSent() {
        return friendRequestsSent;
    }

    public void setFriendRequestsSent(List<Friendship> friendRequestsSent) { this.friendRequestsSent = friendRequestsSent; }

    public void removeFrRequestRec(Friendship fr)
    {
        this.friendRequestsReceived.remove(fr);
    }

    public void removeFrRequestSent(Friendship fr)
    {
        this.friendRequestsSent.remove(fr);
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public void removeFriend(User fr)
    {
        this.friends.removeIf(frr -> Objects.equals(frr.getId(), fr.getId()));
    }

    public void addRequestSent(Friendship frRSent)
    {
        friendRequestsSent.add(frRSent);
    }

    public void addMessage(Message mess)
    {
        messages.add(mess);
    }

    //DACA FACEAM OBSERVER IN PAGE NU ERA LA FEL DE EFICIENT, DEOARECE RELUAM TOT DIN BAZA DE DATE CAND SE
    //SCHIMBA CEVA, ASA DOAR ADAUG CATE UN PRIETEN/REQUEST/MESAJ
}
