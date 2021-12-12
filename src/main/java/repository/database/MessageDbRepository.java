package repository.database;

import domain.Message;
import domain.User;
import domain.validation.Validator;
import repository.Repository;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DataBase user repository made for sql use
 * implements the base interface Repository
 * contains objects of type Long and Message
 */
public class MessageDbRepository implements Repository<Long, Message> {

    private final String url;
    private final String username;
    private final String password;
    private final Validator<Message> validator;

    /**
     * Public constructor for the UserDataBase Repository
     * @param url - String
     * @param username - String
     * @param password - String
     * @param validator - Validator
     */
    public MessageDbRepository(String url, String username, String password, Validator<Message> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }


    @Override
    public Message findOne( Long id ) {

        if(id == null)
            throw new IllegalArgumentException("Id must not be null!");

        Message msg;

        try(Connection connection = DriverManager.getConnection(url,username,password);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from messages where messages.id = ?")){
            preparedStatement.setInt(1,Math.toIntExact(id));
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                msg = extractMessage(resultSet);
                return msg;
            }
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Message> findAll() {

        Set<Message> messages = new HashSet<>();
        Message msg;

        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from messages order by datem")){
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                msg = extractMessage(resultSet);
                messages.add(msg);
            }
            return messages;
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return messages;
    }

    /**
     * Function for extract one entity from Message table
     * @param resultSet - attribute that contains information from the table
     * @return - the message from the table
     */
    private Message extractMessage(ResultSet resultSet) throws SQLException {
        Message msg;
        Long idm = resultSet.getLong("id");
        LocalDateTime date = resultSet.getTimestamp("datem").toLocalDateTime();
        Long fromId = resultSet.getLong("fromm");
        User from = findOneUser(fromId);
        List<User> to = new ArrayList<>();

        findTo(idm).forEach( x -> to.add(findOneUser(x)) );
        String message = resultSet.getString("messagem");
        Long idReply = resultSet.getLong("replym");

        Message reply = this.findOne(idReply);
        msg = new Message(from,to,message);
        msg.setMessage(message);
        msg.setId(idm);
        msg.setDate(date);
        msg.setRepliedTo(reply);

        return msg;
    }

    @Override
    public Message save( Message entity ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm");

        if(entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }
        validator.validate(entity);
        Message message;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement("insert into messages (fromm, messagem, datem, replym ) values (?,?,'"+entity.getDate().format(formatter)+"',?)")) {

            message = this.findOne(entity.getId());
            if(message != null)
                return message;

            ps.setLong(1, entity.getFrom().getId());

            PreparedStatement psChats = connection.prepareStatement("insert into chats (id, tom) values (?,?)");

            for(User user: entity.getTo()) {
                psChats.setLong(1,entity.getId());
                psChats.setLong(2,user.getId());
                psChats.executeUpdate();
            }

            ps.setString(2,entity.getMessage());
            if(entity.getRepliedTo() != null)
                ps.setLong(3,entity.getRepliedTo().getId());
            else
                ps.setLong(3,-1L);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message delete( Long id ) {
        if(id == null)
            throw new IllegalArgumentException("ID must not be null!");
        Message messageRemoved = null;

        try(Connection connection = DriverManager.getConnection(url,username,password)) {
            PreparedStatement ps = connection.prepareStatement("delete from messages where id = ?");

            messageRemoved = this.findOne(id);
            if(messageRemoved == null)
                return null;

            ps.setLong(1,id);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return messageRemoved;
    }

    @Override
    public Message update( Message entity ) {
        return null;
    }

    /**
     * Function that search one user with a given id
     * @param fromId - integer, id of user
     * @return the User with the given id
     */
    private User findOneUser( Long fromId ) {

        if(fromId == null)
            throw new IllegalArgumentException("Id must not be null");

        User user;

        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users where users.id = ?")){
            statement.setInt(1, Math.toIntExact(fromId));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                user = new User(firstName,lastName);
                user.setId(fromId);
                return user;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Function getter for
     * @param id - integer, id of message
     * @return the list of users ids
     */
    private List<Long> findTo (Long id) {

        List<Long> listTo = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement("""
                    select tom from chats 
                    where id = ?""")){
            preparedStatement.setLong(1,id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()){
                    Long idNew = resultSet.getLong("tom");
                    listTo.add(idNew);
                }
                return listTo;
            }
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Find the friends of one user
     * @param id - integer, user id
     * @return - list of users
     */
    private List<User> FindFriends (Long id){

        List<User> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("""
                     select id, first_name, last_name, date
                     from users u inner join friendships f on u.id = f.id1 or u.id=f.id2
                     where (f.id1= ? or f.id2 = ? )and u.id!= ?""")){
            statement.setLong(1, id);
            statement.setLong(2, id);
            statement.setLong(3, id);
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    Long idNew = resultSet.getLong("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");

                    User user = new User(firstName,lastName);
                    user.setId(idNew);

                    users.add(user);
                }
                return users;
            }
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return null;
    }
}
