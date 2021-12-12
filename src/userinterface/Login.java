package userinterface;

import domain.Friendship;
import domain.Message;
import domain.User;
import domain.UserFriendDTO;
import domain.validation.ValidationException;
import service.ServiceFriendship;
import service.ServiceMessage;
import service.ServiceUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * UI for the login
 * ID- the ID of the user logged in
 * servMessage - service of messages
 * servUser - service of users
 */
public class Login {
    ServiceMessage servMessage;
    ServiceUser servUser;
    ServiceFriendship servFriendship;
    UI ui;
    User currentUser;

    /**
     * Constructor
     * @param serviceMessage - serviceMessage
     * @param serviceUser - serviceUser
     */
    public Login(ServiceMessage serviceMessage, ServiceUser serviceUser, ServiceFriendship serviceFriendship, UI ui) {
        this.servMessage = serviceMessage;
        this.servUser = serviceUser;
        this.servFriendship = serviceFriendship;
        this.ui = ui;
    }

    /**
     * Function which run the application with its all menus
     */
    public void run(){
        Scanner in = new Scanner(System.in);
        boolean running = true;
        while(running){
            showUserOptions();
            System.out.println("Enter your option: ");
            String option = in.nextLine();

            switch (option){
                case "1" -> {
                    System.out.println("\nRegister down below. Please provide: first name, last name\n");
                    ui.adduser();
                }
                case "2" ->
                    //for log in use
                    userLogin();
                case "3" ->
                    ui.show();
                case "x" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("wrong command");
            }
        }
    }

    /**
     *
     */
    private void userLogin() {

        currentUser = login();
        if (currentUser == null )
            return;

        System.out.println("\nLogin approved!\n");

        boolean logged = true;
        showMenu();
        while(logged){
            System.out.println("Enter your option:");
            Scanner loggedUserInput = new Scanner(System.in);
            String userOption = loggedUserInput.nextLine();

            switch (userOption) {
                case "1" ->
                    //do the sending friendship stuff
                    sendFriendshipRequest();
                case "2" -> {
                    //show the friendship requests for the logged user
                    showFriendshipRequests();
                    manageFriendshipRequests();
                }
                case "3" ->
                    //show all the friends for the current logged user
                    showUserLoggedFriends();
                case "4" ->
                    //add a message
                    addMessage();
                case "5" ->
                    //reply to a message
                    addReply();
                case "6" ->
                    //show conversation
                    showPrivateChat();
                case "x" ->
                    //logout
                    logged = false;
                default -> System.out.println("wrong command");
            }
        }
    }

    /**
     *
     */
    private void showMenu() {
        System.out.println("Current logged user: "  + currentUser.getFirstName() + " " + currentUser.getLastName());
        System.out.println("""
        1.Send a friendship request
        2.Show my friendship requests
        3.Show all my friends
        4.Sent a message
        5.Reply to a message
        6.Show conversation with another user
        x.Logout
        """);
    }

    /**
     *
     */
    private void showUserOptions(){
        System.out.println("""
        1.Register
        2.Login
        3.Menu
        x.Exit""");
    }

    /**
     * Function that login a user to the application
     * @return the user if it exists on database
     */
    private User login(){
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Enter ID: ");
            Long id = Long.parseLong(scanner.nextLine());
            return servUser.findOne(id);
        }catch (NumberFormatException ex){
            ex.printStackTrace();
        }catch (ValidationException ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }

    /**
     * Read the data and try to add a message
     */
    private void addMessage(){

        Scanner scanner = new Scanner(System.in);

        List<Long> to = new ArrayList<>();
        System.out.println("Message:");
        String msg = scanner.nextLine();
        String cmd;
        while(true){
            System.out.println("Choose a user id\nX-STOP");
            cmd = scanner.nextLine();
            if(Objects.equals(cmd,"x"))
                break;
            try{
                long id = Long.parseLong(cmd);
                servUser.findOne(id);
                to.add(id);
            }
            catch (NumberFormatException exception){
                System.out.println(cmd + "is not a valid id");
            }
            catch (ValidationException exception){
                System.out.println(exception.getMessage());
            }
        }
        try{
            servMessage.save(currentUser.getId(),to,msg);
            System.out.println("Message sent!");
        }
        catch (ValidationException | IllegalArgumentException exception){
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Read the data and try to save a reply message
     */
    private void addReply(){
        Scanner scanner = new Scanner(System.in);
        String cmd;
        long idReply;
        System.out.println("Give id of the message");
        cmd = scanner.nextLine();
        try{
            idReply = Long.parseLong(cmd);
            System.out.println("Message:");
            String msg = scanner.nextLine();
            servMessage.saveReply(currentUser.getId(),msg,idReply);
            System.out.println("Reply message sent!");
        }
        catch (NumberFormatException e){
            System.out.println(cmd + " is not a valid id!");
        }
        catch (ValidationException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     *
     */
    private void showPrivateChat(){

        Scanner scanner = new Scanner(System.in);
        String id = "";

        try{
            System.out.println("Id of second user:");
            id = scanner.nextLine();
            long id2 = Long.parseLong(id);
            servUser.findOne(id2);
            List<Message> conversation = servMessage.PrivateChat(currentUser.getId(),id2);
            conversation.forEach(System.out::println);
        }
        catch (NumberFormatException e){
            System.out.println(id + " is not a valid id!");
        }
        catch (ValidationException exception){
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Sends a friendship request for the current logged user
     * The friendship status : Pending
     * Waiting for the other user to accept/reject the friendhip request (login required)
     */
    private void sendFriendshipRequest(){
        try{
            Scanner scanner = new Scanner(System.in);
            System.out.println("""
            Sending friendship request to
            Enter user ID you want to add as friend:""");
            Long id = Long.parseLong(scanner.nextLine());
            servFriendship.addFriend(currentUser.getId(),id);
            System.out.println("Friendship request sent successfully!");

        }catch (NumberFormatException ex){
            System.out.println("Please provide a correct number!");
        }catch (ValidationException ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Shows for the current logged user all the friendship requests
     */
    private void showFriendshipRequests(){
        System.out.println("Friendship requests: ");
        for(Friendship friendship : servUser.getFriendshipRequestForUser(currentUser.getId())) {
            System.out.println("From: " + servUser.findOne(friendship.getId().getLeft()));
        }
    }

    /**
     * Allows the current logged user to accept or reject all the friendship requests
     */
    private void manageFriendshipRequests(){
        Scanner scanner = new Scanner(System.in);
        List<Friendship> friendships = (List<Friendship>) servUser.getFriendshipRequestForUser(currentUser.getId());
        if ( friendships.size()==0 ) {
            System.out.println("Does not have friend requests!");
            return;
        }
        System.out.println("""
        Do you want to accept/reject your friendship requests?
        Type y-for yes or n-for no""");
        String response = scanner.nextLine();

        switch (response) {
            case "y":
                for (Friendship friendship : servUser.getFriendshipRequestForUser(currentUser.getId())) {
                    System.out.println("Request from: " + servUser.findOne(friendship.getId().getLeft()));
                    System.out.println("approve or reject?");
                    String state = scanner.nextLine();
                    if (state.equals("approve")) {
                        servFriendship.update(friendship.getId().getLeft(), friendship.getId().getRight(), "Approved");
                        servFriendship.acceptFriendship(friendship.getId().getLeft(), friendship.getId().getRight());
                    } else if (state.equals("reject")) {
                        servFriendship.update(friendship.getId().getLeft(), friendship.getId().getRight(), "Rejected");
                        servFriendship.rejectFriendship(friendship.getId().getLeft(), friendship.getId().getRight());
                    } else {
                        System.out.println("Wrong command! Type approve or reject!");
                    }
                }
                break;
            case "n":
                break;
            default:
                System.out.println("wrong command! Type y or n");
                break;
        }
    }

    /**
     * Shows all the friends for the current logged user
     */
    private void showUserLoggedFriends(){
        for(UserFriendDTO friend : servUser.getFriendsForUser(currentUser.getId())){
            System.out.println(friend);
        }
    }
}
