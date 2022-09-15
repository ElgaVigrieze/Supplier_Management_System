package com.company.springmvcweb.data;
import com.company.springmvcweb.dto.OrderSaveDto;
import com.company.springmvcweb.dto.SupplierSaveDto;
import lombok.NonNull;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepository {
    private static SessionFactory factory;

    public SupplierRepository() {
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

    public List<Supplier> getSuppliers() {
        var session = factory.openSession();

        try {
            return session.createQuery("FROM Supplier order by name asc").list();
        } catch (HibernateException exception) {
            System.err.println(exception);
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

        public Object getSupplier(int id) {
        var session = factory.openSession();

        try {
            var sql = "FROM Supplier where id = :id";
            var query = session.createQuery(sql);
            query.setParameter("id", id);

            var items = query.list();

            return items.size() > 0 ? items.get(0) : null;
        } catch (HibernateException exception) {
            System.err.println(exception);
        } finally {
            session.close();
        }
        return null;
    }

    public Integer add(@NonNull Supplier supplier){
        var session = factory.openSession();
        Integer supplierId = null;
        try{
            supplierId = (Integer)session.save(supplier);
        }catch (HibernateException ex){
            System.err.println(ex);
        }finally{
            session.close();
        }
        return supplierId;
    }

    public Integer add(SupplierSaveDto dto) {
        var supplier = new Supplier(0, dto.getName(), dto.getCategory(), dto.getVATNo(), dto.getEMail());
        return add(supplier);
    }

    public String getSupplierName(int id) {
        var session = factory.openSession();

        try {

            var sql = "SELECT name FROM Supplier where id = :id";
            var query = session.createQuery(sql);
            query.setParameter("id", id);

            var items = query.list();

            var item = items.get(0);

            return (String) item;
        } catch (HibernateException exception) {
            System.err.println(exception);
        } finally {
            session.close();
        }
        return null;
    }

    public void update(@NonNull Object item) {
        var session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.update(item);
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

    public void update(SupplierSaveDto dto) {
        Supplier updatedSupplier = (Supplier)getSupplier(dto.getId());
        updatedSupplier.setName(dto.getName());
        updatedSupplier.setCategory(dto.getCategory());
        updatedSupplier.setEMail(dto.getEMail());
        update(updatedSupplier);
    }

}
