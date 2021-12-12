package domain.validation;

import domain.Friendship;
import domain.User;

import java.util.Objects;

/**
 * Friendship Validator class
 * implements interface of Validator
 */
public class FriendshipValidator implements  Validator<Friendship>{

    @Override
    public void validate(Friendship entity) throws ValidationException {
        if(Objects.equals(entity.getId().getLeft(), entity.getId().getRight()))
            throw new ValidationException("\uD83C\uDD74\uD83C\uDD81\uD83C\uDD81\uD83C\uDD7E\uD83C\uDD81 : the ids must be different");
    }
}
