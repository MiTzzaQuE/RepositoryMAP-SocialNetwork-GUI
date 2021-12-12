package repository.file;

import domain.User;
import domain.validation.Validator;

import java.util.List;

/**
 * class which extract a User List from a file
 * extends the AbstractFileRepository
 * uses a pair of id and a User
 */
public class UserFile extends AbstractFileRepository<Long, User> {

    /**
     constructor for the class
     * @param fileName - name of the file
     * @param validator - used for corect inputs
     */
    public UserFile(String fileName, Validator<User> validator) {
        super(fileName, validator);
    }

    @Override
    protected User extractEntity(List<String> attributes) {
        User user = new User(attributes.get(1), attributes.get(2));
        user.setId(Long.parseLong(attributes.get(0)));
        return user;
    }

    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName();

    }
}
