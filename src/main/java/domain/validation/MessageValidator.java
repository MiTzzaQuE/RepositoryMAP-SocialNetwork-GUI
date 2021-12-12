package domain.validation;

import domain.Message;

public class MessageValidator implements Validator<Message> {

    @Override
    public void validate(Message entity) throws ValidationException {

        if( entity.getTo().size() == 0 )
            throw new ValidationException("\uD83C\uDD74\uD83C\uDD81\uD83C\uDD81\uD83C\uDD7E\uD83C\uDD81" +
                    " You cannot send a message with no destination!");
        if( entity.getTo().contains(entity.getFrom()))
            throw new ValidationException("\uD83C\uDD74\uD83C\uDD81\uD83C\uDD81\uD83C\uDD7E\uD83C\uDD81" +
                    " You cannot send a message to yourself!");
    }
}
