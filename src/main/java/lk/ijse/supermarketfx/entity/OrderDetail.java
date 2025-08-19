package lk.ijse.supermarketfx.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * --------------------------------------------
 * Author: Shamodha Sahan
 * GitHub: https://github.com/shamodhas
 * Website: https://shamodha.com
 * --------------------------------------------
 * Created: 7/1/2025 10:28 AM
 * Project: Supermarket-layered
 * --------------------------------------------
 **/

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_details") // order_details
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id") // order_id
    private Order order;
    //    private String orderId;

    @ManyToOne
    @JoinColumn(name = "item_id") // item_id
    private Item item;
//    private String itemId;

    private int quantity;

    // unit_price
    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal price;
}
