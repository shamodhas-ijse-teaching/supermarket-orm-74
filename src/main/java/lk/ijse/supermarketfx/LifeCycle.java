package lk.ijse.supermarketfx;

import lk.ijse.supermarketfx.config.FactoryConfiguration;
import lk.ijse.supermarketfx.entity.Customer;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * --------------------------------------------
 * Author: Shamodha Sahan
 * GitHub: https://github.com/shamodhas
 * Website: https://shamodha.com
 * --------------------------------------------
 * Created: 8/26/2025 1:16 PM
 * Project: supermarket-orm-layered-architecture
 * --------------------------------------------
 **/

public class LifeCycle {
    public static void main(String[] args) {
        Session session1 = FactoryConfiguration.getInstance().getSession();

        // Transient state
        Customer customer = new Customer();
        customer.setName("Jhon Doe");
        // no session
        // no db
        // hibernate no track changes

        Transaction transaction = session1.beginTransaction();

        // Persistent state
        session1.persist(customer);
        // have session
        // object is now managed by hibernate
        // no db
        // changes effect to db
        transaction.commit();
        // db have

        session1.close();
        // Detached state
        // customer
        // commit - db have
        // no commit - no db
        // changes no effect to db
        customer.setName("Hello");

        Session session2 = FactoryConfiguration.getInstance().getSession();

        // Persistent state
        session2.merge(customer);

        Transaction transaction1 = session2.beginTransaction();

        // Removed state
        session2.remove(customer);
        // rady to remove from db
        transaction1.commit();
        // removed from db, no db

        Session session3 = FactoryConfiguration.getInstance().getSession();
        Customer customer1 = session3.get(Customer.class, "C001");

    }
}
