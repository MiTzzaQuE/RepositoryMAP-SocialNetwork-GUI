package com.example.social_network_gui_v2.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Chat extends Entity<Long>{
    String name;
    private List<Long> people = new ArrayList<>();;

    public Chat(String name, List<Long> people) {

        this.name = name;
        this.people = people.stream().sorted().collect(Collectors.toList());//we lost the sorted array at constructor
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getPeople() {
        return people;
    }

    public void setPeople(List<Long> people) {
        this.people = people;
    }
}
