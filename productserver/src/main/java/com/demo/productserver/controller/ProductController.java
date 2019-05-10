package com.demo.productserver.controller;

import com.demo.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    public List<Product> productBy(@RequestParam("id") Integer id){

        System.out.println("id:" + id);
        Product product = new Product(id,"餐具 port:" + port,20);

        return Arrays.asList(product);
    }

    @RequestMapping("/productTimeout")
    public List<Product> productTimeout(){

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Product product = new Product(5,"手机 port:" + port,30);

        return Arrays.asList(product);
    }
}
