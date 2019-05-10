package com.demo.productserver.controller;

import com.demo.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Lee on 2019-05-08.
 *
 * @author Lee
 */
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Value("${server.port}")
    private String port;

    @RequestMapping("/list")
    public List<Product> list(){
        Product product = new Product(1,"服装 port:" + port,10);

        return Arrays.asList(product);
    }

    @RequestMapping("/productBy")
    public List<Product> list(String id){
        System.out.println(id);

        Product product = new Product(Integer.valueOf(id),"餐具 port:" + port,10);

        return Arrays.asList(product);
    }


}
