package com.company.springmvcweb.dto;

import lombok.Data;

@Data
public class OrderUpdateDto {
    private String partNo;
    private int quantity;
    private String deliveryDate;
}
