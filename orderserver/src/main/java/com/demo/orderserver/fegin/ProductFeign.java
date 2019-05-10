package com.demo.orderserver.fegin;

import com.demo.model.Product;
import com.demo.orderserver.fegin.fallback.ProductFeignFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by Lee on 2019-05-10.
 * fallback针对于商品服务降级处理，必需在配置文件中开启，否则不生效
 * @author Lee
 */
@FeignClient(value = "product-server",fallback = ProductFeignFallBack.class)
public interface ProductFeign {

    /**
     * 参数必需加@RequestParam，官网给的@PathVariable是不行的
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/product/productBy")
    public List<Product> productBy(@RequestParam("id") Integer id);

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/product/productTimeout")
    public List<Product> productTimeout();


    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/product/hystrix")
    public List<Product> hystrix();

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/product/hystrixTimeout")
    public List<Product> hystrixTimeout();

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/product/dashboard")
    public List<Product> dashboard();
}
