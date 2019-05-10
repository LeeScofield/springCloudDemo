package com.demo.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Lee on 2019-05-08.
 *
 * @author Lee
 */
@Data
public class Order implements Serializable {
    private Integer id;
    private String orderNo;
    private Integer productId;
    private String productName;
    private Integer productPrice;
    private Integer num;
    private Integer orderPrice;

    public Order() {
    }

    public Order(Integer id, String orderNo, Integer productId, String productName, Integer productPrice, Integer num, Integer orderPrice) {
        this.id = id;
        this.orderNo = orderNo;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.num = num;
        this.orderPrice = orderPrice;
    }
}
