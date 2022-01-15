package com.example.social_network_gui_v2.controller;

import com.example.social_network_gui_v2.HelloApplication;
import com.example.social_network_gui_v2.domain.Message;
import com.example.social_network_gui_v2.domain.Page;
import com.example.social_network_gui_v2.domain.User;
import com.example.social_network_gui_v2.domain.UserFriendDTO;
import com.example.social_network_gui_v2.domain.validation.ValidationException;
import com.example.social_network_gui_v2.service.ServiceEvent;
import com.example.social_network_gui_v2.service.ServiceFriendship;
import com.example.social_network_gui_v2.service.ServiceMessage;
import com.example.social_network_gui_v2.service.ServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ScrollEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MenuController{


    protected ServiceUser servUser;
    protected ServiceFriendship servFriendship;
    protected ServiceMessage servMessage;
    protected ServiceEvent servEvent;
    protected List<User> users;
    protected List<User> friends;

    protected Page userLogin;
    protected Stage dialogStage;

    ObservableList<User> modelUser = FXCollections.observableArrayList();
    ObservableList<User> modelFriends = FXCollections.observableArrayList();

    @FXML
    Label Name;
    @FXML
    public Label pageNumber;
    @FXML
    TextField userFilter;
    @FXML
    TextField friendFilter;
    @FXML
    private Label genderTypeField;
    @FXML
    private DatePicker datePickerDataField;

    @FXML
    Button accountButtonTab;
    @FXML
    Button notificationsEventsButtonTab;
    @FXML
    Button chatButtonTab;

    @FXML
    public DatePicker dateStart;
    @FXML
    public DatePicker dateEnd;

    @FXML
    TableView<User> tableViewUsers;
    @FXML
    TableColumn<User,String> tableColumnFirstNameU;
    @FXML
    TableColumn<User,String> tableColumnLastNameU;
    @FXML
    TableView<User> tableViewFriends;
    @FXML
    TableColumn<User,String> tableColumnFirstNameF;
    @FXML
    TableColumn<User,String> tableColumnLastNameF;


    public void setService(ServiceUser servUser, ServiceFriendship servFriendship, ServiceMessage servMessage, ServiceEvent servEvent,Page user, Stage dialogStage){

        this.servUser = servUser;
        this.servFriendship = servFriendship;
        this.servMessage = servMessage;
        this.servEvent = servEvent;
        this.userLogin = user;
        this.dialogStage = dialogStage;
        if(user != null){
            setFields(user);
        }
        initModelMenu();
    }

    protected void initModelMenu() {

        users = servUser.getUsersOnPage(0);
        friends = getFriends();
        modelUser.setAll(users);
        modelFriends.setAll(friends);
        datePickerDataField.setValue(LocalDate.parse("2001-09-06"));
    }

    @FXML
    public void initialize(){
        //TODO
        tableColumnFirstNameU.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastNameU.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableViewUsers.setItems(modelUser);
        tableViewUsers.setStyle("-fx-selection-bar: #6e6e6e");

        tableColumnFirstNameF.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastNameF.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableViewFriends.setItems(modelFriends);
        tableViewFriends.setStyle("-fx-selection-bar: #6e6e6e");

        userFilter.textProperty().addListener(o -> handleFilter1());
        friendFilter.textProperty().addListener(o -> handleFilter2());
    }

    private void setFields(User user) {
        String name = new String(user.getFirstName()+ " " + user.getLastName());
        Name.setText(name);
        pageNumber.setText("1");
    }

    private void handleFilter1() {
        Predicate<User> p1 = n -> n.getFirstName().startsWith(userFilter.getText());
        Predicate<User> p2 = n -> n.getLastName().startsWith(userFilter.getText());

        modelUser.setAll(getUsersNoFriends()
                .stream()
                .filter(p1.or(p2))
                .collect(Collectors.toList()));
    }

    private void handleFilter2() {
            Predicate<User> p1 = n -> n.getFirstName().startsWith(friendFilter.getText());
            Predicate<User> p2 = n -> n.getLastName().startsWith(friendFilter.getText());

            modelFriends.setAll(friends
                    .stream()
                    .filter(p1.or(p2))
                    .collect(Collectors.toList()));
        }

    private List<User> getUsersNoFriends() {
        Iterable<User> users = servUser.printUs();
        List<User> userList = StreamSupport.stream(users.spliterator(),false)
                .collect(Collectors.toList());
        return userList;
    }

    protected List<User> getFriends(){
        Iterable<User> friends = servUser.getFriends(userLogin.getId());
        List<User> friendList = StreamSupport.stream(friends.spliterator(),false)
                .collect(Collectors.toList());
        return friendList;
    }

    @FXML
    public void onAddButtonClick(ActionEvent actionEvent) {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
        if(selectedUser != null) {
            try {
                servFriendship.addFriend(userLogin.getId(), selectedUser.getId());
                tableViewUsers.getItems().removeAll(tableViewUsers.getSelectionModel().getSelectedItem());
            } catch (ValidationException validationException) {
                MessageAlert.showErrorMessage(null, validationException.getMessage());
            }
        }
        else MessageAlert.showErrorMessage(null,"No selected user!");
    }

    @FXML
    public void onDelButtonClick(ActionEvent actionEvent) {
        User selectedUser = tableViewFriends.getSelectionModel().getSelectedItem();
        if(selectedUser != null) {
            try {
                servFriendship.deleteFriend(userLogin.getId(), selectedUser.getId());
                tableViewFriends.getItems().removeAll(tableViewFriends.getSelectionModel().getSelectedItem());
            } catch (ValidationException validationException) {
                MessageAlert.showErrorMessage(null, validationException.getMessage());
            }
        }
        else MessageAlert.showErrorMessage(null,"No selected friend!");
    }

    @FXML
    public void onRequestButtonClick(ActionEvent actionEvent) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("requests-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 630, 450);
//            Stage stage = new Stage();
            dialogStage.setTitle("Friendship requests!");
            dialogStage.setScene(scene);

            RequestsController requestsController = fxmlLoader.getController();
            requestsController.setService(servUser, servFriendship, servMessage, servEvent, dialogStage, userLogin);

            dialogStage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onCloseButtonClick(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 630, 450);
            dialogStage.setTitle("Sign In!");
            dialogStage.setScene(scene);

            LoginController loginController = fxmlLoader.getController();
            loginController.setService(servUser, servFriendship, servMessage, servEvent, dialogStage);

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
    public void handleExportActivityButtonAction(ActionEvent actionEvent) throws IOException {

        if(dateStart.getValue() == null || dateEnd.getValue() == null)
            MessageAlert.showErrorMessage(null,"No selected dates!");
        else {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.beginText();
            contentStream.setFont(PDType1Font.COURIER, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText("Friendships made in this period of time:");
            contentStream.newLine();

            Iterable<UserFriendDTO> friendsMade = servUser.getFriendsForUser(userLogin.getId());
            List<UserFriendDTO> friends = StreamSupport.stream(friendsMade.spliterator(), false).collect(Collectors.toList());
            friends.forEach(f -> {
                if (f.getFriendshipDate().isAfter(dateStart.getValue().atStartOfDay()) && f.getFriendshipDate().isBefore(dateEnd.getValue().atStartOfDay())) {
                    try {
                        contentStream.newLine();
                        contentStream.showText(f.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            contentStream.newLine();
            contentStream.newLine();
            contentStream.showText("Messages sent and receved in this period of time:");
            contentStream.newLine();

            List<Message> messages = servMessage.userMessages(userLogin);
            extractMessagesForExport(contentStream, messages);

            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Choose path to export!");
            File selectedDirectory = chooser.showDialog(null);

            document.save(selectedDirectory.getAbsolutePath() + "/Export1.pdf");
            document.close();
        }
    }

    @FXML
    public void handleExportPrivateButtonAction(ActionEvent actionEvent) throws IOException {

        User selectedFriend = tableViewFriends.getSelectionModel().getSelectedItem();
        if(dateStart.getValue() == null || dateEnd.getValue() == null)
            MessageAlert.showErrorMessage(null,"No selected dates!");
        else if(selectedFriend != null){
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage( page );

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.beginText();
            contentStream.setFont(PDType1Font.COURIER, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText("Private chat with " + selectedFriend.getFirstName() + " " + selectedFriend.getLastName() + " in this period of time:");
            contentStream.newLine();

            List<Message> messages = servMessage.PrivateChat(userLogin.getId(), selectedFriend.getId());
            extractMessagesForExport(contentStream, messages);

            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Choose path to export!");
            File selectedDirectory = chooser.showDialog(null);

            document.save(selectedDirectory.getAbsolutePath() + "/Export2.pdf");
            document.close();
        }
        else MessageAlert.showErrorMessage(null,"No selected user!");

    }

    private void extractMessagesForExport(PDPageContentStream contentStream, List<Message> messages) throws IOException {
        messages.sort(Comparator.comparing(Message::getId));
        messages.forEach(m -> {
            if(m.getDate().isAfter(dateStart.getValue().atStartOfDay()) && m.getDate().isBefore(dateEnd.getValue().atStartOfDay())){
                try {
                    contentStream.newLine();
                    contentStream.showText(m.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        contentStream.endText();

        contentStream.close();
    }

    @FXML
    public void handleAccountButtonTab(ActionEvent actionEvent) {

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 630, 450);
            dialogStage.setTitle("Main Menu!");
            dialogStage.setScene(scene);

            MenuController menuController = fxmlLoader.getController();
            menuController.setService(servUser, servFriendship, servMessage, servEvent, userLogin, dialogStage);

            dialogStage.show();

            accountButtonTab.setStyle("-fx-border-color: #ffffff");;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ValidationException exception){
            MessageAlert.showErrorMessage(null,exception.getMessage());
        }
        catch (IllegalArgumentException exception){
            MessageAlert.showErrorMessage(null,"Error!");
        }
    }

    @FXML
    public void handleNotificationsEventsButtonTab(ActionEvent actionEvent) {

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("notifications-events-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 630, 450);
            dialogStage.setTitle("Notifications/Events Menu!");
            dialogStage.setScene(scene);

            NotificationsEventController notificationsEventController = fxmlLoader.getController();
            notificationsEventController.setService(servUser, servFriendship, servMessage, servEvent, userLogin, dialogStage);

            dialogStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ValidationException exception){
            MessageAlert.showErrorMessage(null,exception.getMessage());
        }
        catch (IllegalArgumentException exception){
            MessageAlert.showErrorMessage(null,"Error!");
        }
    }

    @FXML
    public void handleChatButtonTab(ActionEvent actionEvent) {

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("chat-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 630, 450);
            dialogStage.setTitle("Chat Menu!");
            dialogStage.setScene(scene);

            ChatController chatController = fxmlLoader.getController();
            chatController.setService(servUser, servFriendship, servMessage, servEvent, userLogin, dialogStage);

            dialogStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ValidationException exception){
            MessageAlert.showErrorMessage(null,exception.getMessage());
        }
        catch (IllegalArgumentException exception){
            MessageAlert.showErrorMessage(null,"Error!");
        }
    }

    @FXML
    protected void onPreviousButtonClick(ActionEvent actionEvent) {
        try{
            users = servUser.getPreviousUsers();
            modelUser.setAll(users);
            int pageNr = servUser.getPageNumber();
            pageNumber.setText(Integer.toString(pageNr+1));
        }catch (ValidationException ex){
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }

    @FXML
    protected void onNextButtonClick(ActionEvent actionEvent) {
        try {
            users = servUser.getNextUsers();
            modelUser.setAll(users);
            int pageNr = servUser.getPageNumber();
            pageNumber.setText(Integer.toString(pageNr+1));
        }catch (ValidationException ex) {
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }
}
