package com.example.social_network_gui_v2.repository.database;

import com.example.social_network_gui_v2.domain.User;
import com.example.social_network_gui_v2.domain.validation.Validator;
import com.example.social_network_gui_v2.repository.Repository;
import com.example.social_network_gui_v2.utils.BCrypt;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * DataBase user com.example.social_network_gui_v2.repository made for sql use
 * implements the base interface Repository
 * contains objects of type Long and User
 */
public class UserDbRepository implements Repository<Long, User> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<User> validator;

    /**
     * Public constructor for the UserDataBase Repository
     * @param url - String
     * @param username - String
     * @param password - String
     * @param validator - Validator
     */
    public UserDbRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public User findOne(Long id) {
        if(id == null)
            throw new IllegalArgumentException("Id must not be null");

        String sql = "SELECT * FROM users where users.id = ?";
        User user;

        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, Math.toIntExact(id));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                user = new User(firstName,lastName);
                user.setId(id);
                return user;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                User utilizator = new User(firstName, lastName);
                utilizator.setId(id);
                users.add(utilizator);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User save(User entity) {
        if(entity == null)
            throw new IllegalArgumentException("Entity must not be null");
        validator.validate(entity);
        String sql = "insert into users (first_name, last_name ) values (?, ?)";
        //String sql2 = "select users.id from users order by users.id desc limit 1";
        String sql3 = "insert into usernames (id,username,password) values ((select users.id from users order by users.id desc limit 1), ?, ?)";


        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            //PreparedStatement ps2 = connection.prepareStatement(sql2);
            PreparedStatement ps3 = connection.prepareStatement(sql3);

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.executeUpdate();

            //ResultSet resultSet = ps2.executeQuery();
            //int id = resultSet.getInt("id");

            //ps3.setInt(1,id);
            ps3.setString(1,entity.getUsername());
            ps3.setString(2,entity.getPassword());
            ps3.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User delete(Long id) {
        User user = null;
        String sql = "delete from users where id = ?";

        try(Connection connection = DriverManager.getConnection(url,username,password)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            user = this.findOne(id);
            if(user == null)
                return null;

            ps.setLong(1,id);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    @Override
    public User update(User entity) {
        if(entity == null)
            throw new IllegalArgumentException("Entity must not be null");
        validator.validate(entity);
        String sql = "update users set first_name = ?, last_name = ? where id = ?";
        int row_count = 0;

        try(Connection connection = DriverManager.getConnection(url,username,password)) {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1,entity.getFirstName());
            ps.setString(2,entity.getLastName());
            ps.setLong(3,entity.getId());

            row_count = ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(row_count > 0)
            return null;
        return entity;
    }

    public User findUserByUsernameAndPassword(String usernameC, String passwordC){
        if(usernameC == null || passwordC == null)
            throw new IllegalArgumentException("Username and password must not be null");

        String sql = "select u.id, u.first_name, u.last_name, un.username, un.password from users u inner join usernames un on u.id = un.id where un.username = ?";
        User user;

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1,usernameC);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String uname = resultSet.getString("username");
                String pass = resultSet.getString("password");

                if(BCrypt.checkpw(passwordC,pass)){
                    user = new User(firstName,lastName,uname,pass);
                    user.setId(id);
                    return user;
                }
                else{
                    throw new IllegalArgumentException("Invalid password");
                }

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Silviu");
        return null;
    }
}
