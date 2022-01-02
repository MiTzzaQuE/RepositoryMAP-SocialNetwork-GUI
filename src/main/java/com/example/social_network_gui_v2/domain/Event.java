package com.example.social_network_gui_v2.domain;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Event extends Entity<Long> {

    private String name;
    private LocalDate date;

    private Map<Long, Long> ids = new HashMap<Long,Long>();

    public Event(String name, LocalDate date, Map<Long, Long> ids) {

        this.name = name;
        this.date = date;
        this.ids = ids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Map<Long, Long> getIds() {
        return ids;
    }

    public void setIds(Map<Long, Long> ids) {
        this.ids = ids;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(name, event.name) && Objects.equals(date, event.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, date);
    }

    @Override
    public String toString() {

        return "Event{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", ids=" + ids +
                '}';
    }
}
