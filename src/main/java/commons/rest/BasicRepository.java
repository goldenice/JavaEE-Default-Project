package commons.rest;

import commons.entity.SoftDeletable;
import database.Connection;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class BasicRepository<E> implements Repository<E> {

    @Inject
    private Connection connection;
    private Session session;

    protected Session getSession() {
        if (session == null) {
            return session = connection.getSession();
        } else {
            return session;
        }
    }

    @Override
    public E getById(Class<E> type, Long id) {
        Transaction t = getSession().beginTransaction();
        E result = (E) getSession().get(type, id);
        t.commit();
        return result;
    }

    @Override
    public List<E> getAll(Class<E> type) {
        Transaction t = getSession().beginTransaction();
        List<E> result = (List<E>) getSession().createCriteria(type).list();
        t.commit();
        return result;
    }

    @Override
    public void create(E entity) {
        Transaction t = getSession().beginTransaction();
        getSession().persist(entity);
        t.commit();
    }

    @Override
    public void update(E entity) {
        Transaction t = getSession().beginTransaction();
        getSession().update(entity);
        t.commit();
    }

    @Override
    public void createOrUpdate(E entity) {
        Transaction t = getSession().beginTransaction();
        getSession().saveOrUpdate(entity);
        t.commit();
    }

    @Override
    public void delete(E entity) {
        if (entity instanceof SoftDeletable) {
            ((SoftDeletable) entity).setDeletedOn(new Date());
            this.update(entity);
        } else {
            Transaction t = getSession().beginTransaction();
            getSession().delete(entity);
            t.commit();
        }
    }
}
