package com.company.springmvcweb.data;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class DeliveryPerformance {
    private Double supplierPerformanceGeneral;
    private Double supplierPerformancePerSupplier;
    private Double supplierPerformancePerMonth;
    private Double supplierPerformancePerMonthPerSupplier;

    private static SessionFactory factory;

    public DeliveryPerformance() {
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
    public Double getSupplierPerformancePerSupplier(int supplierId){ //performance general total per supplier
        if(getCountOfOrdersPerSupplier(supplierId)==0){
            return null;
        }
        return (double)getCountOfOrdersDeliveredOnTimePerSupplier(supplierId) / (double) getCountOfOrdersPerSupplier(supplierId);
    }

    public Double getSupplierPerformanceGeneral(){ //performance general total
        if(getCountOfOrdersPerSupplier()==0){
            return null;
        }
        return (double)getCountOfOrdersDeliveredOnTime() / (double) getCountOfOrders();
    }

    public Double getSupplierPerformancePerMonthPerSupplier(int supplierId, int month){ //performance per month per supplier
        if(getCountOfOrdersPerSupplierPerMonth(supplierId, month)==0){
            return null;
        }
        return (double)getCountOfOrdersDeliveredOnTimePerSupplier(supplierId, month)/(double) getCountOfOrdersPerSupplierPerMonth(supplierId,month);
    }

    public Double getSupplierPerformancePerMonth(int month){  //performance per month
        if(getCountOfOrdersPerMonth(month)==0){
            return null;
        }
        return (double)getCountOfOrdersDeliveredOnTimePerMonth(month)/(double) getCountOfOrdersPerMonth(month);
    }

    public long getCountOfOrdersPerSupplierPerMonth(int supplierId, int month) {
        try (var session = factory.openSession()) {
            var sql = "Select count(*) FROM Order where supplier_id = :supplier_id and MONTH(delivery_date_order) = :month  and delivery_date_order <= CURDATE()";
            var query = session.createQuery(sql);
            query.setParameter("supplier_id", supplierId);
            query.setParameter("month", month);
            var count = query.list().get(0);
            return (long) count;
        } catch (HibernateException exception) {
            System.err.println(exception);
        }
        return 0;
    }

    public long getCountOfOrdersPerSupplier(int supplierId) {
        try (var session = factory.openSession()) {
            var sql = "Select count(*) FROM Order where supplier_id = :supplier_id and delivery_date_order <= CURDATE()";
            var query = session.createQuery(sql);
            query.setParameter("supplier_id", supplierId);
            var count = query.list().get(0);
            return (long) count;
        } catch (HibernateException exception) {
            System.err.println(exception);
        }
        return 0;
    }

    public long getCountOfOrdersPerSupplier() {
        try (var session = factory.openSession()) {
            var sql = "Select count(*) FROM Order ";
            var query = session.createQuery(sql);
            var count = query.list().get(0);
            return (long) count;
        } catch (HibernateException exception) {
            System.err.println(exception);
        }
        return 0;
    }

    public long getCountOfOrdersPerMonth(int month) {
        try (var session = factory.openSession()) {
            var sql = "Select count(*) FROM Order where MONTH(delivery_date_order) = :month";
            var query = session.createQuery(sql);
            query.setParameter("month", month);
            var count = query.list().get(0);
            return (long) count;
        } catch (HibernateException exception) {
            System.err.println(exception);
        }
        return 0;
    }

    public long getCountOfOrders() {
        try (var session = factory.openSession()) {
            var sql = "Select count(*) FROM Order ";
            var query = session.createQuery(sql);
            var count = query.list().get(0);
            return (long) count;
        } catch (HibernateException exception) {
            System.err.println(exception);
        }
        return 0;
    }

    public long  getCountOfOrdersDeliveredOnTimePerSupplier(int supplierId) {
        try (var session = factory.openSession()) {
            var sql = "Select count(*) FROM Order where supplier_id = :supplier_id and delivery_date_real <= delivery_date_order and quantity_real >= quantity_order";
            var query = session.createQuery(sql);
            query.setParameter("supplier_id", supplierId);
            var count = query.list().get(0);
            return (long) count;
        } catch (HibernateException exception) {
            System.err.println(exception);
        }
        return 0;
    }

    public long  getCountOfOrdersDeliveredOnTime() {
        try (var session = factory.openSession()) {
            var sql = "Select count(*) FROM Order where  delivery_date_real <= delivery_date_order and quantity_real >= quantity_order";
            var query = session.createQuery(sql);
            var count = query.list().get(0);
            return (long) count;
        } catch (HibernateException exception) {
            System.err.println(exception);
        }
        return 0;
    }

    public long  getCountOfOrdersDeliveredOnTime(int month) {
        try (var session = factory.openSession()) {
            var sql = "Select count(*) FROM Order where MONTH(delivery_date_order) = :month and delivery_date_real <= delivery_date_order and quantity_real >= quantity_order";
            var query = session.createQuery(sql);
            query.setParameter("month", month);
            var count = query.list().get(0);

            return (long) count;
        } catch (HibernateException exception) {
            System.err.println(exception);
        }
        return 0;
    }

    public long getCountOfOrdersDeliveredOnTimePerSupplier(int supplierId, int month) {
        var session = factory.openSession();

        try {
            var sql = "Select count(*) FROM Order where supplier_id = :supplier_id and MONTH(delivery_date_order) = :month  and delivery_date_real <= delivery_date_order and quantity_real >= quantity_order";
            var query = session.createQuery(sql);
            query.setParameter("supplier_id", supplierId);
            query.setParameter("month", month);

            var count = query.list().get(0);

            return (long ) count;
        } catch (HibernateException exception) {
            System.err.println(exception);
        } finally {
            session.close();
        }
        return 0;
    }

    public long getCountOfOrdersDeliveredOnTimePerMonth(int month) {
        var session = factory.openSession();

        try {
            var sql = "Select count(*) FROM Order where MONTH(delivery_date_order) = :month  and delivery_date_real <= delivery_date_order and quantity_real >= quantity_order";
            var query = session.createQuery(sql);
            query.setParameter("month", month);

            var count = query.list().get(0);

            return (long ) count;
        } catch (HibernateException exception) {
            System.err.println(exception);
        } finally {
            session.close();
        }
        return 0;
    }

    public long getCountOfOrdersDeliveredLatePerMonth(int month) {
        var session = factory.openSession();

        try {
            var sql = "Select count(*) FROM Order where (delivery_date_real != null and MONTH(delivery_date_order) = :month and delivery_date_real > delivery_date_order)  or (delivery_date_real != null and MONTH(delivery_date_order) = :month and quantity_real < quantity_order)";
            var query = session.createQuery(sql);
            query.setParameter("month", month);

            var count = query.list().get(0);

            return (long ) count;
        } catch (HibernateException exception) {
            System.err.println(exception);
        } finally {
            session.close();
        }
        return 0;
    }




}
