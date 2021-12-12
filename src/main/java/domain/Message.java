package domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Message class which extends an Entity
 */
public class Message extends Entity<Long> {

    private User from;
    private List<User> to;
    private String message;
    private LocalDateTime date;
    private Message repliedTo;

    /**
     * Constructor for a normal message
     * @param from - User who sends the message
     * @param to - List of users where the message goes
     * @param message - String that contains the message
     */
    public Message(User from, List<User> to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.repliedTo=null;
    }

    /**
     * Constructor for a reply message
     * @param from - User who sends the message
     * @param to - List of users where the message goes
     * @param message - String that contains the message
     * @param reply - message that contains the old message
     */
    public Message(User from, List<User> to, String message, Message reply) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.repliedTo = reply;
    }

    /**
     * Getter function for User from
     * @return the user who sends the message
     */
    public User getFrom() {
        return from;
    }

    /**
     * Setter function for User from
     * @param from - new User for the message
     */
    public void setFrom(User from) {
        this.from = from;
    }

    /**
     * Getter function for destination of the message
     * @return - List of users that get the message
     */
    public List<User> getTo() {
        return to;
    }

    /**
     * Function that return the list of Users without userFrom
     * @param userFrom - User that sends the message
     * @return the list of Users without him
     */
    public List<User> getToReply(User userFrom) {
        List<User> result = new ArrayList<>();
        for(User user: this.to){
            if(!Objects.equals(user.getId(),userFrom.getId()))
                result.add(user);
        }
        return result;
    }

    /**
     * Setter function for List of Users
     * @param to - new List of Users
     */
    public void setTo(List<User> to) {
        this.to = to;
    }

    /**
     * Getter function for message
     * @return the string containing the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter function for message
     * @param message - new string message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter function for date
     * @return - the date of message when was sent
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Setter function for date
     * @param date - new date of the message
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Getter function for reply message
     * @return - reply message
     */
    public Message getRepliedTo() {
        return repliedTo;
    }

    /**
     * Setter function for reply message
     * @param repliedTo - new reply message
     */
    public void setRepliedTo(Message repliedTo) {
        this.repliedTo = repliedTo;
    }

    @Override
    public String toString() {
        if(getRepliedTo()==null)
            return from.getFirstName()+" "+from.getLastName()+" : "+message;
        else
            return from.getFirstName()+" "+from.getLastName()+" (reply to: \""+repliedTo.getMessage()+"\") with: "+message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return from.equals(message1.from) && to.equals(message1.to) && message.equals(message1.message) && date.equals(message1.date) && repliedTo.equals(message1.repliedTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, message, date, repliedTo);
    }

}
