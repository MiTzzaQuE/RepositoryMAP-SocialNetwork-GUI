package com.example.social_network_gui_v2.service;

import com.example.social_network_gui_v2.domain.Entity;
import com.example.social_network_gui_v2.domain.Event;
import com.example.social_network_gui_v2.domain.Notification;
import com.example.social_network_gui_v2.domain.validation.ValidationException;
import com.example.social_network_gui_v2.repository.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ServiceEvent {
    private Repository<Long, Event> repoEvents;
    private Repository<Long, Notification> repoNotif;

    public ServiceEvent(Repository<Long, Event> repoEvents, Repository<Long, Notification> repoNotif) {
        this.repoEvents = repoEvents;
        this.repoNotif = repoNotif;
    }

    public void save(String name, LocalDate date, Map<Long,Long> ids) {

        Event ev = new Event(name, date, ids);
        long id = 0L;
        for (Event ur : repoEvents.findAll()) {
            if (ur.getId() > id)
                id = ur.getId();
        }
        id++;
        ev.setId(id);
        Event save = repoEvents.save(ev);
        if (save != null)
            throw new ValidationException("id already used");
    }

    public void update(Long id, String name, LocalDate date,Map<Long,Long> ids) {

        Event ev = new Event(name, date, ids);
        ev.setId(id);
        Event save = repoEvents.update(ev);
        if (save != null)
            throw new ValidationException("this id does not exit");
    }

    public Entity delete(Long id) {

        Event deleted = repoEvents.delete(id);
        if (deleted == null)
            throw new ValidationException("id invalid");
        return deleted;
    }

    public Iterable<Event> printUs() { return repoEvents.findAll(); }

    public Iterable<Notification> printUsNotif() { return repoNotif.findAll(); }

    public Event findOne(Long nr) {

        if (repoEvents.findOne(nr) != null)
            return repoEvents.findOne(nr);
        else
            throw new ValidationException(" id invalid");
    }

    public Notification findOneNotification(Long nr) {

        if (repoEvents.findOne(nr) != null)
            return repoNotif.findOne(nr);
        else
            throw new ValidationException(" id invalid");
    }

    public Notification findOneNotificationIdName(Long iduser,String notif) {

        for(Notification nf :repoNotif.findAll()){
            if(Objects.equals(nf.getIduser(), iduser) && Objects.equals(nf.getNotif(), notif))
                return nf;
        }
        return null;
    }

    public void saveNotif(Long iduser,String name) {

        Notification nf=new Notification(name,iduser);
        long id = 0L;
        for (Notification ur : repoNotif.findAll()) {
            if (ur.getId() > id)
                id = ur.getId();
        }
        id++;
        nf.setId(id);
        Notification save=repoNotif.save(nf);
        if (save != null)
            throw new ValidationException("id already used");
    }
}
