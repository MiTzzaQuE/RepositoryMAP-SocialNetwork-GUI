package com.example.social_network_gui_v2.repository.database;

import com.example.social_network_gui_v2.domain.Event;
import com.example.social_network_gui_v2.domain.validation.Validator;
import com.example.social_network_gui_v2.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class EventDbRepository implements Repository<Long, Event> {
    private String url;
    private String username;
    private String password;
    private Validator<Event> validator;

    public EventDbRepository(String url, String username, String password, Validator<Event> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Event save(Event entity) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd");

        if (entity == null)
            throw new IllegalArgumentException("Entity must be not null");
        validator.validate(entity);
        String sql = "insert into events(ide,name,date) values (?,?,'"+entity.getDate().format(formatter)+"')";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setLong(1, entity.getId());
            ps.setString(2, entity.getName());
//            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d/MM/yyyy");
//            String text = entity.getDate().format(formatters);
//            ps.setString(3, text);

            String sqlChat = "insert into participants (idp,iduser,idnotify) values (?,?,?)";
            PreparedStatement psChat = connection.prepareStatement(sqlChat);
            ps.executeUpdate();

            Map<Long,Long> listTo = entity.getIds();
            for (Map.Entry<Long,Long> ur : listTo.entrySet()) {
                psChat.setLong(1, entity.getId());
                psChat.setLong(2, ur.getKey());
                psChat.setLong(3,ur.getValue());
                psChat.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Event findOne(Long aLong) {

        if (aLong == null)
            throw new IllegalArgumentException("Id must be not null");

        String sql = "SELECT * from events where events.ide = ? ";
        Event ev;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ev = extractEvent(connection, resultSet);
                return ev;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Event> findAll() {

        String sql = "SELECT * from events ";
        Event ev;
        Set<Event> events = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                ev = extractEvent(connection, resultSet);
                events.add(ev);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return events;
    }

    private Event extractEvent(Connection connection, ResultSet resultSet) throws SQLException {
        Event ev;
        Long id = resultSet.getLong("ide");
        String st = resultSet.getString("name");
        LocalDate date = resultSet.getTimestamp("date").toLocalDateTime().toLocalDate();
        Map<Long, Long> idList = findParticipants(id, connection);

        ev = new Event(st, date, idList);
        ev.setId(id);
        return ev;
    }

    @Override
    public Event delete(Long aLong) {
        int row_count = 0;
        Event event = null;
        String sql = "delete from events where events.ide=?  ";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, event.getId());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return event;
    }

    //NEIMPLREMENTAT
    @Override
    public Event update(Event entity) {
        int row_count = 0;
        if (entity == null)
            throw new IllegalArgumentException("Entity must be not null");
        validator.validate(entity);
        String sql = "update  events SET name =?, date = ? where events.ide=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, entity.getName());
            LocalDate now = entity.getDate();
            Timestamp date = Timestamp.valueOf(now.atStartOfDay());
            ps.setTimestamp(2, date);
            ps.setLong(3, entity.getId());
            row_count = ps.executeUpdate();

            String sqlDel = "delete from participants where idp=?  ";
            PreparedStatement psDel = connection.prepareStatement(sqlDel);
            psDel.setLong(1, entity.getId());
            psDel.executeUpdate();

            String sqlChat = "insert into participants values(?,?,?)";
            PreparedStatement psChat = connection.prepareStatement(sqlChat);
            Map<Long,Long> listTo = entity.getIds();
            for (Map.Entry<Long,Long> ur : listTo.entrySet()) {
                psChat.setLong(1, entity.getId());
                psChat.setLong(2, ur.getKey());
                psChat.setLong(3,ur.getValue());
                psChat.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (row_count > 0)
            return null;
        return entity;
    }


    private Map<Long, Long> findParticipants(Long id, Connection connection) {
        Map<Long,Long> toIds = new HashMap<>();
        try (
                PreparedStatement statement = connection.prepareStatement("select iduser,idnotify \n" +
                        "from events e inner join participants p on e.ide = p.idp \n" +
                        "where (e.ide=?)")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long idNew = resultSet.getLong("iduser");
                    Long notify = resultSet.getLong("idnotify");
                    toIds.put(idNew,notify);
                }
                return toIds;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
