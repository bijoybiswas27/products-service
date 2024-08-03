package com.bijoy.cloud.productservice.model;

import com.bijoy.cloud.productservice.validator.CouponCodeValidatorGroup;
import com.bijoy.cloud.productservice.validator.ProductValidatorGroup;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(groups = {ProductValidatorGroup.class})
    private String name;
    private String description;
    private BigDecimal price;
    @Transient
    @NotNull(groups = {CouponCodeValidatorGroup.class}, message = "couponCode is a required parameter")
    @NotEmpty(groups = {CouponCodeValidatorGroup.class}, message = "couponCode cannot be empty")
    private String couponCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}
