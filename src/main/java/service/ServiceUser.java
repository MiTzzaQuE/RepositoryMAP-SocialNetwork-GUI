package service;

import domain.*;
import domain.validation.ValidationException;
import repository.Repository;
import utils.Months;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * class Service
 * repoUser-Repository for Users
 * repoFriends-Repository for Friendships
 */
public class ServiceUser {
    private final Repository<Long, User> repoUser;
    private final Repository<Tuple<Long, Long>, Friendship> repoFriends;

    /**
     * constructor for the service
     * @param RepoUser UserRepository
     * @param RepoFriends FriendsRepository
     */
    public ServiceUser(Repository<Long, User> RepoUser, Repository<Tuple<Long, Long>, Friendship> RepoFriends) {
        repoFriends = RepoFriends;
        repoUser = RepoUser;
    }


    /**
     * save a user, call the repo function
     * if is not valid throw ValidationException
     * @param firstname - String
     * @param lastname - String
     */
    public void save(String firstname, String lastname) {
        User user = new User(firstname, lastname);
        long id = get_size();
        id++;
        user.setId(id);
        User save = repoUser.save(user);
        if (save != null)
            throw new ValidationException("\uD83C\uDD74\uD83C\uDD81\uD83C\uDD81\uD83C\uDD7E\uD83C\uDD81 : id already used");
    }

    /**
     * update a user, call the repo function
     * if is not valid throw ValidationException
     * @param id-Long
     * @param firstname-String
     * @param lastname-String
     */
    public void update(Long id, String firstname, String lastname) {
        User user = new User(firstname, lastname);
        user.setId(id);
        User save = repoUser.update(user);
        if (save != null)
            throw new ValidationException("\uD83C\uDD74\uD83C\uDD81\uD83C\uDD81\uD83C\uDD7E\uD83C\uDD81 " +
                    ": id invalid!");
    }

    /**
     * delete a user, call the repo function
     * @param id-Long
     * @return the deleted user
     * otherwise, throw ValidationException
     */
    public Entity delete(Long id) {

        Entity deleted = repoUser.delete(id);
        if (deleted == null)
            throw new ValidationException("\uD83C\uDD74\uD83C\uDD81\uD83C\uDD81\uD83C\uDD7E\uD83C\uDD81 " +
                    ": id invalid!");
        return deleted;
    }

    /**
     * get the maximum id
     * @return the result-Long
     */
    public Long get_size() {
        Long maxim = 0L;
        for (User ur : repoUser.findAll())
            if (ur.getId() > maxim)
                maxim = ur.getId();
        return maxim;
    }

    /**
     * Display friends for a given user
     * @param id integer id of a posible user
     * @return the list of users friends with the one given
     * @throws ValidationException if the id for user given is invalid
     */
    public Iterable<User> getFriends(Long id) throws ValidationException{
        try{
            Set<User> users= new HashSet<>();
            User response = repoUser.findOne(id);
            if(response == null)
                throw new ValidationException("\uD83C\uDD74\uD83C\uDD81\uD83C\uDD81\uD83C\uDD7E\uD83C\uDD81 " +
                        ": id invalid!");
            else
                for (Friendship fr : repoFriends.findAll()){
                    if(Objects.equals(fr.getId().getLeft(), id))
                        users.add(repoUser.findOne(fr.getId().getRight()));
                    if(Objects.equals(fr.getId().getRight(), id))
                        users.add(repoUser.findOne(fr.getId().getLeft()));
                }
            return users;
        }
        catch (ValidationException exception){
            throw new ValidationException(exception);
        }
    }

    /** Function which returns all the users from list
     * @return all the users
     */
    public Iterable<User> printUs() {
        return repoUser.findAll();
    }

    /**Display friends for a given user
     * @param nr integer id of a posible user
     * @return the user with the given id
     * @throws ValidationException if the id for user given is invalid
     */
    public User findOne(Long nr) {
        if(repoUser.findOne(nr) != null)
            return repoUser.findOne(nr);
        else
            throw new ValidationException("\uD83C\uDD74\uD83C\uDD81\uD83C\uDD81\uD83C\uDD7E\uD83C\uDD81" +
                    ": id invalid!");
    }


    /**
     * Function which show friends of an user, with friendship made on a specific mounth
     * @param id - integer
     * @param month - String
     * @return a list of friends
     */
    public Iterable<UserFriendDTO> findFriendshipsByMonth(Long id, String month){

        if(repoUser.findOne(id) == null)
            throw new ValidationException("\uD83C\uDD74\uD83C\uDD81\uD83C\uDD81\uD83C\uDD7E\uD83C\uDD81" +
                    "ID does not exist!");

        return StreamSupport.stream(repoFriends.findAll().spliterator(),false)
                .filter(friendship -> (friendship.getId().getLeft().equals(id) || friendship.getId().getRight().equals(id))
                        && friendship.getDate().getMonth().toString().equals(month)
                        && friendship.getState().equals("Approved"))
                .map(friendship -> {
                    User friend;
                    if (friendship.getId().getLeft().equals(id)){
                        friend = repoUser.findOne(friendship.getId().getRight());
                    }
                    else{
                        friend = repoUser.findOne(friendship.getId().getLeft());
                    }
                    return new UserFriendDTO(friend.getFirstName(),friend.getLastName(),friendship.getDate());
                })
                .collect(Collectors.toList());
    }

    /**
     * Function which show friends of an user
     * @param id - integer, if of a given user
     * @return a list of friends
     */
    public Iterable<UserFriendDTO> getFriendsForUser(Long id){

        if(repoUser.findOne(id) == null)
            throw new ValidationException("\uD83C\uDD74\uD83C\uDD81\uD83C\uDD81\uD83C\uDD7E\uD83C\uDD81" +
                    "ID does not exist!");

        return StreamSupport.stream(repoFriends.findAll().spliterator(),false)
                .filter(friendship -> (friendship.getId().getLeft().equals(id) || friendship.getId().getRight().equals(id))
                        && friendship.getState().equals("Approved"))
                .map(friendship -> {
                    User friend;
                    if(friendship.getId().getLeft().equals(id)){
                        friend = repoUser.findOne(friendship.getId().getRight());
                    }
                    else
                        friend = repoUser.findOne(friendship.getId().getLeft());
                    return new UserFriendDTO(friend.getFirstName(),friend.getLastName(),friendship.getDate());
                })
                .collect(Collectors.toList());

    }

    /**
     * Shows all friendship requests for a given user
     * An user is a recever of a friendship request if he is on the right side of the tuple (id2)
     * @param id -Long
     * @return a list with all the friendship requests
     */
    public Iterable<Friendship> getFriendshipRequestForUser(Long id) {
        if(repoUser.findOne(id) == null)
            throw new ValidationException("\uD83C\uDD74\uD83C\uDD81\uD83C\uDD81\uD83C\uDD7E\uD83C\uDD81" +
                    "ID does not exist!");

        return StreamSupport.stream(repoFriends.findAll().spliterator(),false)
                .filter(friendship -> friendship.getId().getRight().equals(id) && friendship.getState().equals("Pending"))
                .collect(Collectors.toList());
    }

}
