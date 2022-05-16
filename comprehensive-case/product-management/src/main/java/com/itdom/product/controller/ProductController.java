package com.itdom.product.controller;

import com.itdom.product.entity.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/list")
    public List<? extends Product> getProductList() {
        return new ArrayList<Product>() {{
            add(new Product(1L, "10000001", "产品1"));
            add(new Product(2L, "10000002", "产品2"));
            add(new Product(3L, "10000003", "产品3"));
            add(new Product(4L, "10000004", "产品4"));
            add(new Product(5L, "10000005", "产品5"));
        }};
    }

}
