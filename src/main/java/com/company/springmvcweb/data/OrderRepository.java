package com.company.springmvcweb.data;

import com.company.springmvcweb.dto.OrderSaveDto;
import com.company.springmvcweb.dto.OrderSearchDto;

import lombok.NonNull;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.Query;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class OrderRepository {
    private static SessionFactory factory;
    private SupplierRepository repo1 = new SupplierRepository();

    public OrderRepository() {
        try {
            factory = new Configuration().
                    configure().
                    addAnnotatedClass(Order.class).
                    addAnnotatedClass(Supplier.class).
                    addAnnotatedClass(Part.class).
                    buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public Iterable<Order> getOrders() {
        var session = factory.openSession();

        try {
            return session.createQuery("FROM Order ORDER BY id DESC").list();
        } catch (HibernateException exception) {
            System.err.println(exception);
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public Object getOrder(int id) {

        try (var session = factory.openSession()) {

            var sql = "FROM Order where id = :id";
            var query = session.createQuery(sql);
            query.setParameter("id", id);

            var items = query.list();

            return items.size() > 0 ? items.get(0) : null;
        } catch (HibernateException exception) {
            System.err.println(exception);
        }
        return null;
    }



    public Iterable<Order> getOrdersPerSupplierAndOrPartNo(OrderSearchDto searchDto) {

        try (var session = factory.openSession()) {
            var sql = "FROM Order";

            if (!searchDto.getPartNo().isBlank() || !String.valueOf(searchDto.getSupplierId()).isBlank() || searchDto.getStatus() != null) {
                sql += " where ";
            }

            if (!searchDto.getPartNo().isBlank()) {
                sql += " part_no = :search_part_no";

            }

            if (!String.valueOf(searchDto.getSupplierId()).isBlank()) {
                if (!searchDto.getPartNo().isBlank()) {

                    sql += " and ";
                }
                sql += " supplier_id = :search_supplier_id";
            }

            if (!String.valueOf(searchDto.getSupplierId()).isBlank() || !searchDto.getPartNo().isBlank()) {
                if (searchDto.getStatus() != null) {
                    sql += " and ";
                }
            }

            if (searchDto.getStatus() == Status.OPEN) {
                sql += "delivery_date_real = null ";
            }
            if (searchDto.getStatus() == Status.DELIVERED) {
                sql += "delivery_date_real != null ";
            }
            if (searchDto.getStatus() == Status.LATE) {
                sql += "delivery_date_order <= CURDATE() and delivery_date_real = null ";
            }
            if (searchDto.getStatus()==null) {
                sql +="";
            }

            sql+= " ORDER BY id DESC";

            var query = session.createQuery(sql);
            if (!searchDto.getPartNo().isBlank()) {
                query.setParameter("search_part_no", searchDto.getPartNo());
            }

            if (!String.valueOf(searchDto.getSupplierId()).isBlank()) {
                query.setParameter("search_supplier_id", searchDto.getSupplierId());
            }

            return query.list();

        } catch (HibernateException exception) {
            System.err.println(exception);
        }
            return new ArrayList();
        }

        public boolean orderDelivered(int id){
        var order = (Order)getOrder(id);
            if(order.getDeliveryDateReal() == null){
                return false;
            }
            return true;
        }

    public Iterable<Order> getOrdersPerSupplier(int supplierId) {
        var session = factory.openSession();
        try {
            var sql = "FROM Order where supplier_id = :supplier_id";
            var query = session.createQuery(sql);
            query.setParameter("supplier_id", supplierId);
            return query.list();
        } catch (HibernateException exception) {
            System.err.println(exception);
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }
//
    public Integer add(@NonNull Order order){
        var session = factory.openSession();
        Integer orderId = null;

        try{
            orderId = (Integer)session.save(order);
        }catch (HibernateException ex){
            System.err.println(ex);
        }finally{
            session.close();
        }
        return orderId;
    }


    public Integer add(OrderSaveDto dto) {
        var order = new Order(0, LocalDate.now(), dto.getSupplier(), dto.getPartNo(), dto.getQuantityOrder(), LocalDate.parse(dto.getDeliveryDateOrder()));
        return add(order);
    }

    public void update(@NonNull Order order) {
        var session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.update(order);
            tx.commit();
        } catch (HibernateException exception) {
            if(tx != null) {
                tx.rollback();
            }
            System.err.println(exception);
        } finally {
            session.close();
        }
    }

    public void update(OrderSaveDto dto) {
        Order updatedOrder = (Order)getOrder(dto.getId());
        updatedOrder.setQuantityOrder(dto.getQuantityOrder());
        updatedOrder.setPartNo(dto.getPartNo());
        updatedOrder.setDeliveryDateOrder(LocalDate.parse(dto.getDeliveryDateOrder()));
        update(updatedOrder);
    }

    public void delete(int id){
    var session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            var sql = "Delete FROM Order where id = :id";
            var query = session.createQuery(sql);
            query.setParameter("id", id);
            query.executeUpdate();
            tx.commit();
        }catch (HibernateException ex){
            if(tx != null){
                tx.rollback();
            }
            System.err.println(ex);
        }finally{
            session.close();
        }
}

    public long getCountOfOrdersPerSupplier(int supplierId) {

        try (var session = factory.openSession()) {
            var sql = "Select count(*) FROM Order where supplier_id = :supplier_id";
            var query = session.createQuery(sql);
            query.setParameter("supplier_id", supplierId);
            var count = query.list().get(0);
            return (long) count;
        } catch (HibernateException exception) {
            System.err.println(exception);
        }
        return 0;
    }

    public long getCountOfOpenOrdersPerSupplier(int supplierId) {

        try (var session = factory.openSession()) {
            var sql = "Select count(*) FROM Order where supplier_id = :supplier_id and delivery_date_real = null";
            var query = session.createQuery(sql);
            query.setParameter("supplier_id", supplierId);
            var count = query.list().get(0);
            return (long) count;
        } catch (HibernateException exception) {
            System.err.println(exception);
        }
        return 0;
    }

    public Iterable<Order> getOpenOrdersPerSupplier(int supplierId) {

        try (var session = factory.openSession()) {
            var sql = "FROM Order where supplier_id = :supplier_id and delivery_date_real = null";
            var query = session.createQuery(sql);
            query.setParameter("supplier_id", supplierId);

            return query.list();
        } catch (HibernateException exception) {
            System.err.println(exception);
        }
        return new ArrayList<>();
    }

    public long getCountOfOpenLateOrdersPerSupplier(int supplierId) {

        try (var session = factory.openSession()) {
            var sql = "Select count(*) FROM Order where supplier_id = :supplier_id and delivery_date_order <= CURDATE() and delivery_date_real = null";
            var query = session.createQuery(sql);
            query.setParameter("supplier_id", supplierId);
            var count = query.list().get(0);
            return (long) count;
        } catch (HibernateException exception) {
            System.err.println(exception);
        }
        return 0;
    }

    public Iterable<Order> getDeliveredOrdersPerSupplier(int supplierId) {

        try (var session = factory.openSession()) {
            var sql = "FROM Order where supplier_id = :supplier_id and delivery_date_real != null";
            var query = session.createQuery(sql);
            query.setParameter("supplier_id", supplierId);
            return query.list();
        } catch (HibernateException exception) {
            System.err.println(exception);
        }
        return new ArrayList<>();
    }

    public Iterable<Order> getOrdersToBeDeliveredPerMonth(int supplierId, int month) {
        try (var session = factory.openSession()) {
            var sql = "FROM Order where supplier_id = :supplier_id and MONTH(delivery_date_order) = :month";
            var query = session.createQuery(sql);
            query.setParameter("supplier_id", supplierId);
            query.setParameter("month", month);
            return query.list();
        } catch (HibernateException exception) {
            System.err.println(exception);
        }
        return new ArrayList<>();
    }


    public Iterable<Order> getOrdersOnTimePerMonth(int supplierId, int month) {
        try (var session = factory.openSession()) {
            var sql = "FROM Order where supplier_id=: supplier_id and MONTH(delivery_date_order) = :month and delivery_date_real <= delivery_date_order and quantity_real >= quantity_order";
            var query = session.createQuery(sql);
            query.setParameter("supplier_id", supplierId);
            query.setParameter("month", month);
            return query.list();
        } catch (HibernateException exception) {
            System.err.println(exception);
        }
        return new ArrayList<>();
    }

    public Iterable<Order> getOrdersToBeDeliveredPerMonth(int month) {

        try (var session = factory.openSession()) {
            var sql = "FROM Order where MONTH(delivery_date_order) = :month";
            var query = session.createQuery(sql);

            query.setParameter("month", month);
            return query.list();
        } catch (HibernateException exception) {
            System.err.println(exception);
        }
        return new ArrayList<>();
    }

    public Iterable<Order> getOrdersDeliveredLatePerMonth1(int month) {
        var session = factory.openSession();

        try {
          //  var sql = "FROM Order where MONTH(delivery_date_order) = :month and delivery_date_real > delivery_date_order or quantity_real < quantity_order";
            var sql="FROM Order where delivery_date_real  is not null  and MONTH(delivery_date_order) = :month and delivery_date_real > delivery_date_order ";
            var query = session.createQuery(sql);
            query.setParameter("month", month);
            return query.list();
        } catch (HibernateException exception) {
            System.err.println(exception);
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public Iterable<Order> getOrdersDeliveredLatePerMonth1(int month, int supplierId) {
        var session = factory.openSession();

        try {
            //  var sql = "FROM Order where MONTH(delivery_date_order) = :month and delivery_date_real > delivery_date_order or quantity_real < quantity_order";
            var sql="FROM Order where supplier_id=:supplier_id and delivery_date_real  is not null  and MONTH(delivery_date_order) = :month and delivery_date_real > delivery_date_order ";
            var query = session.createQuery(sql);
            query.setParameter("month", month);
            query.setParameter("supplier_id", supplierId);
            return query.list();
        } catch (HibernateException exception) {
            System.err.println(exception);
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public Iterable<Order> getOrdersDeliveredLatePerMonth2(int month) {
        var session = factory.openSession();

        try {
            //  var sql = "FROM Order where MONTH(delivery_date_order) = :month and delivery_date_real > delivery_date_order or quantity_real < quantity_order";
            var sql="FROM Order where delivery_date_real  is not null  and MONTH(delivery_date_order) = :month and quantity_real < quantity_order";
            var query = session.createQuery(sql);
            query.setParameter("month", month);
            return query.list();
        } catch (HibernateException exception) {
            System.err.println(exception);
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public Iterable<Order> getOrdersDeliveredLatePerMonth2(int month, int supplierId) {
        var session = factory.openSession();

        try {
            //  var sql = "FROM Order where MONTH(delivery_date_order) = :month and delivery_date_real > delivery_date_order or quantity_real < quantity_order";
            var sql="FROM Order where supplier_id= :supplier_id and delivery_date_real  is not null  and MONTH(delivery_date_order) = :month and quantity_real < quantity_order";
            var query = session.createQuery(sql);
            query.setParameter("month", month);
            query.setParameter("supplier_id", supplierId);
            return query.list();
        } catch (HibernateException exception) {
            System.err.println(exception);
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public Iterable<Order> getOrdersDeliveredLatePerMonth(int month) {
        var late1 = (List<Order>)getOrdersDeliveredLatePerMonth1(month);
        var late2 = (List<Order>)getOrdersDeliveredLatePerMonth2(month);

        Set<Order> set = new LinkedHashSet<>(late1);
        set.addAll(late2);

        Iterable<Order> late = new ArrayList<>(set);
        return late;
    }

    public Iterable<Order> getOrdersDeliveredLatePerMonth(int month, int supplierId) {
        var late1 = (List<Order>)getOrdersDeliveredLatePerMonth1(month, supplierId);
        var late2 = (List<Order>)getOrdersDeliveredLatePerMonth2(month, supplierId);

        Set<Order> set = new LinkedHashSet<>(late1);
        set.addAll(late2);

        Iterable<Order> late = new ArrayList<>(set);
        return late;
    }

    public Iterable<Order> getOrdersDeliveredOnTime(int month) {
        try (var session = factory.openSession()) {
            var sql = "FROM Order where MONTH(delivery_date_order) = :month and delivery_date_real <= delivery_date_order and quantity_real >= quantity_order";
            var query = session.createQuery(sql);
            query.setParameter("month", month);
            return query.list();
        } catch (HibernateException exception) {
            System.err.println(exception);
        }
        return new ArrayList<>();
    }

    public Iterable<Order> getOpenLateOrdersPerSupplier(int supplierId) {

        try (var session = factory.openSession()) {
            var sql = "FROM Order where supplier_id = :supplier_id and delivery_date_order <= CURDATE() and delivery_date_real = null";
            var query = session.createQuery(sql);
            query.setParameter("supplier_id", supplierId);
            return query.list();
        } catch (HibernateException exception) {
            System.err.println(exception);
        }
        return new ArrayList<>();
    }

    public long getCountOfOrdersDeliveredPerSupplier(int supplierId) {

        try (var session = factory.openSession()) {
            Order order = new Order();
            var sql = "Select count(*) FROM Order where supplier_id = :supplier_id and delivery_date_real != null";
            var query = session.createQuery(sql);
            query.setParameter("supplier_id", supplierId);

            var count = query.list().get(0);

            return (long) count;
        } catch (HibernateException exception) {
            System.err.println(exception);
        }
        return 0;
    }
}
