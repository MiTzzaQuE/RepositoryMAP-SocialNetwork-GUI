package com.example.social_network_gui_v2.controller;

import com.example.social_network_gui_v2.HelloApplication;
import com.example.social_network_gui_v2.domain.User;
import com.example.social_network_gui_v2.domain.validation.ValidationException;
import com.example.social_network_gui_v2.service.ServiceFriendship;
import com.example.social_network_gui_v2.service.ServiceMessage;
import com.example.social_network_gui_v2.service.ServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegisterController {
    @FXML
    private Text actiontarget;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private ComboBox genderComboBox;


    List<String> genders = new ArrayList<>();

    private ServiceUser servUser;
    private ServiceFriendship servFriendship;
    private ServiceMessage servMessage;
    private User user;
    Stage dialogStage;

    @FXML
    public void initialize(){ }

    public void setService(ServiceUser servUser, ServiceFriendship servFriendship, ServiceMessage servMessage,Stage dialogStage) {
        this.servUser = servUser;
        this.servFriendship = servFriendship;
        this.servMessage = servMessage;
        this.dialogStage = dialogStage;
        genders.add("Male");
        genders.add("Female");
        genders.add("Other");
        genderComboBox.getItems().setAll(genders);
    }

    @FXML
    public void handleSignInButtonAction(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 630, 400);
            dialogStage.setTitle("Sign In!");
            dialogStage.setScene(scene);

            LoginController loginController = fxmlLoader.getController();
            loginController.setService(servUser, servFriendship, servMessage, dialogStage);

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
    public void handleSignUpAction(ActionEvent actionEvent) {

//        actiontarget.setText("Sign in button pressed");

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String userName = userNameField.getText();
        String password = passwordField.getText();
        String confrim = confirmPasswordField.getText();
        user = new User(firstName,lastName);

        try {
            if(password.equals(confrim))
                servUser.save(firstName,lastName,userName,password);
            else
                MessageAlert.showErrorMessage(null,"Invalid Password!");

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menu-v2.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 615, 450);
            dialogStage.setTitle("Main Menu");
            dialogStage.setScene(scene);

            MenuController menuController = fxmlLoader.getController();
            menuController.setService(servUser,servFriendship,servMessage,user,dialogStage);

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
}
