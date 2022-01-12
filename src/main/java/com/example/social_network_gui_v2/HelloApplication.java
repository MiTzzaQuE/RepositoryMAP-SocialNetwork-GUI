package com.example.social_network_gui_v2;

import com.example.social_network_gui_v2.controller.LoginController;
import com.example.social_network_gui_v2.domain.Friendship;
import com.example.social_network_gui_v2.domain.Message;
import com.example.social_network_gui_v2.domain.Tuple;
import com.example.social_network_gui_v2.domain.User;
import com.example.social_network_gui_v2.domain.validation.FriendshipValidator;
import com.example.social_network_gui_v2.domain.validation.MessageValidator;
import com.example.social_network_gui_v2.domain.validation.UserValidator;
import com.example.social_network_gui_v2.repository.paging.PagingRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.GridPane;
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

        PagingRepository<Long,User> repo =
                new UserDbRepository("jdbc:postgresql://localhost:5432/socialnetwork","postgres","1234",new UserValidator());
        PagingRepository<Tuple<Long,Long>, Friendship> repofriends =
                new com.example.social_network_gui_v2.repository.database.FriendshipDbRepository("jdbc:postgresql://localhost:5432/socialnetwork","postgres","1234", new FriendshipValidator());
        Repository<Long, Message> repoMessage =
                new MessageDbRepository("jdbc:postgresql://localhost:5432/socialnetwork","postgres","1234", new MessageValidator());

        ServiceUser serv = new ServiceUser(repo,repofriends);
        ServiceFriendship servFr = new ServiceFriendship(repo,repofriends);
        ServiceMessage servMsg = new ServiceMessage(repo,repoMessage);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        SplitPane rootLayout = (SplitPane)fxmlLoader.load();
        LoginController loginController = fxmlLoader.getController();
        loginController.setService(serv,servFr,servMsg,stage);
        Scene scene = new Scene(rootLayout, 630, 400);
        stage.setTitle("Sign in!");
        stage.setScene(scene);
//        stage.show();


        //TEST PAGING USERS
//        serv.printUs().forEach(System.out::println);
//        serv.setPageSize(6);
//
//
//        System.out.println("\nElements on page 0");
//        serv.getUsersOnPage(0).stream()
//                .forEach(System.out::println);
//
//        System.out.println("\nElements on next page");
//        serv.getNextUsers().stream()
//                .forEach(System.out::println);
//
//        System.out.println("\nElements on next page");
//        serv.getNextUsers().stream()
//                .forEach(System.out::println);


        //TEST PAGING FRIENDS
        System.out.println("\n");
        servFr.printFr().forEach(System.out::println);
        servFr.setPageSize(3);


        System.out.println("\nElements on page 0");
        servFr.getFriendsOnPage(0).stream()
                .forEach(System.out::println);

        System.out.println("\nElements on next page");
        servFr.getNextFriends().stream()
                .forEach(System.out::println);

        System.out.println("\nElements on next page");
        servFr.getNextFriends().stream()
                .forEach(System.out::println);

    }

    public static void main(String[] args) {
        launch();
    }
}