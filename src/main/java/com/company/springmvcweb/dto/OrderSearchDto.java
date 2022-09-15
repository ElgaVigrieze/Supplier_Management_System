package com.company.springmvcweb.dto;

import com.company.springmvcweb.data.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderSearchDto {
    private String supplierId;
    private String partNo;
    private Status status;


}
