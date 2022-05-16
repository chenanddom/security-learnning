package com.itdom.product.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
@AllArgsConstructor
@Data
public class Product implements Serializable {
    private Long id;

    private String productNumber;

    private String productName;


}
