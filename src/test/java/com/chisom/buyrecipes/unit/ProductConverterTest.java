package com.chisom.buyrecipes.unit;

import com.chisom.buyrecipes.converter.ProductConverter;
import com.chisom.buyrecipes.dto.ProductResponse;
import com.chisom.buyrecipes.entity.Products;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductConverterTest {

    private ProductConverter productConverter;

    @BeforeEach
    void setUp() {
        productConverter = new ProductConverter();
    }

    @Test
    void convertToProductResponse() {
        Products products = new Products();
        products.setName("Apple");
        products.setPriceIncents(new BigDecimal("99.0"));

        ProductResponse response = productConverter.apply(products);
        assertEquals("Apple", response.name());
        assertEquals(0, new BigDecimal("99.0").compareTo(response.price()));
    }
}
