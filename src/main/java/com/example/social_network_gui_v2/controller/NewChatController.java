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
import java.util.function.Predicate;
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
    @FXML
    TextField textFieldSearch;
    @FXML
    Label selectedUsers;

    ObservableList<User> modelNewChat = FXCollections.observableArrayList();
    List<Long> newChatUsers = new ArrayList<>();
    List<String> selectedUser = new ArrayList<>();
    List<User> listUsers = new ArrayList<>();
    ChatController controllerChat;

    @FXML
    public void initialize() {

//        buttonAdd.setVisible(false);
        tableColumnFirstNameS.setCellValueFactory(new PropertyValueFactory<User, String>("FirstName"));
        tableColumnLastNameS.setCellValueFactory(new PropertyValueFactory<User, String>("LastName"));
        tableViewUsersSearch.setItems(modelNewChat);

        textFieldSearch.textProperty().addListener(o -> handleFilter());
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
        for (User ur : users)
            if (!newChatUsers.contains(ur.getId()))
                listUsers.add(ur);
        modelNewChat.setAll(listUsers);
    }

    private void handleFilter() {
        Predicate<User> p1 = n -> n.getFirstName().startsWith(textFieldSearch.getText());
        Predicate<User> p2 = n -> n.getLastName().startsWith(textFieldSearch.getText());

        modelNewChat.setAll(listUsers
                .stream()
                .filter(p1.or(p2))
                .collect(Collectors.toList()));
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
                if(Objects.equals(newMess.getText(), "")) {
                    MessageAlert.showErrorMessage(null, "Please write a message!");
                    return;
                }
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
            selectedUser.add(found.getFirstName());
            selectedUsers.setText(String.valueOf(selectedUser));
            if (Objects.equals(found.getId(), userLogin.getId()))
                throw new Exception("This is you");
            newChatUsers.add(found.getId());
            initModelNewChat();
            textFieldSearch.clear();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    @FXML
    public void onButtonCloseSearch(ActionEvent actionEvent) {
    }
}
