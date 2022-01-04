package com.example.social_network_gui_v2.controller;

import com.example.social_network_gui_v2.domain.User;
import com.example.social_network_gui_v2.domain.validation.ValidationException;
import com.example.social_network_gui_v2.service.ServiceFriendship;
import com.example.social_network_gui_v2.service.ServiceMessage;
import com.example.social_network_gui_v2.service.ServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SearchController {
    private ServiceUser servUser;
    private ServiceFriendship servFriendship;
    private ServiceMessage servMessage;
    Stage dialogStage;
    User user;
    ObservableList<User> modelSearch = FXCollections.observableArrayList();

    @FXML
    TextField textFieldSearch;
    @FXML
    TableColumn<User,String> tableColumnFirstNameS;
    @FXML
    TableColumn<User,String> tableColumnLastNameS;
    @FXML
    TableView<User> tableViewUsersSearch;

    public void setService(ServiceUser servUser, ServiceFriendship servFriendship, ServiceMessage servMessage, User user, Stage dialogStage) {
        this.servUser = servUser;
        this.servFriendship = servFriendship;
        this.servMessage = servMessage;
        this.dialogStage = dialogStage;
        this.user = user;
        modelSearch.setAll(getUsersNoFriends());
    }

    @FXML
    private void initialize(){
        tableColumnFirstNameS.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastNameS.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));

        tableViewUsersSearch.setItems(modelSearch);

        textFieldSearch.textProperty().addListener(o -> handleFilter());
    }

    private void handleFilter() {
        Predicate<User> p1 = n -> n.getFirstName().startsWith(textFieldSearch.getText());
        Predicate<User> p2 = n -> n.getLastName().startsWith(textFieldSearch.getText());

        modelSearch.setAll(getUsersNoFriends()
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


    public void onButtonAddFriend(ActionEvent actionEvent) {
        User selectedUser = tableViewUsersSearch.getSelectionModel().getSelectedItem();
        if(selectedUser != null) {
            try {
                servFriendship.addFriend(user.getId(), selectedUser.getId());
                tableViewUsersSearch.getItems().removeAll(tableViewUsersSearch.getSelectionModel().getSelectedItem());
            } catch (ValidationException validationException) {
                MessageAlert.showErrorMessage(null, validationException.getMessage());
            }
        }
        else MessageAlert.showErrorMessage(null,"No selected user!");
    }

    public void onButtonCloseSearch(ActionEvent actionEvent) {
        dialogStage.close();
    }

    public void handleAddUser(ActionEvent actionEvent) {
    }
}
