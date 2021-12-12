package service;

import domain.Entity;
import domain.Message;
import domain.User;
import domain.validation.ValidationException;
import repository.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service of Message
 * repoMessage - Message Repository
 * repoUser - User Repository
 */
public class ServiceMessage {

    private final Repository<Long, Message> repoMessage;
    private final Repository<Long, User> repoUser;

    /**
     * Constructor for Message Service
     * @param repoMessage - repository for messages
     * @param repoUser - repository for users
     */
    public ServiceMessage( Repository<Long, User> repoUser, Repository<Long, Message> repoMessage ) {
        this.repoMessage = repoMessage;
        this.repoUser = repoUser;
    }

    /**
     * Save one message
     * @param fromId - id of the user that sends the message
     * @param toIds - list of user's ids
     * @param message -string
     */
    public void save( Long fromId, List<Long> toIds, String message ){

        User from = repoUser.findOne(fromId);
        List<User> to = new ArrayList<>();
        toIds.forEach(x -> to.add(repoUser.findOne(x)));
        Message msg = new Message( from, to, message, null) ;
        msg.setDate(LocalDateTime.now());

        long id = get_size();
        msg.setId(id);

        Message save = repoMessage.save(msg);
        if(save != null)
            throw new ValidationException("Id already used!");
    }

    /**
     * save a reply of a message
     * @param fromId-the user id that sends the reply
     * @param message-string
     * @param reply-the id of the old message
     */
    public void saveReply( Long fromId, String message, Long reply ){

        User from = repoUser.findOne(fromId);
        List<User> to;
        to = findTo(reply,fromId);
        checkMessageReply(reply,fromId);
        Message replyMessage = repoMessage.findOne(reply);
        Message msg = new Message(from ,to, message, replyMessage);
        msg.setDate(LocalDateTime.now());

        long id = get_size();
        msg.setId(id);

        Message save = repoMessage.save(msg);
        if( save != null )
            throw new ValidationException("Id already used!");
    }

    /**
     * Find one message for a given id
     * @param id - id of the message
     * @return - the message if exists
     * otherwise, throw exception
     */
    public Message findOne( Long id ){

        Message findMsg = repoMessage.findOne(id);
        if(findMsg != null)
            return findMsg;
        else
            throw new ValidationException("Message id invalid!");
    }

    /**
     * find the receivers of a reply
     * @param idMessageOld-id of old message
     * @param idMessageNew-id of new message
     * @return - the list of users
     */
    private List<User> findTo( Long idMessageOld, Long idMessageNew ) {

        Message messageOld = findOne(idMessageOld);
        List<User> listTo = messageOld.getToReply(repoUser.findOne(idMessageNew));
        User oldFrom = messageOld.getFrom();
        listTo.add(oldFrom);
        return listTo;
    }

    /**
     * Check if the old message was sent to the user that wants to send a reply
     * @param idMessageOld -id of old message
     * @param idUserNew -id of the user that wants to send a reply
     * @return - the user if is founded
     * throw exception otherwise
     */
    private User checkMessageReply( Long idMessageOld, Long idUserNew ) {

        Message msg = findOne(idMessageOld);
        User userFromNew = repoUser.findOne(idUserNew);
        if(Objects.equals(userFromNew.getId(), msg.getFrom().getId()))
            throw new ValidationException("You cannot reply to yourself!");

        for(User user: msg.getTo()) {
            if (Objects.equals(userFromNew.getId(), user.getId()))
                return userFromNew;
        }
        throw new ValidationException("You cannot reply to a message from a conversation that is not yours!");
    }

    /**
     * Function that make a list of messages for two users
     * @param id1 - id of first user
     * @param id2 - id fo second user
     * @return - returns the list of messages from those 2 users
     */
    public List<Message> PrivateChat( Long id1, Long id2 ){

        List<Message> conversation = new ArrayList<>();
        User user1 = repoUser.findOne(id1);
        User user2 = repoUser.findOne(id2);
        List<Message> messages = new ArrayList<>();
        repoMessage.findAll().forEach(messages :: add);
        List<Message> sortedMessages = messages
                .stream()
                .sorted(Comparator.comparing(Entity::getId))
                .collect(Collectors.toList());

        for(Message msg : sortedMessages){
            if((Objects.equals(id1,msg.getFrom().getId()) && msg.getTo().contains(user2) && msg.getTo().size() == 1)
            || (Objects.equals(id2,msg.getFrom().getId()) && msg.getTo().contains(user1)) && msg.getTo().size() == 1)
                conversation.add(msg);
        }
        return conversation;
    }

    /**
     * Getter function for biggest id
     * @return the biggest id+1
     */
    private long get_size() {
        long id = 0L;
        for (Message message1 : repoMessage.findAll()) {
            if (message1.getId() > id)
                id = message1.getId();
        }
        id++;
        return id;
    }
}
