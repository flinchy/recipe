Feature:   As a user of the Buy-Recipes API
  I want to fetch recipes
  So that I can browse them, optionally in pages

  Scenario: Retrieve all recipes
    When the client requests the list of all recipes
    Then the API returns a successful response
    And the response should contain a total element of 10

  Scenario: Retrieve recipes with pagination
    When the client requests recipes on page 2
    Then the pagination returns a successful response
    And the response contains 5 recipes
    And the pagination metadata should be correct