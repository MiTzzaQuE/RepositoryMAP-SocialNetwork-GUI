package com.example.social_network_gui_v2;

import com.example.social_network_gui_v2.controller.LoginController;
import com.example.social_network_gui_v2.domain.*;
import com.example.social_network_gui_v2.domain.validation.EventValidator;
import com.example.social_network_gui_v2.domain.validation.FriendshipValidator;
import com.example.social_network_gui_v2.domain.validation.MessageValidator;
import com.example.social_network_gui_v2.domain.validation.UserValidator;
import com.example.social_network_gui_v2.repository.paging.PagingRepository;
import com.example.social_network_gui_v2.repository.database.*;
import com.example.social_network_gui_v2.service.ServiceEvent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import com.example.social_network_gui_v2.repository.Repository;
import com.example.social_network_gui_v2.service.ServiceFriendship;
import com.example.social_network_gui_v2.service.ServiceMessage;
import com.example.social_network_gui_v2.service.ServiceUser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

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
        Repository<Long, Event> repoEvents =
                new EventDbRepository("jdbc:postgresql://localhost:5432/socialnetwork","postgres","1234", new EventValidator());
        Repository<Long,Notification> repoNotifications =
                new NotificationDbRepository("jdbc:postgresql://localhost:5432/socialnetwork","postgres","1234", new EventValidator());

        ServiceUser serv = new ServiceUser(repo,repofriends);
        ServiceFriendship servFr = new ServiceFriendship(repo,repofriends);
        ServiceMessage servMsg = new ServiceMessage(repo,repoMessage);
        ServiceEvent servEvt = new ServiceEvent(repoEvents,repoNotifications);

        showLoginDialogStage(stage, serv, servFr, servMsg, servEvt);

    }

    private void showLoginDialogStage(Stage stage, ServiceUser serv, ServiceFriendship servFr, ServiceMessage servMsg, ServiceEvent servEvt) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        SplitPane rootLayout = (SplitPane)fxmlLoader.load();
        LoginController loginController = fxmlLoader.getController();
        loginController.setService(serv, servFr, servMsg, servEvt, stage);
        Scene scene = new Scene(rootLayout, 630, 400);
        stage.setTitle("Sign in!");
        stage.setScene(scene);
        stage.show();


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


        //TEST PAGING
//        serv.printUs().forEach(System.out::println);
//        serv.setPageSize(6);
//
//
//        System.out.println("\nElements on page 0");
//        serv.getUsersOnPage(0).stream()
//                .forEach(System.out::println);

//        System.out.println("\nElements on page 1");
//        serv.getUsersOnPage(1).stream()
//                .forEach(System.out::println);
//
//        System.out.println("\nElements on page 2");
//        serv.getUsersOnPage(2).stream()
//                .forEach(System.out::println);

//        System.out.println("\nElements on next page");
//        serv.getNextUsers().stream()
//                .forEach(System.out::println);
//
//        System.out.println("\nElements on next page");
//        serv.getNextUsers().stream()
//                .forEach(System.out::println);
//
   }


    public static void main(String[] args) {
        launch();
    }
}