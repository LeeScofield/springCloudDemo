package com.demo.orderserver.controller;

import com.demo.model.Order;
import com.demo.model.Product;
import com.demo.orderserver.fegin.ProductFeign;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Lee on 2019-05-08.
 *
 * @author Lee
 */
@RestController
@RequestMapping(value = "/api/v1/order")
public class OrderController {

    @Autowired
    private LoadBalancerClient loadBalancer;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProductFeign productFeign;


    @RequestMapping("/list")
    public List<Order> list() {
        Order order = new Order(1, "o1", 1, "服装", 10, 2, 20);

        return Arrays.asList(order);
    }

    /**
     * 通过ribbon取值
     * @return
     */
    @RequestMapping("/getProduct")
    public String getProduct() {

//      第一种方式
        ServiceInstance instance = loadBalancer.choose("product-server");

        List list = restTemplate.getForObject(String.format("http://%s:%s/api/v1/product/list", instance.getHost(), instance.getPort()), List.class);

        //第二种方式(需要将restTemplate上的LoadBalanced注解打开)
//        List list = restTemplate.getForObject("http://product-server:8061/api/v1/product/list", List.class);
//
//        list.forEach(System.out::println);

        return list.get(0).toString() + "   ---" ;
    }


    /**
     * 通过feign取值
     * @return
     */
    @RequestMapping("/getProductByFeign")
    public String getProductByFeign(Integer id){

        List<Product> products = productFeign.productBy(id);
        return products.get(0).toString() + "   ****";

    }

    /**
     * 测试feign调用超时
     * @return
     */
    @RequestMapping("/getProductTimeout")
    public String getProductTimeout() {

        List<Product> products = productFeign.productTimeout();
        return products.get(0).toString() + "   ****";
    }


    @RequestMapping("/test")
    public String test() {
        return "helo world";
    }

}
