package com.example.social_network_gui_v2.repository.database;

import com.example.social_network_gui_v2.domain.Event;
import com.example.social_network_gui_v2.domain.Notification;
import com.example.social_network_gui_v2.domain.validation.Validator;
import com.example.social_network_gui_v2.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NotificationDbRepository implements Repository<Long, Notification> {
    private String url;
    private String username;
    private String password;
    private Validator<Event> validator;

    public NotificationDbRepository(String url, String username, String password, Validator<Event> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Notification save(Notification entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must be not null");

        String sql = "insert into notifications(idn,iduser,notif) values (?,?,?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setLong(1, entity.getId());
            ps.setLong(2, entity.getIduser());
            ps.setString(3, entity.getNotif());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Notification findOne(Long aLong) {
        if (aLong == null)
            throw new IllegalArgumentException("Id must be not null");

        String sql = "SELECT * from notifications where notifications.idn = ? ";
        Notification nf;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("idn");
                Long iduser = resultSet.getLong("iduser");
                String st = resultSet.getString("notify");

                nf=new Notification(st,iduser);
                nf.setId(id);
                return nf;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Notification> findAll() {
        String sql = "SELECT * from notifications ";
        Notification nf;
        Set<Notification> events = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("idn");
                Long iduser = resultSet.getLong("iduser");
                String st = resultSet.getString("notif");

                nf=new Notification(st,iduser);
                nf.setId(id);
                events.add(nf);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return events;
    }

    @Override
    public Notification delete(Long aLong) {
        return null;
    }

    @Override
    public Notification update(Notification entity) {
        return null;
    }
}
