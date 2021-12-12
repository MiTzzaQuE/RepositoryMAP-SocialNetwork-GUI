package repository.file;

import domain.Entity;
import domain.validation.Validator;
import repository.memory.InMemoryRepository;

import java.io.*;
import java.util.Arrays;
import java.util.List;

///Aceasta clasa implementeaza sablonul de proiectare Template Method; puteti inlucui solutia propusa cu un Factori (vezi mai jos)

/**
 * class AbstractFileRepository extends InMemoryRepository
 * @param <ID> type E must have an attribute of type ID
 * @param <E> type of entities saved in repository
 */
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    String fileName;

    /**
     * Constructor for the file repository
     * @param fileName name of the file
     * @param validator the validator which is used later
     */
    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    /**
     * load the data from a file
     * @catch exceptions if its needed
     */
    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> attributes = Arrays.asList(line.split(";"));
                E entity = extractEntity(attributes);
                super.save(entity);
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * extract entity  - template method design pattern
     * creates an entity of type E having a specified list of @code attributes
     *
     * @param attributes string list with attributes of an entity
     * @return an entity of type E
     */
    protected abstract E extractEntity(List<String> attributes);
    ///Observatie-Sugestie: in locul metodei template extractEntity, puteti avea un factory pr crearea instantelor entity

    /**
     * create entity - template method design pattern
     * creates an entity of type E as string
     * @param entity-generic type
     * @return the string form
     */
    protected abstract String createEntityAsString(E entity);

    /**
     * write the data to a file
     * @param entity- generic type
     * catch exceptions if its needed
     */
    protected void writeToFile(E entity) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(createEntityAsString(entity));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * write all the data to a file
     * catch exceptions if its needed
     */
    protected void writeToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, false))) {
            super.findAll().forEach(entity -> {
                try {
                    bw.write(createEntityAsString(entity));
                    bw.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public E save(E entity) {
        if (super.save(entity) == null) {
            writeToFile(entity);
            return null;
        } else
            return entity;
    }

    @Override
    public E update(E entity) {
        E updated_entity = super.update(entity);
        if (updated_entity == null) {
            writeToFile();
            return null;
        }
        return entity;
    }

    @Override
    public E delete(ID id) {
        E deleted_entity = super.delete(id);
        if (deleted_entity == null) {

            return null;
        }
        writeToFile();
        return deleted_entity;
    }
}

