package com.demo.orderserver.controller;

import com.demo.model.Order;
import com.demo.model.Product;
import com.demo.orderserver.fegin.ProductFeign;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final static ExecutorService executorService = Executors.newSingleThreadExecutor();

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


    /**
     * 测试用Hystrix阶级，当商品服务不可用时
     * @return
     */
    @HystrixCommand(fallbackMethod = "getProductHystrixDefault")
    @RequestMapping("/getProductHystrix")
    public String getProductHystrix(HttpServletRequest request) {
        System.out.println("request add==" + request.getRemoteAddr());
        List<Product> products = productFeign.hystrix();
        return products.get(0).toString() + "   ****";
    }

    /**
     * 失败方法这里的请求参数必需与controller方法参数一致
     * @param request
     * @return
     */
    public String getProductHystrixDefault(HttpServletRequest request){
        executorService.execute(() -> {
            if (stringRedisTemplate.opsForValue().setIfAbsent("sendEmailByOrderGetProduct", "1")) {

                stringRedisTemplate.expire("sendEmailByOrderGetProduct", 10, TimeUnit.SECONDS);
                System.out.println("商品服务挂掉了，拿不到返回值...发送邮件...ip:" + request.getRemoteAddr());
            }else{
                System.out.println("商品服务挂掉了，拿不到返回值...");
            }

        });
        return "product-server商品服务挂掉了";
    }

    /**
     *  测试hystrix超时,可以配置在注解里，也可以配置在yml里
     * @return
     */
    @HystrixCommand(fallbackMethod = "getProductHystrixTimeoutDefault"
//        ,commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "8000")
//        }
    )
    @RequestMapping("/getProductHystrixTimeout")
    public String getProductHystrixTimeout() {

        List<Product> products = productFeign.hystrixTimeout();

        return "hello";
    }

    public String getProductHystrixTimeoutDefault(){

        return "product-server商品服超时了";
    }



    @HystrixCommand(fallbackMethod = "getProductdashboardDefault")
    @RequestMapping("/getProductdashboard")
    public String getProductdashboard(String id) {

        if (id == null || id.equals("1")) {
            return "即时响应";
        }

//        List<Product> products = productFeign.hystrix();
        List<Product> products = productFeign.dashboard();

        return "调用商品服务成功" + products;
    }

    public String getProductdashboardDefault(String id){

        return "product-server商品服超时了，dashboard失败。";
    }


    @RequestMapping("/test")
    public String test() {
        return "helo world";
    }


}
