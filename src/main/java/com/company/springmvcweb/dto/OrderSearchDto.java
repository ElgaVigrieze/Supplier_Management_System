package com.company.springmvcweb.dto;

import com.company.springmvcweb.data.Status;
import lombok.Data;

@Data
public class OrderSearchDto {
    private String supplierId;
    private String partNo;
    private Status status;


}
