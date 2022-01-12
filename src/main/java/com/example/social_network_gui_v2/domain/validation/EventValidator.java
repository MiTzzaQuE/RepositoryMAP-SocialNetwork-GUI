package com.example.social_network_gui_v2.domain.validation;

import com.example.social_network_gui_v2.domain.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;

public class EventValidator implements  Validator<Event>{

    @Override
    public void validate(Event entity) throws ValidationException {

        if(entity.getName()=="")
            throw new ValidationException("Please provide a Title!");
        if(entity.getDate()==null)
            throw new ValidationException("Incorrect format for the given date!");
        if(entity.getDate().isBefore(ChronoLocalDate.from(LocalDateTime.now())))
            throw new ValidationException("Choose a different date, one from the future!");
    }
}