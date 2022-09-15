package com.company.springmvcweb.dto;
import lombok.Data;

@Data
public class SupplierSaveDto {
    private int id;
    private String name;
    private String VATNo;
    private String category;
    private String eMail;

}
