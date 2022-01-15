package com.example.social_network_gui_v2.controller;

import com.example.social_network_gui_v2.HelloApplication;
import com.example.social_network_gui_v2.domain.Friendship;
import com.example.social_network_gui_v2.domain.FriendshipDTO;
import com.example.social_network_gui_v2.domain.Page;
import com.example.social_network_gui_v2.domain.User;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RequestsController extends MenuController{

    private ServiceUser servUser;
    private ServiceFriendship servFriendship;
    private ServiceMessage servMessage;
    Page userLogin;
    ObservableList<FriendshipDTO> modelFriendship = FXCollections.observableArrayList();
    ObservableList<FriendshipDTO> modelSentRequests = FXCollections.observableArrayList();

    @FXML
    public Button acceptBtn;
    @FXML
    public Button rejectBtn;
    @FXML
    public Button cancelBtn;
    @FXML
    public Button simpleBtn;

    @FXML
    TableColumn<FriendshipDTO, String> tableColumnFrom;
    @FXML
    TableColumn<FriendshipDTO, Button> tableColumnAccept;
    @FXML
    TableColumn<FriendshipDTO, Button> tableColumnReject;
    @FXML
    TableView<FriendshipDTO> tableViewFriendhipRequests;

    @FXML
    public TableColumn<FriendshipDTO, String> tableColumnTo;
    @FXML
    public TableColumn<FriendshipDTO, Button> tableColumnCancel;
    @FXML
    public TableView<FriendshipDTO> tableViewRequestsSent;

    public void setService(ServiceUser servUser, ServiceFriendship servFriendship, ServiceMessage servMessage, ServiceEvent servEvent, Stage dialogStage, Page user){

        this.servUser = servUser;
        this.servFriendship = servFriendship;
        this.servMessage = servMessage;
        this.servEvent = servEvent;
        this.dialogStage = dialogStage;
        this.userLogin = user;
        initModelRequest();
    }

    private void initModelRequest() {

        Iterable<Friendship> friendships = servUser.getFriendshipRequestForUser(userLogin.getId());
        List<FriendshipDTO> friendshipDTOList = StreamSupport.stream(friendships.spliterator(),false)
                .map(y -> {
                    Button dup1 = new Button();
                    dup1.setStyle(simpleBtn.getStyle());
                    Image image = new Image(this.getClass().getClassLoader().getResourceAsStream("com/example/social_network_gui_v2/images/accept_icon2.png"),30,20,true,true);
                    ImageView imageView = new ImageView(image);
                    imageView.setPreserveRatio(true);
                    dup1.setGraphic(imageView);
                    dup1.setOnAction((ActionEvent e) -> onAcceptButtonClick(e));

                    Button dup2 = new Button();
                    dup2.setStyle(simpleBtn.getStyle());
                    Image image2 = new Image(this.getClass().getClassLoader().getResourceAsStream("com/example/social_network_gui_v2/images/reject.png"),30,20,true,true);
                    ImageView imageView2 = new ImageView(image2);
                    imageView.setPreserveRatio(true);
                    dup2.setGraphic(imageView2);
                    dup2.setOnAction((ActionEvent e) -> onRejectButtonClick(e));

                    Button dup3 = new Button();
                    dup3.setStyle(simpleBtn.getStyle());
                    dup3.setOnAction((ActionEvent e) -> onCancelButtonClick(e));
                    return new FriendshipDTO(servUser.findOne(y.getId().getLeft()).getId(),servUser.findOne(y.getId().getLeft()).getFirstName() + " " + servUser.findOne(y.getId().getLeft()).getLastName(),
                        servUser.findOne(y.getId().getRight()).getId(),servUser.findOne(y.getId().getRight()).getFirstName() + " " + servUser.findOne(y.getId().getRight()).getLastName(),
                        y.getState(), y.getDate(), dup1, dup2, dup3);})
                .collect(Collectors.toList());
        modelFriendship.setAll(friendshipDTOList);

        Iterable<Friendship> friendshipsSent = servUser.getRequestsSentForUser(userLogin.getId());
        List<FriendshipDTO> friendshipsSentDTO = StreamSupport.stream(friendshipsSent.spliterator(),false)
                .map(y -> {
                    Button dup1 = new Button();
                    dup1.setStyle(acceptBtn.getStyle());
                    dup1.setOnAction((ActionEvent e) -> onAcceptButtonClick(e));
                    Button dup2 = new Button();
                    dup2.setStyle(rejectBtn.getStyle());
                    dup2.setOnAction((ActionEvent e) -> onRejectButtonClick(e));

                    Button dup3 = new Button();
                    dup3.setStyle(cancelBtn.getStyle());
                    Image image = new Image(this.getClass().getClassLoader().getResourceAsStream("com/example/social_network_gui_v2/images/trash.png"),20,20,true,true);
                    ImageView imageView = new ImageView(image);
                    imageView.setPreserveRatio(true);
                    dup3.setGraphic(imageView);

                    dup3.setOnAction((ActionEvent e) -> onCancelButtonClick(e));
                    return new FriendshipDTO(servUser.findOne(y.getId().getLeft()).getId(),servUser.findOne(y.getId().getLeft()).getFirstName() + " " + servUser.findOne(y.getId().getLeft()).getLastName(),
                        servUser.findOne(y.getId().getRight()).getId(),servUser.findOne(y.getId().getRight()).getFirstName() + " " + servUser.findOne(y.getId().getRight()).getLastName(),
                        y.getState(), y.getDate(), dup1, dup2, dup3);})
                .collect(Collectors.toList());
        modelSentRequests.setAll(friendshipsSentDTO);
    }

    @FXML
    public void initialize() {

        tableColumnFrom.setCellValueFactory(new PropertyValueFactory<FriendshipDTO, String>("userFrom"));
        tableColumnAccept.setCellValueFactory(new PropertyValueFactory<FriendshipDTO, Button>("acceptButton"));
        tableColumnReject.setCellValueFactory(new PropertyValueFactory<FriendshipDTO, Button>("rejectButton"));
        tableViewFriendhipRequests.setItems(modelFriendship);
        tableViewFriendhipRequests.setStyle("-fx-selection-bar: #6e6e6e");

        tableColumnTo.setCellValueFactory(new PropertyValueFactory<FriendshipDTO, String>("userTo"));
        tableColumnCancel.setCellValueFactory(new PropertyValueFactory<FriendshipDTO, Button>("cancelButton"));
        tableViewRequestsSent.setItems(modelSentRequests);
        tableViewRequestsSent.setStyle("-fx-selection-bar: #6e6e6e");

        acceptBtn.setVisible(false);
        rejectBtn.setVisible(false);
        cancelBtn.setVisible(false);
        simpleBtn.setVisible(false);
    }

    @FXML
    public void onAcceptButtonClick(ActionEvent actionEvent) {
        FriendshipDTO selected = tableViewFriendhipRequests.getSelectionModel().getSelectedItem();
        if(selected != null) {
            try {
                servFriendship.acceptFriendship(selected.getIdFrom(), selected.getIdTo());
                tableViewFriendhipRequests.getItems().removeAll(tableViewFriendhipRequests.getSelectionModel().getSelectedItem());
                System.out.println(userLogin);

            } catch (ValidationException validationException) {
                MessageAlert.showErrorMessage(null, validationException.getMessage());
            }
        }
        else MessageAlert.showErrorMessage(null,"No selected item!");
    }

    @FXML
    public void onRejectButtonClick(ActionEvent actionEvent) {
        deteleFriendRequest(tableViewFriendhipRequests);
    }

    protected void deteleFriendRequest(TableView<FriendshipDTO> tableViewFriendhipRequests) {
        FriendshipDTO selected = tableViewFriendhipRequests.getSelectionModel().getSelectedItem();
        if(selected != null) {
            try {
                servFriendship.deleteFriend(selected.getIdFrom(), selected.getIdTo());
                tableViewFriendhipRequests.getItems().removeAll(tableViewFriendhipRequests.getSelectionModel().getSelectedItem());
            } catch (ValidationException validationException) {
                MessageAlert.showErrorMessage(null, validationException.getMessage());
            }
        }
        else MessageAlert.showErrorMessage(null,"No selected item!");
    }

    @FXML
    public void onCancelButtonClick(ActionEvent actionEvent) {
        deteleFriendRequest(tableViewRequestsSent);
    }

    public void onBackButtonClick(ActionEvent actionEvent) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 630, 450);
            dialogStage.setTitle("Main Menu!");
            dialogStage.setScene(scene);

            MenuController menuController = fxmlLoader.getController();
            menuController.setService(servUser, servFriendship, servMessage, servEvent, userLogin, dialogStage);

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
}
