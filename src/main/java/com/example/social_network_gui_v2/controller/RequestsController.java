package com.example.social_network_gui_v2.controller;

import com.example.social_network_gui_v2.domain.Friendship;
import com.example.social_network_gui_v2.domain.FriendshipDTO;
import com.example.social_network_gui_v2.domain.User;
import com.example.social_network_gui_v2.domain.validation.ValidationException;
import com.example.social_network_gui_v2.service.ServiceFriendship;
import com.example.social_network_gui_v2.service.ServiceMessage;
import com.example.social_network_gui_v2.service.ServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RequestsController {

    private ServiceUser servUser;
    private ServiceFriendship servFriendship;
    private ServiceMessage servMessage;
    User userLogin;
    ObservableList<FriendshipDTO> modelFriendship = FXCollections.observableArrayList();
    ObservableList<FriendshipDTO> modelSentRequests = FXCollections.observableArrayList();

    @FXML
    public Button acceptBtn;
    @FXML
    public Button rejectBtn;
    @FXML
    public Button cancelBtn;
    @FXML
    public AnchorPane pane1;

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

    public void setService(ServiceUser servUser, ServiceFriendship servFriendship, ServiceMessage servMessage,User user){

        this.servUser = servUser;
        this.servFriendship = servFriendship;
        this.servMessage = servMessage;
        this.userLogin = user;
        initModel();
    }

    private void initModel() {

        Iterable<Friendship> friendships = servUser.getFriendshipRequestForUser(userLogin.getId());
        List<FriendshipDTO> friendshipDTOList = StreamSupport.stream(friendships.spliterator(),false)
                .map(y -> {
                    Button dup1 = new Button();
                    dup1.setStyle(acceptBtn.getStyle());
                    dup1.setOnAction((ActionEvent e) -> onAcceptButtonClick(e));
                    Button dup2 = new Button();
                    dup2.setStyle(rejectBtn.getStyle());
                    dup2.setOnAction((ActionEvent e) -> onRejectButtonClick(e));
                    Button dup3 = new Button();
                    dup3.setStyle(cancelBtn.getStyle());
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

        tableColumnTo.setCellValueFactory(new PropertyValueFactory<FriendshipDTO, String>("userTo"));
        tableColumnCancel.setCellValueFactory(new PropertyValueFactory<FriendshipDTO, Button>("cancelButton"));

        tableViewRequestsSent.setItems(modelSentRequests);

        acceptBtn.setVisible(false);
        rejectBtn.setVisible(false);
        cancelBtn.setVisible(false);
    }

    @FXML
    public void onAcceptButtonClick(ActionEvent actionEvent) {
        System.out.println("click accept");
        FriendshipDTO selected = tableViewFriendhipRequests.getSelectionModel().getSelectedItem();
        if(selected != null) {
            try {
                servFriendship.acceptFriendship(selected.getIdFrom(), selected.getIdTo());
                tableViewFriendhipRequests.getItems().removeAll(tableViewFriendhipRequests.getSelectionModel().getSelectedItem());
            } catch (ValidationException validationException) {
                MessageAlert.showErrorMessage(null, validationException.getMessage());
            }
        }
        else MessageAlert.showErrorMessage(null,"No selected item!");
    }

    @FXML
    public void onRejectButtonClick(ActionEvent actionEvent) {
        System.out.println("click reject");
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
        System.out.println("click cancel");
        FriendshipDTO selected = tableViewRequestsSent.getSelectionModel().getSelectedItem();
        if(selected != null) {
            try {
                servFriendship.deleteFriend(selected.getIdFrom(), selected.getIdTo());
                tableViewRequestsSent.getItems().removeAll(tableViewRequestsSent.getSelectionModel().getSelectedItem());
            } catch (ValidationException validationException) {
                MessageAlert.showErrorMessage(null, validationException.getMessage());
            }
        }
        else MessageAlert.showErrorMessage(null,"No selected item!");
    }
}
