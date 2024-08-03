package com.bijoy.cloud.productservice.controller;

import com.bijoy.cloud.productservice.dto.Coupon;
import com.bijoy.cloud.productservice.model.Product;
import com.bijoy.cloud.productservice.repository.ProductRepository;
import com.bijoy.cloud.productservice.validator.CouponCodeValidatorGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/productapi")
public class ProductController {
    @Value("${couponapi.url}")
    private String couponAPI_URL;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/products")
    public ResponseEntity<Object> createProduct(
            @RequestBody @Validated({CouponCodeValidatorGroup.class}) Product product) {
        try {
            ResponseEntity<Coupon> couponResponse = restTemplate.getForEntity(
                    couponAPI_URL + product.getCouponCode(),
                    Coupon.class);
            if (couponResponse.getStatusCode().is2xxSuccessful()) {
                Coupon coupon = couponResponse.getBody();
                if (coupon.getMessage() == null) {
                    //subtract the coupon
                    product.setPrice(product.getPrice().subtract(coupon.getDiscount()));
                } else  {
                    product.setCouponCode(null);
                }
            }
            Product savedProduct = productRepository.save(product);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (Exception ex) {
            System.out.println("Exception occurred: " + ex.getMessage());
            return new ResponseEntity<>("{\"message\": \"" + ex.getMessage() + "\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getASingleProduct(@PathVariable("id") Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            return ResponseEntity.ok(optionalProduct.get());
        } else {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "Product with id: {" + id + "} does not exist!");
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }
    }
}
