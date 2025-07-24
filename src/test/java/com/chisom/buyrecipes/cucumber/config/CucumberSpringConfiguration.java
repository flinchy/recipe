package com.chisom.buyrecipes.cucumber.config;

import com.chisom.buyrecipes.BuyRecipesApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {CucumberTestConfiguration.class})
@SpringBootTest(classes = BuyRecipesApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberSpringConfiguration extends CucumberTestConfiguration {

}
