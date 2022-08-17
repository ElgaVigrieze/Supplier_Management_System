package com.company.springmvcweb.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name="order_date")
    LocalDate orderDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="supplier_id", referencedColumnName = "id", nullable = false)
    private Supplier supplier;
    @Column(name = "part_no")
    private String partNo;
    @Column(name = "quantity_order")
    private int quantityOrder;
    @Column(name = "delivery_date_order")
    private LocalDate deliveryDateOrder;
    @Column(name = "quantity_real")
    private int quantityReal;
    @Column(name = "delivery_date_real")
    private LocalDate deliveryDateReal;

    public Order(int id, LocalDate orderDate, Supplier supplier, String partNo, int quantityOrder, LocalDate deliveryDateOrder) {
        this.id = id;
        this.orderDate = orderDate;
        this.supplier = supplier;
        this.partNo = partNo;
        this.quantityOrder = quantityOrder;
        this.deliveryDateOrder = deliveryDateOrder;
    }

}
