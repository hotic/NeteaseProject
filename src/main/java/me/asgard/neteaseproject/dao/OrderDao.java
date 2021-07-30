package me.asgard.neteaseproject.dao;

import com.google.inject.Singleton;
import me.asgard.neteaseproject.domain.Order;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

@Singleton
public class OrderDao extends DaoBase<Order> {
    @Override
    public Order findById(Long id) {
        Session session = openSession();
        Order order = null;
        try {
            Query query = session.createQuery("from Order order where order.id=:id");
            query.setParameter("id", id);
            List list = query.list();
            if (!list.isEmpty())  {
                order = (Order) list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return order;
    }

    public Order findByUUID(String uuid) {
        Session session = openSession();
        Order order = null;
        try {
            Query query = session.createQuery("from Order order where order.uuid=:uuid");
            query.setParameter("uuid", uuid);
            List list = query.list();
            if (!list.isEmpty())  {
                order = (Order) list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return order;
    }

    public Order findByOrderId(String orderId) {
        Session session = openSession();
        Order order = null;
        try {
            Query query = session.createQuery("from Order order where order.orderId=:orderId");
            query.setParameter("orderId", orderId);
            List list = query.list();
            if (!list.isEmpty())  {
                order = (Order) list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return order;
    }
}
