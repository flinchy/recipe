package com.chisom.buyrecipes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BuyRecipesApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuyRecipesApplication.class, args);
    }
}
