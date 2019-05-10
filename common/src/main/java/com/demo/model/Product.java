package com.demo.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Lee on 2019-05-08.
 *
 * @author Lee
 */
@Data
public class Product implements Serializable {
    private Integer id;
    private String name;
    private Integer price;

    public Product() {
    }

    public Product(Integer id, String name, Integer price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
