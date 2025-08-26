package lk.ijse.supermarketfx.dao.custom.impl;

import lk.ijse.supermarketfx.config.FactoryConfiguration;
import lk.ijse.supermarketfx.dao.SQLUtil;
import lk.ijse.supermarketfx.dao.custom.CustomerDAO;
import lk.ijse.supermarketfx.dto.CustomerDTO;
import lk.ijse.supermarketfx.entity.Customer;
import lk.ijse.supermarketfx.util.CrudUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * --------------------------------------------
 * Author: Shamodha Sahan
 * GitHub: https://github.com/shamodhas
 * Website: https://shamodha.com
 * --------------------------------------------
 * Created: 7/1/2025 10:51 AM
 * Project: Supermarket-layered
 * --------------------------------------------
 **/

public class CustomerDAOImpl implements CustomerDAO {
    private final FactoryConfiguration factoryConfiguration =
            FactoryConfiguration.getInstance();

    @Override
    public boolean save(Customer customer) throws SQLException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(customer);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Customer> getAll() throws SQLException {
        Session session = factoryConfiguration.getSession();
        try {
            Query<Customer> query = session.createQuery(
                    "from Customer",
                    Customer.class
            );
            List<Customer> list = query.list();
            return list;
        } finally {
            session.close();
        }
    }

    @Override // no need
    public String getLastId() throws SQLException {
        // SELECT customer_id FROM customer ORDER BY customer_id DESC
        // LIMIT 1
        Session session = factoryConfiguration.getSession();
        try {
            Query<String> query = session.createQuery(
                    "SELECT c.id FROM Customer c ORDER BY c.id DESC",
                    String.class
            ).setMaxResults(1);
            List<String> list = query.list();
            if (list.isEmpty()) {
                return null;
            }
            return list.get(0);
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(Customer customer) throws SQLException {
        Session session = factoryConfiguration.getSession();
        Transaction tx = session.beginTransaction();
        try {
            session.merge(customer);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(String id) throws SQLException {
        Session session = factoryConfiguration.getSession();
        Transaction tx = session.beginTransaction();
        try {
            Customer customer = session.get(Customer.class, id);
            if (customer != null) {
                session.remove(customer);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override // no need
    public List<String> getAllIds() throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT customer_id FROM customer");
        List<String> ids = new ArrayList<>();
        while (resultSet.next()) {
            ids.add(resultSet.getString(1));
        }
        return ids;
    }

    @Override
    public Optional<Customer> findById(String id) throws SQLException {
        Session session = factoryConfiguration.getSession();
        try {
            Customer customer = session.get(Customer.class, 1);
            return Optional.ofNullable(customer);
        } finally {
            session.close();
        }
    }
    // %ll%
    // hello

    @Override // no need
    public List<Customer> search(String text) throws SQLException {
        String searchText = "%" + text + "%";
        // SELECT * FROM customer WHERE customer_id LIKE ? OR name LIKE ? OR nic LIKE ? OR email LIKE ? OR phone LIKE ?
        Session session = factoryConfiguration.getSession();
        try {
            Query<Customer> query = session.createQuery(
                    "FROM Customer c WHERE c.id LIKE :text OR c.name LIKE :text OR c.nic LIKE :text OR c.email LIKE :text OR c.phone LIKE :text",
                    Customer.class
            );
            query.setParameter("text", searchText);
            return query.list();
        } finally {
            session.close();
        }

    }

    @Override // no need
    public Optional<Customer> findCustomerByNic(String nic) throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM customer WHERE nic = ?", nic);
        if (resultSet.next()) {
            return Optional.of(new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return Optional.empty();
    }

    @Override // no need
    public boolean existsCustomerByPhoneNumber(String phoneNumber) throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM customer WHERE phone = ?", phoneNumber);
//        if (resultSet.next()){
//            return true;
//        }
//        return false;

        return resultSet.next();
    }
}
