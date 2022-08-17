package com.company.springmvcweb.data;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.ArrayList;

public class PartRepository {
        private static SessionFactory factory;

        public PartRepository() {
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

        public Iterable<Part> getAllParts(){
            var session = factory.openSession();

            try {
                return session.createQuery("FROM Part").list();
            } catch (HibernateException exception) {
                System.err.println(exception);
            } finally {
                session.close();
            }
            return new ArrayList<>();
        }
}
