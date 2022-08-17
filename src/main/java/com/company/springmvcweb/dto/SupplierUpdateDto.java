package com.company.springmvcweb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SupplierUpdateDto {
    private String name;
    private String category;
    private String eMail;

}
