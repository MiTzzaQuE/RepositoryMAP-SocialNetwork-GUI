package com.example.social_network_gui_v2.controller;

import com.example.social_network_gui_v2.domain.Chat;
import com.example.social_network_gui_v2.domain.Message;
import com.example.social_network_gui_v2.domain.Page;
import com.example.social_network_gui_v2.domain.User;
import com.example.social_network_gui_v2.domain.validation.ValidationException;
import com.example.social_network_gui_v2.service.ServiceMessage;
import com.example.social_network_gui_v2.service.ServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NewChatController extends ChatController {

    @FXML
    TableView<User> tableViewUsersSearch;
    @FXML
    TableColumn<User, String> tableColumnFirstNameS;
    @FXML
    TableColumn<User, String> tableColumnLastNameS;
    @FXML
    TextField newMess;
    @FXML
    Button buttonAdd;

    ObservableList<User> modelNewChat = FXCollections.observableArrayList();
    List<Long> newChatUsers = new ArrayList<>();
    ChatController controllerChat;

    @FXML
    public void initialize() {

//        buttonAdd.setVisible(false);
        tableColumnFirstNameS.setCellValueFactory(new PropertyValueFactory<User, String>("FirstName"));
        tableColumnLastNameS.setCellValueFactory(new PropertyValueFactory<User, String>("LastName"));
        tableViewUsersSearch.setItems(modelNewChat);
    }

    public void setService(ServiceUser servUser, ServiceMessage servMessage, Stage stage, Page user, ChatController controller) {

        this.servUser = servUser;
        this.servMessage = servMessage;
        this.dialogStage = stage;
        this.userLogin = user;
        controllerChat=controller;
        newChatUsers.add(userLogin.getId());
        initModelNewChat();
    }

    protected void initModelNewChat() {

        Iterable<User> users = servUser.printUs();
        List<User> listUsers = new ArrayList<>();
        for (User ur : users)
            if (!newChatUsers.contains(ur.getId()))
                listUsers.add(ur);
        modelNewChat.setAll(listUsers);
    }

    @FXML
    public void handleDone() {

        newChatUsers = newChatUsers.stream().sorted().collect(Collectors.toList());
        List<Chat> chats=initModelChat();
        boolean found = false;
        for (Chat ch : chats) {
            if (ch.getPeople().equals(newChatUsers)) {
                Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                informationAlert.setHeaderText("Chat already exists");
                informationAlert.showAndWait();
                found = true;
                break;
            }
        }
        if (!found) {
            try {
                Long Id = servMessage.save(userLogin.getId(), takeToWithoutUserLoginIds(newChatUsers), newMess.getText());
                Message newMessageChat = servMessage.findOne(Id);
                userLogin.addMessage(newMessageChat);

                chatMessages.add(newMessageChat);//get 1st user's text from his/her text field and add message to observable list
                newMess.setText("");//clear 1st user's text field
                controllerChat.initModelChat();

                Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                informationAlert.setHeaderText("Chat created");
                informationAlert.showAndWait();
            }
            catch (ValidationException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
            catch (IllegalArgumentException ee) {
                MessageAlert.showErrorMessage(null, "id null");
            }
        }
        dialogStage.close();
    }

    @FXML
    public void handleAddUser() {

        try {
            User selected = (User) tableViewUsersSearch.getSelectionModel().getSelectedItem();
            User found = servUser.findOne(selected.getId());
            if (Objects.equals(found.getId(), userLogin.getId()))
                throw new Exception("This is you");
            newChatUsers.add(found.getId());
            initModelNewChat();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    @FXML
    public void onButtonCloseSearch(ActionEvent actionEvent) {
    }
}
