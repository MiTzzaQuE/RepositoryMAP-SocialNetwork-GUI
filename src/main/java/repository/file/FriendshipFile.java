package repository.file;


import domain.Friendship;
import domain.Tuple;
import domain.validation.Validator;

import java.time.LocalDateTime;
import java.util.List;

/**
 * class which extract a Friendship List from a file
 * extends the AbstractFileRepository
 * uses a Tuple for the Friendship relation
 */
public class FriendshipFile extends AbstractFileRepository<Tuple<Long,Long>,Friendship>{

    /**
     * constructor for the class
     * @param fileName - name of the file
     * @param validator - used for corect inputs
     */
    public FriendshipFile(String fileName, Validator<Friendship> validator) {
        super(fileName, validator);
    }

    @Override
    protected Friendship extractEntity(List<String> attributes) {
        long id1=Long.parseLong(attributes.get(0));
        long id2=Long.parseLong(attributes.get(1));
        LocalDateTime date=LocalDateTime.parse(attributes.get(2));
        Friendship friendship=new Friendship();

        Tuple t=new Tuple(id1,id2);
        friendship.setId(t);
        friendship.setDate(date);
        return  friendship;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getId().getLeft()+";"+entity.getId().getRight()+";"+entity.getDate();
    }
}
