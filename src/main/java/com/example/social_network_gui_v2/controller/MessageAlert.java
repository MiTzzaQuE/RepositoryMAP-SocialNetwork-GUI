package com.example.social_network_gui_v2.controller;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MessageAlert {

    public static void showErrorMessage(Stage owner, String text){
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.initOwner(owner);
        message.setTitle("Error message");
        message.setContentText(text);
        message.showAndWait();
    }
}
