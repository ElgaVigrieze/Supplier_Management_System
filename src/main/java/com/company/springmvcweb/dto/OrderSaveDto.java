package com.company.springmvcweb.dto;

import com.company.springmvcweb.data.Supplier;
import com.company.springmvcweb.data.SupplierRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderSaveDto {
    private int id;
    private String orderDate;
    private Supplier supplier;
    private String partNo;
    private int quantityOrder;
    private String deliveryDateOrder;


}
