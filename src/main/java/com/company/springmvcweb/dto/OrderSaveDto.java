package com.company.springmvcweb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderSaveDto {
    private int supplierId;
    private String partNo;
    private int quantity;
    private String date;
}
