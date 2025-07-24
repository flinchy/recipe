package com.chisom.buyrecipes.converter;

import com.chisom.buyrecipes.dto.ProductResponse;
import com.chisom.buyrecipes.entity.Products;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ProductConverter implements Function<Products, ProductResponse> {

    @Override
    public ProductResponse apply(Products products) {
        return toProductResponse(products);
    }

    private ProductResponse toProductResponse(Products products) {
        return new ProductResponse(
                products.getId(),
                products.getName(),
                products.getPriceIncents()
        );
    }
}
