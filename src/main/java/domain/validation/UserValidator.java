package domain.validation;

import domain.User;

/**
 * User validator where is verified the inputs of an potential user
 */
public class UserValidator implements  Validator<User>{

    @Override
    public void validate(User entity) throws ValidationException {
        String first=entity.getFirstName();
        String last= entity.getLastName();
        Long id= entity.getId();
        if(!first.matches("^.[a-z]{0,24}$"))
            throw new ValidationException("\uD83C\uDD74\uD83C\uDD81\uD83C\uDD81\uD83C\uDD7E\uD83C\uDD81" +
                    " : the first name must contain only small letters[25 max], except the first one ");
        if(!first.matches("^[A-Z].*"))
            throw new ValidationException("\uD83C\uDD74\uD83C\uDD81\uD83C\uDD81\uD83C\uDD7E\uD83C\uDD81" +
                    " : the first name must start with a big letter");
        if(!last.matches("^.[a-z]{0,24}$"))
            throw new ValidationException("\uD83C\uDD74\uD83C\uDD81\uD83C\uDD81\uD83C\uDD7E\uD83C\uDD81" +
                    " : the last name must contain only small letters[25 max], except the first one");
        if(!last.matches("^[A-Z].*"))
            throw new ValidationException("\uD83C\uDD74\uD83C\uDD81\uD83C\uDD81\uD83C\uDD7E\uD83C\uDD81" +
                    " : the last name must start with a big letter");
    }
}
