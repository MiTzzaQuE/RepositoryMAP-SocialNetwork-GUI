package com.example.social_network_gui_v2.controller;

import com.example.social_network_gui_v2.HelloApplication;
import com.example.social_network_gui_v2.domain.Page;
import com.example.social_network_gui_v2.domain.User;
import com.example.social_network_gui_v2.domain.validation.ValidationException;
import com.example.social_network_gui_v2.service.ServiceEvent;
import com.example.social_network_gui_v2.service.ServiceFriendship;
import com.example.social_network_gui_v2.service.ServiceMessage;
import com.example.social_network_gui_v2.service.ServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MenuController{
    protected ServiceUser servUser;
    protected ServiceFriendship servFriendship;
    protected ServiceMessage servMessage;
    protected ServiceEvent servEvent;
    protected List<User> users;
    protected List<User> friends;

    protected Page userLogin;
    protected Stage dialogStage;

    ObservableList<User> modelUser = FXCollections.observableArrayList();
    ObservableList<User> modelFriends = FXCollections.observableArrayList();

    @FXML
    Label Name;
    @FXML
    TextField userFilter;
    @FXML
    TextField friendFilter;
    @FXML
    private Label genderTypeField;
    @FXML
    private DatePicker datePickerDataField;

    @FXML
    Button accountButtonTab;
    @FXML
    Button notificationsEventsButtonTab;
    @FXML
    Button chatButtonTab;

    @FXML
    TableView<User> tableViewUsers;
    @FXML
    TableColumn<User,String> tableColumnFirstNameU;
    @FXML
    TableColumn<User,String> tableColumnLastNameU;
    @FXML
    TableView<User> tableViewFriends;
    @FXML
    TableColumn<User,String> tableColumnFirstNameF;
    @FXML
    TableColumn<User,String> tableColumnLastNameF;


    public void setService(ServiceUser servUser, ServiceFriendship servFriendship, ServiceMessage servMessage, ServiceEvent servEvent,Page user, Stage dialogStage){

        this.servUser = servUser;
        this.servFriendship = servFriendship;
        this.servMessage = servMessage;
        this.servEvent = servEvent;
        this.userLogin = user;
        this.dialogStage = dialogStage;
        if(user != null){
            setFields(user);
        }
        initModelMenu();
    }

    protected void initModelMenu() {

        users = getUsersNoFriends();
        friends = getFriends();
        modelUser.setAll(users);
        modelFriends.setAll(friends);
    }

    @FXML
    public void initialize(){
        //TODO
        tableColumnFirstNameU.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastNameU.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableViewUsers.setItems(modelUser);

        tableColumnFirstNameF.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastNameF.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableViewFriends.setItems(modelFriends);

        userFilter.textProperty().addListener(o -> handleFilter1());
        friendFilter.textProperty().addListener(o -> handleFilter2());
    }

    private void setFields(User user) {
        String name = new String(user.getFirstName()+ " " + user.getLastName());
        Name.setText(name);
    }

    private void handleFilter1() {
        Predicate<User> p1 = n -> n.getFirstName().startsWith(userFilter.getText());
        Predicate<User> p2 = n -> n.getLastName().startsWith(userFilter.getText());

        modelUser.setAll(users
                .stream()
                .filter(p1.or(p2))
                .collect(Collectors.toList()));
    }

    private void handleFilter2() {
            Predicate<User> p1 = n -> n.getFirstName().startsWith(friendFilter.getText());
            Predicate<User> p2 = n -> n.getLastName().startsWith(friendFilter.getText());

            modelFriends.setAll(friends
                    .stream()
                    .filter(p1.or(p2))
                    .collect(Collectors.toList()));
        }

    private List<User> getUsersNoFriends() {
        Iterable<User> users = servUser.printUs();
        List<User> userList = StreamSupport.stream(users.spliterator(),false)
                .collect(Collectors.toList());
        return userList;
    }

    protected List<User> getFriends(){
        Iterable<User> friends = servUser.getFriends(userLogin.getId());
        List<User> friendList = StreamSupport.stream(friends.spliterator(),false)
                .collect(Collectors.toList());
        return friendList;
    }

    @FXML
    public void onAddButtonClick(ActionEvent actionEvent) {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
        if(selectedUser != null) {
            try {
                servFriendship.addFriend(userLogin.getId(), selectedUser.getId());
                tableViewUsers.getItems().removeAll(tableViewUsers.getSelectionModel().getSelectedItem());
            } catch (ValidationException validationException) {
                MessageAlert.showErrorMessage(null, validationException.getMessage());
            }
        }
        else MessageAlert.showErrorMessage(null,"No selected user!");
    }

    @FXML
    public void onDelButtonClick(ActionEvent actionEvent) {
        User selectedUser = tableViewFriends.getSelectionModel().getSelectedItem();
        if(selectedUser != null) {
            try {
                servFriendship.deleteFriend(userLogin.getId(), selectedUser.getId());
                tableViewFriends.getItems().removeAll(tableViewFriends.getSelectionModel().getSelectedItem());
            } catch (ValidationException validationException) {
                MessageAlert.showErrorMessage(null, validationException.getMessage());
            }
        }
        else MessageAlert.showErrorMessage(null,"No selected friend!");
    }

    @FXML
    public void onRequestButtonClick(ActionEvent actionEvent) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("requests-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 630, 450);
            Stage stage = new Stage();
            stage.setTitle("Friendship requests");
            stage.setScene(scene);

            RequestsController requestsController = fxmlLoader.getController();
            requestsController.setService(servUser, servFriendship, servMessage, userLogin);

            stage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onHelpButtonClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onCloseButtonClick(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 630, 450);
            dialogStage.setTitle("Sign In!");
            dialogStage.setScene(scene);

            LoginController loginController = fxmlLoader.getController();
            loginController.setService(servUser, servFriendship, servMessage, servEvent, dialogStage);

            dialogStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ValidationException exception){
            MessageAlert.showErrorMessage(null,exception.getMessage());
        }
        catch (IllegalArgumentException exception){
            MessageAlert.showErrorMessage(null,"ID null!");
        }
    }

    @FXML
    public void handleOpenChatButtonAction(ActionEvent actionEvent) {
    }

    @FXML
    public void handleExportActivityButtonAction(ActionEvent actionEvent) {
    }

    @FXML
    public void handleExportPrivateButtonAction(ActionEvent actionEvent) {
    }

    @FXML
    public void handleChatButtonTab(ActionEvent actionEvent) {

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("chat-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 630, 450);
            dialogStage.setTitle("Chat Menu!");
            dialogStage.setScene(scene);

            ChatController chatController = fxmlLoader.getController();
            chatController.setService(servUser, servFriendship, servMessage, servEvent, userLogin, dialogStage);

            dialogStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ValidationException exception){
            MessageAlert.showErrorMessage(null,exception.getMessage());
        }
        catch (IllegalArgumentException exception){
            MessageAlert.showErrorMessage(null,"Error!");
        }
    }

    @FXML
    public void handleAccountButtonTab(ActionEvent actionEvent) {

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 630, 450);
            dialogStage.setTitle("Main Menu!");
            dialogStage.setScene(scene);

            MenuController menuController = fxmlLoader.getController();
            menuController.setService(servUser, servFriendship, servMessage, servEvent, userLogin, dialogStage);

            dialogStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ValidationException exception){
            MessageAlert.showErrorMessage(null,exception.getMessage());
        }
        catch (IllegalArgumentException exception){
            MessageAlert.showErrorMessage(null,"Error!");
        }
    }

    @FXML
    public void handleNotificationsEventsButtonTab(ActionEvent actionEvent) {

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("notifications-events-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 630, 450);
            dialogStage.setTitle("Notifications/Events Menu!");
            dialogStage.setScene(scene);

            NotificationsEventController notificationsEventController = fxmlLoader.getController();
            notificationsEventController.setService(servUser, servFriendship, servMessage, servEvent, userLogin, dialogStage);

            dialogStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ValidationException exception){
            MessageAlert.showErrorMessage(null,exception.getMessage());
        }
        catch (IllegalArgumentException exception){
            MessageAlert.showErrorMessage(null,"Error!");
        }
    }
}
