package com.example.social_network_gui_v2.domain.validation;

import com.example.social_network_gui_v2.domain.Friendship;

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
