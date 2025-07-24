Feature: Manage recipes in the shopping cart
  As a shopper
  I want to add a complete recipe (all its ingredients) to my cart and later remove it
  So that my cart always reflects exactly what I intend to buy

  @DatabaseSeeding @DatabaseCleanUp
  Scenario: Adding and removing a recipe adjusts cart contents and total
    Given I have a valid recipe payload and an empty cart
    When I add the recipe to my cart
    Then the operation succeeds
    And my cart shows the newly added recipe
    And the total amount matches the sum of those ingredient prices
    When I delete the recipe from my cart
    Then the operation succeeds
    And the recipe’s ingredients are removed from my cart
    And the total amount is updated accordingly
