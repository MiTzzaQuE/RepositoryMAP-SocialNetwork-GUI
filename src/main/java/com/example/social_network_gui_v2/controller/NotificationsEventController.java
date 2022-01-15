package com.example.social_network_gui_v2.controller;

import com.example.social_network_gui_v2.HelloApplication;
import com.example.social_network_gui_v2.domain.Event;
import com.example.social_network_gui_v2.domain.Notification;
import com.example.social_network_gui_v2.domain.Page;
import com.example.social_network_gui_v2.domain.validation.ValidationException;
import com.example.social_network_gui_v2.service.ServiceEvent;
import com.example.social_network_gui_v2.service.ServiceFriendship;
import com.example.social_network_gui_v2.service.ServiceMessage;
import com.example.social_network_gui_v2.service.ServiceUser;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class NotificationsEventController extends MenuController {
    @FXML
    ListView<Event> eventsListView;
    @FXML
    ListView<String> notificationsListView;
    @FXML
    ListView<String> myEventsListView;
    @FXML
    TextField titleEvent;
    @FXML
    DatePicker datePickerEvent;
    @FXML
    Button createEventButton;
    @FXML
    Button chooseButton;

    Event lastEventSelected=null;

    ObservableList<Event> modelEvents = FXCollections.observableArrayList();
    ObservableList<String> modelUsersEvents = FXCollections.observableArrayList();
    ObservableList<String> modelNotifications = FXCollections.observableArrayList();

    public void setService( ServiceUser servUser, ServiceFriendship servFriendship, ServiceMessage servMessage, ServiceEvent servEvent, Page userLogin, Stage stage) {

        this.servUser = servUser;
        this.servMessage = servMessage;
        this.servFriendship = servFriendship;
        this.servEvent=servEvent;
        this.dialogStage = stage;
        this.userLogin = userLogin;

        chooseButton.setVisible(false);
        initModelEvents();
        initModelUsersEvents();
        displayAppointmentNotification();
    }

    @FXML
    public void initialize() {

        Platform.setImplicitExit(false);
        chooseButton.setVisible(false);
        eventsListView.setItems(modelEvents);
        eventsListView.setStyle("-fx-selection-bar: #6e6e6e");
        myEventsListView.setItems(modelUsersEvents);
        myEventsListView.setStyle("-fx-selection-bar: #6e6e6e");
        notificationsListView.setItems(modelNotifications.sorted(Comparator.reverseOrder()));
        notificationsListView.setStyle("-fx-selection-bar: #6e6e6e");
    }

    protected void initModelEvents() {
        List<Event> list = new ArrayList<>();
        servEvent.printUs().forEach(list::add);
        modelEvents.clear();
        for(Event ev:list) {
            if(!ev.getDate().isBefore(LocalDate.now())) {
                modelEvents.add(ev);
            }
        }
        //modelEvents.setAll(list);
    }

    protected void initModelUsersEvents() {
        List<Event> list = servEvent.getEventsForUser(userLogin.getId());
        modelUsersEvents.clear();
        for(Event ev : list) {
            if(!ev.getDate().isBefore(LocalDate.now())) {
                modelUsersEvents.add(ev.getName());
            }
        }
    }

    @FXML
    private void onCreateEventButton(ActionEvent event) {
        String name=titleEvent.getText();
        LocalDate dateNew=datePickerEvent.getValue();
        Map<Long,Long> ids=new HashMap<>();
        ids.put(userLogin.getId(),1L);
        Event ev=new Event(name,dateNew,ids);
        try {
            servEvent.save(name, dateNew, ids);
            modelEvents.add(ev);//get 1st user's text from his/her textfield and add message to observablelist
            initModelEvents();
            initModelUsersEvents();
            titleEvent.setText("");//clear 1st user's textfield
            datePickerEvent.getEditor().clear();
        }catch (ValidationException e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
            titleEvent.clear();
            datePickerEvent.getEditor().clear();
        } catch (IllegalArgumentException ee){
            MessageAlert.showErrorMessage(null,"id null");
        }
    }

    @FXML
    public void onEventListViewClick(){
        try {
            Event selected = eventsListView.getSelectionModel().getSelectedItem();
            if(selected!=null)
                lastEventSelected=selected;
            else{
                throw new ValidationException("No Event Selected!");
            }
            long nr = ChronoUnit.DAYS.between(LocalDate.now(), lastEventSelected.getDate());
            if (nr >= 0) {
                if (lastEventSelected != null && !lastEventSelected.getIds().containsKey(userLogin.getId())) {
                    chooseButton.setText("Subscribe!");
                    chooseButton.setVisible(true);
                } else if (lastEventSelected != null && lastEventSelected.getIds().get(userLogin.getId()).longValue() == 1L) {
                    chooseButton.setText("Notifications off");
                    chooseButton.setVisible(true);
                } else if (lastEventSelected != null) {
                    chooseButton.setText("Notifications on");
                    chooseButton.setVisible(true);
                }
            }
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    @FXML
    public void Choose() {

        try {
            Event selected =  eventsListView.getSelectionModel().getSelectedItem();
            if(selected!=null)
                lastEventSelected=selected;

            if(Objects.equals(chooseButton.getText(), "Subscribe!")) {
                Map<Long,Long> ids = lastEventSelected.getIds();

                ids.put(userLogin.getId(), 1L);
                servEvent.update(lastEventSelected.getId(), lastEventSelected.getName(), lastEventSelected.getDate(), ids);
                initModelUsersEvents();
                chooseButton.setText("Notifications off");
            }
            else
            if(Objects.equals(chooseButton.getText(), "Notifications off")) {
                Map<Long,Long> ids = lastEventSelected.getIds();

                ids.put(userLogin.getId(), -1L);
                servEvent.update(lastEventSelected.getId(), lastEventSelected.getName(), lastEventSelected.getDate(), ids);
                chooseButton.setText("Notifications on");
            }
            else
            if(Objects.equals(chooseButton.getText(), "Notifications on")) {
                Map<Long,Long> ids = lastEventSelected.getIds();

                ids.put(userLogin.getId(), 1L);
                servEvent.update(lastEventSelected.getId(), lastEventSelected.getName(), lastEventSelected.getDate(), ids);
                chooseButton.setText("Notifications off");
            }
            initModelEvents();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    private void displayAppointmentNotification() {
        notifyy();
        List<Event> listEv = new ArrayList<>();
        servEvent.printUs().forEach(listEv::add);

        for (Event ev : listEv) {
            long nr = ChronoUnit.DAYS.between(LocalDate.now(), ev.getDate());

            if (nr == 0 && ev.getIds().containsKey(userLogin.getId()) && ev.getIds().get(userLogin.getId()) == 1L) {
                Notifications notificationsBuilder = Notifications.create()
                        .title("New upcoming event")
                        .text(ev.getName() + " it s happening today")
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.BOTTOM_RIGHT);
                notificationsBuilder.darkStyle();
                notificationsBuilder.show();
            }
        }
        initModelNotifEvents();
    }

    public void initModelNotifEvents() {

        List<Notification> listNf = new ArrayList<>();
        servEvent.printUsNotif().forEach(listNf::add);
        List<Notification> listNfNew = new ArrayList<>();

        modelNotifications.clear();
        listNf.forEach(listNfNew::add);
        listNfNew=  listNfNew.stream().sorted(Comparator.comparing(Notification::getNotif)).collect(Collectors.toList());
        //   Collections.reverse(listNfNew);///
        for (Notification nf : listNfNew) {
            if (Objects.equals(nf.getIduser(), userLogin.getId()))
                modelNotifications.add(nf.getNotif());
        }
    }

    public void notifyy() {

        long delay = ChronoUnit.MILLIS.between(LocalTime.now(), LocalTime.of(0, 0, 1));
//        System.out.println(delay);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//        scheduler.schedule(this::startNotifications, delay, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(this::startNotifications, delay, 86400000, TimeUnit.MILLISECONDS);
        initModelNotifEvents();
    }

    public Runnable startNotifications() {

        List<Event> listEv = new ArrayList<>();
        servEvent.printUs().forEach(listEv::add);
        for (Event ev : listEv) {
            long nr = ChronoUnit.DAYS.between(LocalDate.now(), ev.getDate());

            if (nr >= 0 && ev.getIds().containsKey(userLogin.getId()) && ev.getIds().get(userLogin.getId()) == 1L
                    && servEvent.findOneNotificationIdName(userLogin.getId(), LocalDate.now() + " " + ev.getName() + " days left: " + nr) == null
            ) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //update application thread
                        modelNotifications.add(LocalDate.now() + " " + ev.getName() + " days left: " + nr);
                    }
                });
                servEvent.saveNotif(userLogin.getId(), LocalDate.now() + " " + ev.getName() + " days left: " + nr);
            }
        }
        return null;
    }
}
