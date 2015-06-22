package commons.rest;

import database.Connection;

import javax.inject.Inject;
import java.util.List;

public interface Repository<E> {

    public E getById(Class<E> type, Long id);
    public List<E> getAll(Class<E> type);

    public void create(E entity);
    public void update(E entity);
    public void createOrUpdate(E entity);

    public void delete(E entity);

}
