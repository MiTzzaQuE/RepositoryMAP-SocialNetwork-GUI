package com.example.social_network_gui_v2.controller;

import com.example.social_network_gui_v2.HelloApplication;
import com.example.social_network_gui_v2.domain.Friendship;
import com.example.social_network_gui_v2.domain.User;
import com.example.social_network_gui_v2.domain.UserFriendDTO;
import com.example.social_network_gui_v2.domain.validation.ValidationException;
import com.example.social_network_gui_v2.service.ServiceFriendship;
import com.example.social_network_gui_v2.service.ServiceMessage;
import com.example.social_network_gui_v2.service.ServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MenuController{
    private ServiceUser servUser;
    private ServiceFriendship servFriendship;
    private ServiceMessage servMessage;
    User userLogin;
    Stage dialogStage;
    ObservableList<User> modelUser = FXCollections.observableArrayList();

    @FXML
    TextField First_Name;
    @FXML
    TextField Last_Name;

    @FXML
    TableColumn<User,String> tableColumnID;
    @FXML
    TableColumn<User,String> tableColumnFirstName;
    @FXML
    TableColumn<User,String> tableColumnLastName;
    @FXML
    TableView<User> tableViewUsers;

    public void setService(ServiceUser servUser, ServiceFriendship servFriendship, ServiceMessage servMessage,User user, Stage dialogStage){

        this.servUser = servUser;
        this.servFriendship = servFriendship;
        this.servMessage = servMessage;
        this.userLogin = user;
        this.dialogStage = dialogStage;
        if(user != null){
            setFields(user);
        }
        initModel();
    }

    private void initModel() {

        Iterable<User> users = servUser.getFriends(userLogin.getId());
        List<User> userList = StreamSupport.stream(users.spliterator(),false)
                .collect(Collectors.toList());
        modelUser.setAll(userList);
    }

    @FXML
    public void initialize(){
        //TODO
//        tableColumnID.setCellValueFactory(new PropertyValueFactory<UserFriendDTO, String>("ID"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));

        tableViewUsers.setItems(modelUser);
    }

    private void setFields(User user) {

        First_Name.setText(user.getFirstName());
        Last_Name.setText(user.getLastName());
    }

    @FXML
    public void onAddButtonClick(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-search-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 630, 400);
            Stage stage = new Stage();
            stage.setTitle("Search Friends");
            stage.setScene(scene);

            SearchController searchController = fxmlLoader.getController();
            searchController.setService(servUser,servFriendship,servMessage,userLogin,stage);

            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onDelButtonClick(ActionEvent actionEvent) {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
        if(selectedUser != null) {
            try {
                servFriendship.deleteFriend(userLogin.getId(), selectedUser.getId());
                tableViewUsers.getItems().removeAll(tableViewUsers.getSelectionModel().getSelectedItem());
            } catch (ValidationException validationException) {
                MessageAlert.showErrorMessage(null, validationException.getMessage());
            }
        }
        else MessageAlert.showErrorMessage(null,"No selected user!");

    }

    @FXML
    public void onRequestButtonClick(ActionEvent actionEvent) {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("requests-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 630, 400);
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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        GridPane rootLayout = (GridPane)fxmlLoader.load();
        LoginController loginController = fxmlLoader.getController();
        loginController.setService(servUser,servFriendship,servMessage,dialogStage);
        Scene scene = new Scene(rootLayout, 320, 240);
        dialogStage.setTitle("Log in!");
        dialogStage.setScene(scene);
        dialogStage.show();
    }

}
