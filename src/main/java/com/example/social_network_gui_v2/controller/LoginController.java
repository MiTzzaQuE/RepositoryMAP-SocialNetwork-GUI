package com.example.social_network_gui_v2.controller;

import com.example.social_network_gui_v2.HelloApplication;
import com.example.social_network_gui_v2.domain.Page;
import com.example.social_network_gui_v2.domain.User;
import com.example.social_network_gui_v2.domain.validation.ValidationException;
import com.example.social_network_gui_v2.service.ServiceFriendship;
import com.example.social_network_gui_v2.service.ServiceMessage;
import com.example.social_network_gui_v2.service.ServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class LoginController {
    @FXML
    private Text actiontarget;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private ServiceUser servUser;
    private ServiceFriendship servFriendship;
    private ServiceMessage servMessage;
    private Page userLogin;
    Stage dialogStage;

    @FXML
    public void initialize(){ }

    public void setService(ServiceUser servUser, ServiceFriendship servFriendship, ServiceMessage servMessage,Stage dialogStage) {
        this.servUser = servUser;
        this.servFriendship = servFriendship;
        this.servMessage = servMessage;
        this.dialogStage = dialogStage;
    }

    @FXML
    public void handleSubmitButtonAction(ActionEvent actionEvent) {

        actiontarget.setText("Sign in button pressed");

        String ID = usernameField.getText();
        String password = passwordField.getText();

        try {
            User userC = servUser.findUserByUsernamePassword(ID, password);
            userLogin = new Page(userC.getFirstName(),userC.getLastName());
            userLogin.setId(userC.getId());
            initPage();

            showMenuDialogStage();
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

    public void showMenuDialogStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 630, 450);
        dialogStage.setTitle("Main Menu");
        dialogStage.setScene(scene);

        MenuController menuController = fxmlLoader.getController();
        menuController.setService(servUser, servFriendship, servMessage, userLogin, dialogStage);

        dialogStage.show();
    }

    @FXML
    public void handleRegisterButtonAction(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 630, 450);
            dialogStage.setTitle("Sign Up");
            dialogStage.setScene(scene);

            RegisterController registerController = fxmlLoader.getController();
            registerController.setService(servUser,servFriendship,servMessage,dialogStage);

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

    private void initPage() {
        initModelFriendsUser();
//        initModelFriendshipReqUser();
//        initModelFriendshipReqUser2();
        initModelChatUser();

    }

    private void initModelChatUser() {
        userLogin.setMessages( servMessage.userMessages(userLogin));
    }

    protected void initModelFriendsUser()
    {
        Iterable<User> friends = servUser.getFriends(userLogin.getId());
        List<User> friendList = StreamSupport.stream(friends.spliterator(),false)
                .collect(Collectors.toList());
        userLogin.setFriends(friendList);
    }

//    protected void initModelFriendshipReqUser() {
//        Predicate<FriendRequest> certainUserRight = x -> x.getId().getRight().equals(user.getId());
//
//        List<FriendRequestDTO> friendshipsReqList = StreamSupport.stream(servFriendRequest.findAllRenew().spliterator(), false)
//                .filter(certainUserRight)
//                .map(x ->
//                {
//                    User u1 = servUser.findOne(x.getId().getLeft());
//                    FriendRequestDTO pp=new FriendRequestDTO(u1.getFirstName() + " " + u1.getLastName(), user.getFirstName() + " " + user.getLastName(), x.getDate(), x.getStatus());
//                    return pp;
//                })
//                .collect(Collectors.toList());
//        user.setFriendRequestsReceived(friendshipsReqList);
//    }
//
//    protected void initModelFriendshipReqUser2() {
//        Predicate<FriendRequest> certainUserLeft = x -> x.getId().getLeft().equals(user.getId());
//
//        //am facut aici o susta de functie pt findall()
//        List<FriendRequestDTO> friendshipsReqList = StreamSupport.stream(servFriendRequest.findAllRenew().spliterator(), false)
//                .filter(certainUserLeft)
//                .map(x ->
//                {
//                    User u1 = servUser.findOne(x.getId().getRight());
//                    return new FriendRequestDTO(user.getFirstName() + " " + user.getLastName(), u1.getFirstName() + " " + u1.getLastName(), x.getDate(), x.getStatus());
//                })
//                .collect(Collectors.toList());
//        user.setFriendRequestsSent(friendshipsReqList);
//    }
}
