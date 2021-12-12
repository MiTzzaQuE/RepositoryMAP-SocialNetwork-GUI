package com.example.social_network_gui_v2;

import com.example.social_network_gui_v2.domain.Friendship;
import com.example.social_network_gui_v2.domain.Message;
import com.example.social_network_gui_v2.domain.Tuple;
import com.example.social_network_gui_v2.domain.User;
import com.example.social_network_gui_v2.domain.validation.FriendshipValidator;
import com.example.social_network_gui_v2.domain.validation.MessageValidator;
import com.example.social_network_gui_v2.domain.validation.UserValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.social_network_gui_v2.repository.Repository;
import com.example.social_network_gui_v2.repository.database.MessageDbRepository;
import com.example.social_network_gui_v2.repository.database.UserDbRepository;
import com.example.social_network_gui_v2.service.ServiceFriendship;
import com.example.social_network_gui_v2.service.ServiceMessage;
import com.example.social_network_gui_v2.service.ServiceUser;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Repository<Long, User> repo =
                new UserDbRepository("jdbc:postgresql://localhost:5432/socialnetwork","postgres","1234",new UserValidator());
        Repository<Tuple<Long,Long>, Friendship> repofriends =
                new com.example.social_network_gui_v2.repository.database.FriendshipDbRepository("jdbc:postgresql://localhost:5432/socialnetwork","postgres","1234", new FriendshipValidator());
        Repository<Long, Message> repoMessage =
                new MessageDbRepository("jdbc:postgresql://localhost:5432/socialnetwork","postgres","1234", new MessageValidator());

        ServiceUser serv = new ServiceUser(repo,repofriends);
        ServiceFriendship servFr = new ServiceFriendship(repo,repofriends);
        ServiceMessage servMsg = new ServiceMessage(repo,repoMessage);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("RideWithMe!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}