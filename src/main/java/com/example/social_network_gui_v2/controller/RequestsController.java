package com.example.social_network_gui_v2.controller;

import com.example.social_network_gui_v2.domain.Friendship;
import com.example.social_network_gui_v2.domain.FriendshipDTO;
import com.example.social_network_gui_v2.domain.User;
import com.example.social_network_gui_v2.service.ServiceFriendship;
import com.example.social_network_gui_v2.service.ServiceMessage;
import com.example.social_network_gui_v2.service.ServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    @FXML
    TableColumn<FriendshipDTO, String> tableColumnFrom;
    @FXML
    TableColumn<FriendshipDTO, String> tableColumnStatus;
    @FXML
    TableColumn<FriendshipDTO, LocalDateTime> tableColumnDate;
    @FXML
    TableView<FriendshipDTO> tableViewFriendhipRequests;

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
                .map(y -> new FriendshipDTO(servUser.findOne(y.getId().getLeft()).getFirstName() + servUser.findOne(y.getId().getLeft()).getLastName(),
                        servUser.findOne(y.getId().getRight()).getFirstName() + servUser.findOne(y.getId().getRight()).getLastName(),
                        y.getState(), y.getDate()))
                .collect(Collectors.toList());
        modelFriendship.setAll(friendshipDTOList);
    }

    @FXML
    public void initialize() {

        tableColumnFrom.setCellValueFactory(new PropertyValueFactory<FriendshipDTO, String>("userFrom"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<FriendshipDTO, String>("status"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<FriendshipDTO, LocalDateTime>("date"));

        tableViewFriendhipRequests.setItems(modelFriendship);
    }




}
