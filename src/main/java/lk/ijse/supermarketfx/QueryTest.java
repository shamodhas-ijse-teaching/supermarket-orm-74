package lk.ijse.supermarketfx;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lk.ijse.supermarketfx.config.FactoryConfiguration;
import lk.ijse.supermarketfx.entity.Customer;
import lk.ijse.supermarketfx.entity.Order;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.util.List;

/**
 * --------------------------------------------
 * Author: Shamodha Sahan
 * GitHub: https://github.com/shamodhas
 * Website: https://shamodha.com
 * --------------------------------------------
 * Created: 8/26/2025 9:17 AM
 * Project: supermarket-orm-layered-architecture
 * --------------------------------------------
 **/

public class QueryTest {
    public static void main(String[] args) {
//        Session session = FactoryConfiguration.getInstance().getSession();

//        // Native SQL (SQL)
//        NativeQuery<Customer> nativeQuery = session.createNativeQuery(
//                "select * from cutomer_table where customer_name='John Doe'",
//                Customer.class
//        );
//        List<Customer> list = nativeQuery.list();
//        System.out.println("========== SQL ==========");
//        list.forEach(customer -> System.out.println(customer.toString()));
//
//        // HQL / JPQL
//        Query<Customer> query = session.createQuery(
//                "from Customer where name='John Doe'",
//                Customer.class
//        );
//        List<Customer> customerList = query.list();
//        System.out.println("========== HQL / JPQL ==========");
//        customerList.forEach(customer -> System.out.println(customer.toString()));


//      Parameter

        // Native SQL (SQL)
//        NativeQuery<Customer> nativeQuery = session.createNativeQuery(
//                "select * from cutomer_table where customer_name= :cus_name",
//                Customer.class
//        );
//        nativeQuery.setParameter("cus_name","John Doe");
//        List<Customer> list = nativeQuery.list();
//        System.out.println("========== SQL ==========");
//        list.forEach(customer -> System.out.println(customer.toString()));
//
//        // HQL / JPQL
//        Query<Customer> query = session.createQuery(
//                "from Customer where name= :cus_name",
//                Customer.class
//        );
//        query.setParameter("cus_name","John Doe");
//        List<Customer> customerList = query.list();
//        System.out.println("========== HQL / JPQL ==========");
//        customerList.forEach(customer -> System.out.println(customer.toString()));

//        Session session = FactoryConfiguration.getInstance().getSession();

        // Criteria API

//        // 1. create CriteriaBuilder object
//        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
//        // 2. create CriteriaQuery object, Define what entity class // select *
//        CriteriaQuery<Customer> query = criteriaBuilder.createQuery(Customer.class);
//        // 3. Setup root entity from Customer
//        Root<Customer> from = query.from(Customer.class);
//        // from Customer where name = 'John Doe'
//        query.select(from).where(
//                criteriaBuilder.equal(from.get("name"), "John Doe")
//        );
//        Query<Customer> customerQuery = session.createQuery(query);
//        List<Customer> customerList = customerQuery.list();
//        customerList.forEach(customer -> System.out.println(customer.toString()));

        // select * from cutomer_table c left join orders o on c.id = o.cus_id where c.id='C001'
        Session session = FactoryConfiguration.getInstance().getSession();
        Query<Object[]> joinQuery = session.createQuery(
                "from Customer c left join Order o on c.id = o.customer where c.id='C001'",
                Object[].class
        );
        List<Object[]> objects = joinQuery.list();
//        [
//          [object(customer) -> 0 , object(order) -> 1],
//          [object(customer),object(order)]
//        ]
        for (Object[] objectArray : objects) {
            Customer customer = (Customer) objectArray[0];
            Order order = (Order) objectArray[1];
        }
    }
}
