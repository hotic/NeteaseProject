package me.asgard.neteaseproject.dao;

import com.google.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public abstract class DaoBase<T> {

    @Inject
    private SessionFactory sessionFactory;

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Session openSession() {
        return sessionFactory.openSession();
    }

    public Long add(T t) {
        Long id = 0L;
        Session session = openSession();
        Transaction transaction = session.beginTransaction();
        try {
            id = (Long) session.save(t);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return id;
    }

    public void update(T t) {
        Session session = openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(t);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void del(T t) {
        Session session = openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(t);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public abstract T findById (Long id);

}