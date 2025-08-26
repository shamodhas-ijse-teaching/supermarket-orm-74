package lk.ijse.supermarketfx.bo.custom.impl;

import lk.ijse.supermarketfx.bo.custom.PlaceOrderBO;
import lk.ijse.supermarketfx.bo.exception.NotFoundException;
import lk.ijse.supermarketfx.config.FactoryConfiguration;
import lk.ijse.supermarketfx.dao.DAOFactory;
import lk.ijse.supermarketfx.dao.DAOTypes;
import lk.ijse.supermarketfx.dao.custom.CustomerDAO;
import lk.ijse.supermarketfx.dao.custom.ItemDAO;
import lk.ijse.supermarketfx.dao.custom.OrderDAO;
import lk.ijse.supermarketfx.dao.custom.OrderDetailsDAO;
import lk.ijse.supermarketfx.db.DBConnection;
import lk.ijse.supermarketfx.dto.OrderDTO;
import lk.ijse.supermarketfx.dto.OrderDetailsDTO;
import lk.ijse.supermarketfx.entity.Customer;
import lk.ijse.supermarketfx.entity.Item;
import lk.ijse.supermarketfx.entity.Order;
import lk.ijse.supermarketfx.entity.OrderDetail;
import lk.ijse.supermarketfx.util.CrudUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.sql.Connection;
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
 * Created: 7/11/2025 10:25 AM
 * Project: Supermarket-layered
 * --------------------------------------------
 **/

public class PlaceOrderBOImpl implements PlaceOrderBO {
    private OrderDAO orderDAO = DAOFactory.getInstance().getDAO(DAOTypes.ORDER);

    private OrderDetailsDAO orderDetailsDAO =
            DAOFactory.getInstance().getDAO(DAOTypes.ORDER_DETAILS);
    private ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOTypes.ITEM);
    private CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOTypes.CUSTOMER);

    @Override
    public boolean placeOrder(OrderDTO dto) throws SQLException {
        // save Order
        // save OrderDetails
        // update Item

        Session currentSession = FactoryConfiguration.getInstance().getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();
        try {
            if (orderDAO.findById(dto.getOrderId()).isPresent()) {
                // duplicate id
                transaction.rollback();
                return false;
            }

            Optional<Customer> optionalCustomer = customerDAO.findById(dto.getCustomerId());
            if (optionalCustomer.isEmpty()) {
                transaction.rollback();
                return false;
            }

            Customer customer = optionalCustomer.get();

            Order order = new Order();
            order.setId(dto.getOrderId());
            order.setOrderDate(dto.getDate());
            order.setCustomer(customer);

            List<OrderDetail> orderDetailList = new ArrayList<>();
            for (OrderDetailsDTO detailsDTO : dto.getCartList()) {
                Optional<Item> optionalItem = itemDAO.findById(detailsDTO.getItemId());
                if (optionalItem.isEmpty()) {
                    transaction.rollback();
                    return false;
                }

                Item item = optionalItem.get();

                OrderDetail orderDetail = new OrderDetail();

                orderDetail.setOrder(order);
                orderDetail.setItem(item);
                orderDetail.setQuantity(detailsDTO.getQty());
                orderDetail.setPrice(BigDecimal.valueOf(detailsDTO.getPrice()));

                orderDetailList.add(orderDetail);
            }

            order.setOrderDetails(orderDetailList);

            if (!orderDAO.save(order)) {
                transaction.rollback();
                return false;
            }

            for (OrderDetail orderDetail : orderDetailList) {
                Item item = orderDetail.getItem();
                if (item.getQuantity() < orderDetail.getQuantity()) {
                    transaction.rollback();
                    return false;
                }

                item.setQuantity(item.getQuantity() - orderDetail.getQuantity());

                if (!itemDAO.updateItemForPlaceOrder(item)) {
                    transaction.rollback();
                    return false;
                }
            }

            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        }
    }

    @Override
    public String getNextId() throws SQLException {
        String lastId = orderDAO.getLastId();
        char tableChar = 'O';
        if (lastId != null) {
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format(tableChar + "%03d", nextIdNumber);
        }
        return tableChar + "001";
    }

//    private boolean saveDetailsAndUpdateItem(List<OrderDetailsDTO> detailsList) throws SQLException {
//        //            for (int i = 1; i < detailsList.size() - 1; i++) {
////                OrderDetailsDTO orderDetailsDTO = detailsList.get(i);
////            }
//        for (OrderDetailsDTO detailsDTO : detailsList) {
//            OrderDetail orderDetail = new OrderDetail();
//            orderDetail.setOrderId(detailsDTO.getOrderId());
//            orderDetail.setItemId(detailsDTO.getItemId());
//            orderDetail.setQuantity(detailsDTO.getQty());
//            orderDetail.setPrice(BigDecimal.valueOf(detailsDTO.getPrice()));
//
//            if (!orderDetailsDAO.save(orderDetail)) {
////                rollback
//                return false;
//            }
//
//            Optional<Item> optionalItem = itemDAO.findById(detailsDTO.getItemId());
//            if (optionalItem.isEmpty()) {
//                throw new NotFoundException("Item not found");
//            }
////            Item item = optionalItem.get();
////            int oldQuantity = item.getQuantity();
//
//            // check quantity here
//
////            int newQuantity = oldQuantity - detailsDTO.getQty();
////
////            item.setQuantity(newQuantity);
////
////            itemDAO.update(item);
//            boolean isItemUpdated = itemDAO.reduceQuantity(
//                    detailsDTO.getItemId(),
//                    detailsDTO.getQty()
//            );
//            if (!isItemUpdated) {
//                return false;
//            }
//        }
//        return true;
//    }
}
