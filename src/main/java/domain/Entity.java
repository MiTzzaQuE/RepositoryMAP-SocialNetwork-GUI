package domain;

import java.io.Serializable;

/**
 * the class Entity used for extending other classes
 * @param <ID> generic implementation for keeping things organised
 */
public class Entity<ID> implements Serializable {

    private static final long serialVersionUID = 7331115341259248461L;
    /**
     * attribute of type ID;
     */
    private ID id;

    /**
     * getter funtion for an entity
     * @return the id of the entity
     */
    public ID getId() {
        return id;
    }

    /**
     * setter function for an entity
     * sets an id for an entity
     * @param id the new id
     */
    public void setId(ID id) {
        this.id = id;
    }
}