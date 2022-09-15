package com.company.springmvcweb.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

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
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    LocalDate orderDate;
    @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinColumn(name="supplier_id", referencedColumnName = "id", nullable = false)
    private Supplier supplier;
    @Column(name = "part_no")
    private String partNo;
    @Column(name = "quantity_order")
    private int quantityOrder;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "delivery_date_order")
    private LocalDate deliveryDateOrder;
    @Column(name = "quantity_real")
    private int quantityReal;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
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
