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
import java.util.function.Predicate;
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
    private Page user;
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
            user = new Page(userC.getFirstName(),userC.getLastName());
            user.setId(userC.getId());
            initPage();

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 615, 450);
            Stage stage = new Stage();
            stage.setTitle("Main Menu");
            stage.setScene(scene);

            MenuController menuController = fxmlLoader.getController();
            menuController.setService(servUser,servFriendship,servMessage,user,stage);

            stage.show();
            // Hide this current window (if this is what you want)
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
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
    public void handleRegisterButtonAction(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 615, 450);
            Stage stage = new Stage();
            stage.setTitle("Sign Up");
            stage.setScene(scene);

            RegisterController registerController = fxmlLoader.getController();
            registerController.setService(servUser,servFriendship,servMessage,stage);

            stage.show();
            // Hide this current window (if this is what you want)
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
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
        user.setMessages( servMessage.userMessages(user));
    }

    protected void initModelFriendsUser()
    {
        Iterable<User> friends = servUser.getFriends(user.getId());
        List<User> friendList = StreamSupport.stream(friends.spliterator(),false)
                .collect(Collectors.toList());
        user.setFriends(friendList);
    }
//    protected void initModelFriendshipReqUser() {
//        Predicate<FriendRequest> certainUserRight = x -> x.getId().getRight().equals(user.getId());
//
//        List<DtoFriendReq> friendshipsReqList = StreamSupport.stream(serviceFriendRequest.findAllRenew().spliterator(), false)
//                .filter(certainUserRight)
//                .map(x ->
//                {
//                    User u1 = servUser.findOne(x.getId().getLeft());
//                    DtoFriendReq pp=new DtoFriendReq(u1.getFirstName() + " " + u1.getLastName(), user.getFirstName() + " " + user.getLastName(), x.getDate(), x.getStatus());
//                    return pp;
//                })
//                .collect(Collectors.toList());
//
//        user.setFriendRequestsReceived(friendshipsReqList);
//
//    }
//    protected void initModelFriendshipReqUser2() {
//        Predicate<FriendRequest> certainUserLeft = x -> x.getId().getLeft().equals(user.getId());
//
////am facut aici o susta de functie pt findall()
//        List<DtoFriendReq> friendshipsReqList = StreamSupport.stream(serviceFriendRequest.findAllRenew().spliterator(), false)
//                .filter(certainUserLeft)
//                .map(x ->
//                {
//                    User u1 = servUser.findOne(x.getId().getRight());
//                    return new DtoFriendReq(user.getFirstName() + " " + user.getLastName(), u1.getFirstName() + " " + u1.getLastName(), x.getDate(), x.getStatus());
//                })
//                .collect(Collectors.toList());
//        user.setFriendRequestsSent(friendshipsReqList);
//    }
}
