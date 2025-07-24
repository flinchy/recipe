package com.chisom.buyrecipes.cucumber.stepdefinitions;

import com.chisom.buyrecipes.dto.CartResponse;
import com.chisom.buyrecipes.util.ScenarioContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
public class CartsStepDef {

    static final int CART_ID = 112344;
    private final ScenarioContext scenarioContext;

    @Given("I have a valid recipe payload and an empty cart")
    public void i_have_a_valid_recipe_payload_and_an_empty_cart() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("recipeId", 1);
        payload.put("quantity", 1);
        scenarioContext.set("payload", payload);
    }

    @SuppressWarnings("unchecked")
    @When("I add the recipe to my cart")
    public void i_add_the_recipe_to_my_cart() {
        Map<String, Object> payload = scenarioContext.get("payload", Map.class);
        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(payload)
                .when()
                .post("/carts/%d/add_recipe".formatted(CART_ID))
                .then().log().all().extract().response();

        scenarioContext.set("recipeResponse", response);
    }

    @Then("the operation succeeds")
    public void the_operation_succeeds() {
        scenarioContext.get("recipeResponse", Response.class).then()
                .assertThat().statusCode(200);
    }

    @Then("my cart shows the newly added recipe")
    public void my_cart_shows_the_newly_added_recipe() {
        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/carts/%d".formatted(CART_ID))
                .then().assertThat().statusCode(200)
                .log().all().extract().response();

        scenarioContext.set("recipeResponse", response);
    }

    @Then("the total amount matches the sum of those ingredient prices")
    public void the_total_amount_matches_the_sum_of_those_ingredient_prices() {
        CartResponse cartResponse = scenarioContext.get("recipeResponse", Response.class)
                .as(CartResponse.class);

        assertEquals(0, new BigDecimal("1575.00").compareTo(cartResponse.total()));
        assertEquals(112344, cartResponse.id());
        assertFalse(cartResponse.cartItem().isEmpty());
    }

    @When("I delete the recipe from my cart")
    public void i_delete_the_recipe_from_my_cart() {
        CartResponse cartResponse = scenarioContext.get("recipeResponse", Response.class)
                .as(CartResponse.class);
        final Long CART_RECIPE_ID = cartResponse.cartItem().getFirst().recipe().id();

        given()
                .when()
                .delete("/carts/%d/recipes/%d".formatted(CART_ID, CART_RECIPE_ID))
                .then()
                .assertThat()
                .statusCode(200).log();
    }

    @Then("the recipe’s ingredients are removed from my cart")
    public void the_recipe_s_ingredients_are_removed_from_my_cart() {
        Response response = given()
                .when()
                .get("/carts/%d".formatted(CART_ID))
                .then().assertThat().statusCode(200)
                .log().all().extract().response();

        scenarioContext.set("recipeResponse", response);
    }

    @Then("the total amount is updated accordingly")
    public void the_total_amount_is_updated_accordingly() {
        CartResponse cartResponse = scenarioContext.get("recipeResponse", Response.class)
                .as(CartResponse.class);

        assertEquals(0, new BigDecimal("0.00").compareTo(cartResponse.total()));
        assertTrue(cartResponse.cartItem().isEmpty());
    }
}
