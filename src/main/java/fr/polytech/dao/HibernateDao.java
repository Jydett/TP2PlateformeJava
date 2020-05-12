package fr.polytech.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Collection;

public abstract class HibernateDao<T> {

    protected final String className;
    protected final Session hibernateSession;
    protected final Class<T> clazz;
    protected final String tableName;

    public HibernateDao(Session hibernateSession, Class<T> clazz) {
        this.hibernateSession = hibernateSession;
        this.clazz = clazz;
        Table tableAnnotation = clazz.getAnnotation(Table.class);
        if (tableAnnotation == null || tableAnnotation.name().isEmpty()) {
            tableName = clazz.getSimpleName().toLowerCase();
        } else {
            tableName = tableAnnotation.name();
        }
        className = clazz.getSimpleName();
    }

    public boolean isEmpty() {
        System.out.println("[DEBUG] isEmpty " + tableName);
        return ((BigInteger) hibernateSession.createSQLQuery("SELECT EXISTS (SELECT NULL FROM " + tableName + ")").uniqueResult()).intValue() == 0;
    }

    public void save(T toSave) {
        Transaction transaction = hibernateSession.beginTransaction();
        hibernateSession.save(toSave);
        transaction.commit();
    }

    public void remove(T toRemove) {
        Transaction transaction = hibernateSession.beginTransaction();
        hibernateSession.delete(toRemove);
        transaction.commit();
    }

    public Collection<T> getAll() {
        return hibernateSession.createQuery("FROM " + className, clazz).list();
    }
}
