package domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void getterSetterTest() {
        User user = new User("Pop","Mihai");
        assertEquals(user.getFirstName(),"Pop");
        assertEquals(user.getLastName(),"Mihai");

        user.setFirstName("First");
        user.setLastName("Last");
        assertEquals(user.getFirstName(),"First");
        assertEquals(user.getLastName(),"Last");

        User friend = new User("Baciu","Cristi");
//        user.addFriend(friend);
//        assertEquals(user.getFriends().toString(),"[User{firstName='Baciu', lastName='Cristi', friends=}]");
//
//        assertEquals(user.toString(),"User{firstName='First', lastName='Last', friends=|Baciu Cristi}");
//
//        user.deleteFriend(friend);
//        assertEquals(user.getFriends().toString(),"[]");
//
//        List<User> friends = new ArrayList<>();
//        friends.add(friend);
//        user.setFriends(friends);
//        assertEquals(user.getFriends().toString(),"[User{firstName='Baciu', lastName='Cristi', friends=}]");

    }
}