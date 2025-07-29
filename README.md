## Prerequisite
  - Java 21
  - docker

## Running the application on your local machine

To begin, you need to start up the postgres db with docker compose

First *cd* into the working directory then run the below command
```bash
  docker compose up -d
  
 ```

Run the test to make sure all passes with the below command
```bash
    mvn clean test
 ```


Then start the application with the below command
```shell
    ./mvnw spring-boot:run
```

### API Test Automation Stack
This application implements a robust end-to-end API testing framework combining:

- RestAssured: For streamlined HTTP requests and response validation
- Cucumber (BDD): For behavior-driven test scenarios in Gherkin syntax
- Testcontainers

#### Key features of our API testing solution:

- Behavior-Driven Development (BDD) Approach
    - Feature files written in business readable Gherkin syntax
    - Step definitions mapping to API test operations
- Comprehensive API Validation
    - Status code assertions
    - Response assertions
    - Schema validation (JSON Schema)


#### Running the cucumber test alone
```bash
    mvn clean test -P cucumber-tests cluecumber-report:reporting
```

#### Running the Unit test alone
```bash
  mvn clean test -P unit
```

### DATA MODEL

```mermaid
erDiagram
    CARTS {
        BIGINT id PK
        TIMESTAMP created_at
        TIMESTAMP updated_at
        TIMESTAMP deleted_at
        NUMERIC total_in_cents
    }

    PRODUCTS {
        BIGINT id PK
        TIMESTAMP created_at
        TIMESTAMP updated_at
        TIMESTAMP deleted_at
        VARCHAR name
        NUMERIC price_in_cents
    }

    RECIPE {
        BIGINT id PK
        TIMESTAMP created_at
        TIMESTAMP updated_at
        TIMESTAMP deleted_at
        VARCHAR name
        VARCHAR description
    }

    CART_RECIPES {
        BIGINT id PK
        TIMESTAMP created_at
        TIMESTAMP updated_at
        TIMESTAMP deleted_at
        BIGINT cart_id FK
        BIGINT recipe_id FK
    }

    RECIPE_INGREDIENT {
        BIGINT id PK
        TIMESTAMP created_at
        TIMESTAMP updated_at
        TIMESTAMP deleted_at
        BIGINT recipe_id FK
        BIGINT product_id FK
    }

    CART_ITEMS {
        BIGINT id PK
        TIMESTAMP created_at
        TIMESTAMP updated_at
        TIMESTAMP deleted_at
        BIGINT cart_id FK
        BIGINT product_id FK
        BIGINT cart_recipe_id FK
        cartline line_type
        BIGINT quantity
    }

    CARTS ||--o{ CART_ITEMS : contains
    %% CART_ITEMS ||--o{ CART_RECIPES : contains
    CART_ITEMS ||--o{ PRODUCTS : contains
    RECIPE ||--o{ RECIPE_INGREDIENT : contains
    RECIPE_INGREDIENT ||--o{ PRODUCTS : contains

    RECIPE ||--o{ CART_RECIPES : contains
    CART_RECIPES ||--o{ CART_ITEMS : contains

```
