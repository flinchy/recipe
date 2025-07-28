package com.chisom.buyrecipes.cucumber.stepdefinitions;

import com.chisom.buyrecipes.dto.PageResponse;
import com.chisom.buyrecipes.dto.RecipeResponse;
import com.chisom.buyrecipes.util.ScenarioContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
@RequiredArgsConstructor
public class RecipeStepDef {

    private final ScenarioContext scenarioContext;

    @Before
    public void setUp() {
        //ensures a scenario starts with fresh state
        scenarioContext.clear();
    }

    /*==================================================================================================================
     SCENARIO: RETRIEVE ALL RECIPES
     =================================================================================================================*/

    @When("the client requests the list of all recipes")
    public void the_client_requests_the_list_of_all_recipes() {
        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/recipes")
                .then().log().all().extract().response();

        scenarioContext.set("recipeResponse", response);
    }

    @Then("the API returns a successful response")
    public void the_api_returns_a_successful_response() {
        scenarioContext.get("recipeResponse", Response.class).then()
                .assertThat().statusCode(200);
    }

    @Then("the response should contain a total element of {int}")
    public void the_response_should_contain_a_total_element_of(Integer totalElement) {
        Response response = scenarioContext.get("recipeResponse", Response.class);
        TypeRef<PageResponse<RecipeResponse>> recipePage = new TypeRef<>() {
        };

        PageResponse<RecipeResponse> page = response.as(recipePage);
        assertEquals((long) totalElement, page.totalElements());
    }

    /*==================================================================================================================
     SCENARIO: RETRIEVE RECIPES WITH PAGINATION
     =================================================================================================================*/

    @When("the client requests recipes on page {int}")
    public void the_client_requests_recipes_on_page(Integer page) {
        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/recipes?page=%s".formatted(page))
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/recipe-schema.json"))
                .log().all().extract().response();

        scenarioContext.set("recipeResponse", response);
    }

    @Then("the pagination returns a successful response")
    public void the_pagination_returns_a_successful_response() {
        scenarioContext.get("recipeResponse", Response.class).then()
                .assertThat().statusCode(200);
    }

    @Then("the response contains {int} recipes")
    public void the_response_contains_recipes(Integer totalContent) {
        Response response = scenarioContext.get("recipeResponse", Response.class);
        TypeRef<PageResponse<RecipeResponse>> recipePage = new TypeRef<>() {
        };
        PageResponse<RecipeResponse> page = response.as(recipePage);
        assertEquals(totalContent, page.content().size());
    }

    @Then("the pagination metadata should be correct")
    public void the_pagination_metadata_should_be_correct() {
        Response response = scenarioContext.get("recipeResponse", Response.class);
        TypeRef<PageResponse<RecipeResponse>> recipePage = new TypeRef<>() {
        };

        PageResponse<RecipeResponse> page = response.as(recipePage);
        assertEquals(1, page.page());
        assertEquals(5, page.size());
        assertEquals(10, page.totalElements());
    }

    @After
    public void tearDown() {
        scenarioContext.clear();
        logger.info("Scenario for Recipe completed. Scenario Context Cleared.");
    }
}
