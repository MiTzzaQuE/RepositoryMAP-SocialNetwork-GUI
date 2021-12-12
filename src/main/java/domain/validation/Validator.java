package domain.validation;

/**
 * Validator interface
 * @param <T> generic type T
 */
public interface Validator<T> {
    /**
     * verify all the cases that are not allowed for the validation of the entity given
     * @param entity-generic Type
     //* @throws ValidationException- if is not valid
     */
    void validate(T entity) throws ValidationException;
}
