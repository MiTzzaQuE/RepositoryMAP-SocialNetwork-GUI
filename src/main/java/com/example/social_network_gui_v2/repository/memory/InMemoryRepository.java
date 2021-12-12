package com.example.social_network_gui_v2.repository.memory;

import com.example.social_network_gui_v2.domain.Entity;
import com.example.social_network_gui_v2.domain.validation.Validator;
import com.example.social_network_gui_v2.repository.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * InMemoryRepository where are store the objects
 * implements the interface of Repository
 * @param <ID> - type E must have an attribute of type ID
 * @param <E> -  type of entities saved in com.example.social_network_gui_v2.repository
 */
public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    private Validator<E> validator;
    Map<ID,E> entities;

    /**
     * constructor for the com.example.social_network_gui_v2.repository
     * @param validator used for validation
     */
    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    @Override
    public E findOne(ID id){
        if (id==null)
            throw new IllegalArgumentException("\uD83C\uDD74\uD83C\uDD81\uD83C\uDD81\uD83C\uDD7E\uD83C\uDD81 : id must be not null");
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("\uD83C\uDD74\uD83C\uDD81\uD83C\uDD81\uD83C\uDD7E\uD83C\uDD81 : entity must be not null");
        validator.validate(entity);
        if(entities.get(entity.getId()) != null) {
            return entity;
        }
        else entities.put(entity.getId(),entity);
        return null;
    }

    @Override
    public E delete(ID id) {
        if(entities.get(id)==null)
            return null;
        E entity=entities.get(id);
        entities.remove(id);
        return entity;
    }

    @Override
    public E update(E entity) {

        if(entity == null)
            throw new IllegalArgumentException("\uD83C\uDD74\uD83C\uDD81\uD83C\uDD81\uD83C\uDD7E\uD83C\uDD81 : entity must be not null!");
        validator.validate(entity);
        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            return null;
        }
        return entity;
    }
}
