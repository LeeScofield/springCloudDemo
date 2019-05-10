package com.demo.orderserver.fegin;

import com.demo.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by Lee on 2019-05-10.
 *
 * @author Lee
 */
@FeignClient("product-server")
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
}
