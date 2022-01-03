package com.example.social_network_gui_v2.controller;

import com.example.social_network_gui_v2.HelloApplication;
import com.example.social_network_gui_v2.domain.Chat;
import com.example.social_network_gui_v2.domain.Message;
import com.example.social_network_gui_v2.domain.Page;
import com.example.social_network_gui_v2.domain.User;
import com.example.social_network_gui_v2.domain.validation.ValidationException;
import com.example.social_network_gui_v2.service.ServiceFriendship;
import com.example.social_network_gui_v2.service.ServiceMessage;
import com.example.social_network_gui_v2.service.ServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChatController extends MenuController{

    @FXML
    TableView<Chat> tableViewChat;
    @FXML
    TableColumn<Chat, String> tableColumnNameChat;
    @FXML
    Button sendMessage;
    @FXML
    TextField newMessage;
    @FXML
    private ListView<Message> lvChatWindow;

    ObservableList<Chat> modelChat = FXCollections.observableArrayList();
    ObservableList<Message> chatMessages = FXCollections.observableArrayList();//create observable list for listview

    public void setService(ServiceUser servUser, ServiceFriendship servFriendship, ServiceMessage servMessage, Page user, Stage dialogStage){

        this.servUser = servUser;
        this.servFriendship = servFriendship;
        this.servMessage = servMessage;
        this.dialogStage = dialogStage;
        this.userLogin = user;

        initModelChat();
    }

    @FXML
    public void initialize() {
        tableColumnNameChat.setCellValueFactory(new PropertyValueFactory<Chat, String>("name"));
        tableViewChat.setItems(modelChat);
    }


    public List<Chat> initModelChat() {
        Iterable<Message> mess = userLogin.getMessages();

        List<Chat> chats = new ArrayList<>();
        for (Message ms : mess) {
            List<Long> messageInvolved = new ArrayList<>();
            if (Objects.equals(ms.getFrom().getId(), userLogin.getId()) ||
                    ms.getTo().contains(userLogin)) {
                ms.getTo().forEach(x -> messageInvolved.add(x.getId()));
                messageInvolved.add(ms.getFrom().getId());
                List<Long> messageInvolvedSorted = messageInvolved.stream().sorted().collect(Collectors.toList());

                if (!containsPeople(messageInvolvedSorted, chats) && messageInvolved.size() > 2) {
                    Chat chatNew = new Chat("Group " + messageInvolvedSorted, messageInvolvedSorted);
                    chats.add(chatNew);
                }
                if (!containsPeople(messageInvolvedSorted, chats) && messageInvolved.size() == 2) {
                    Long id1 = messageInvolved.get(0);
                    Long id2 = messageInvolved.get(1);
                    Chat chatNew = null;
                    if (messageInvolved.get(0) == userLogin.getId()) {
                        User u2 = servUser.findOne(id2);
                        chatNew = new Chat(u2.getFirstName() + " " + u2.getLastName(), messageInvolvedSorted);
                    } else {
                        User u1 = servUser.findOne(id1);
                        chatNew = new Chat(u1.getFirstName() + " " + u1.getLastName(), messageInvolvedSorted);
                    }
                    chats.add(chatNew);
                }
            }
        }
        modelChat.setAll(chats);
        return chats;
    }

    private boolean containsPeople(List<Long> idsInvolved, List<Chat> chats) {
        for (Chat ch : chats) {
            //System.out.println(ch.getPeople());
            if (ch.getPeople().equals(idsInvolved))
                return true;
        }
        return false;
    }

    public void initializeChat() {
        // TODO
        Chat selected = (Chat) tableViewChat.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;

        chatMessages.setAll(servMessage.groupChat(userLogin.getMessages(), selected.getPeople()));

        lvChatWindow.setItems(chatMessages);//attach the observable list to the listview
        lvChatWindow.setCellFactory(param -> {
            ListCell<Message> cell = new ListCell<Message>() {
                final Label lblUserLeft = new Label();
                final Label lblTextLeft = new Label();
                final HBox hBoxLeft = new HBox(lblUserLeft, lblTextLeft);

                final Label lblUserRight = new Label();
                final Label lblTextRight = new Label();
                final HBox hBoxRight = new HBox(lblTextRight, lblUserRight);

                {
                    hBoxLeft.setAlignment(Pos.CENTER_LEFT);
                    hBoxLeft.setSpacing(5);
                    hBoxRight.setAlignment(Pos.CENTER_RIGHT);
                    hBoxRight.setSpacing(5);
                }

                @Override
                protected void updateItem(Message item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
//                        System.out.println(item.getFrom());
                        if (!item.getFrom().getId().equals(userLogin.getId())) {
                            lblUserLeft.setText(item.getFrom().getFirstName() + " " + item.getFrom().getLastName() + ":");
                            lblTextLeft.setText(item.getMessage());
                            //lblTextLeft.setTextFill(Color.color(1, 0, 0));
                            setGraphic(hBoxLeft);
                        } else {
                            lblUserRight.setText(": " + item.getFrom().getFirstName() + " " + item.getFrom().getLastName());
                            lblTextRight.setText(item.getMessage());
                            setGraphic(hBoxRight);
                        }
                    }
                }
            };
            return cell;
        });
    }

    @FXML
    private void handleUser1SubmitMessage(ActionEvent event) {
        Chat selected = (Chat) tableViewChat.getSelectionModel().getSelectedItem();
        try {
            Long Id = null;
            Message selectedMessage = lvChatWindow.getSelectionModel().getSelectedItem();
            if(selectedMessage != null)
                Id = servMessage.saveReply(userLogin.getId(), newMessage.getText(), selectedMessage.getId());
            else
                Id = servMessage.save(userLogin.getId(), takeToWithoutUserLoginIds(selected.getPeople()), newMessage.getText());

            lvChatWindow.getSelectionModel().clearSelection();

            Message newMess = servMessage.findOne(Id);
            userLogin.addMessage(newMess);
            chatMessages.add(newMess);//get 1st user's text from his/her textfield and add message to observablelist
            newMessage.setText("");//clear 1st user's textfield
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        } catch (IllegalArgumentException ee) {
            MessageAlert.showErrorMessage(null, "id null");
        }
    }

    private List<User> takeToWithoutUserLoginUsers(List<Long> ids) {
        List<User> idsNew = new ArrayList<>();
        for (Long id : ids)
            if (id != userLogin.getId())
                idsNew.add(servUser.findOne(id));
        return idsNew;
    }

    protected List<Long> takeToWithoutUserLoginIds(List<Long> ids) {
        List<Long> idsNew = new ArrayList<>();
        for (Long id : ids)
            if (id != userLogin.getId())
                idsNew.add(id);
        return idsNew;
    }

    @FXML
    private void showNewChatEditDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("chat-new-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 630, 450);
            Stage stage = new Stage();
            stage.setTitle("New Chat Menu!");
            stage.setScene(scene);

            NewChatController controller = fxmlLoader.getController();
            controller.setService(servUser,servMessage,stage,userLogin,this);

            stage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
