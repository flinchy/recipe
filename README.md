## Running the application on your local machine

To begin, you need to start up the postgre db with docker compose

First *cd* into the working directory then run the below command
```bash
  docker compose up -d
  
 ```

### API Test Automation Stack
This application implements a robust end-to-end API testing framework combining:

- RestAssured: For streamlined HTTP requests and response validation
- Cucumber (BDD): For behavior-driven test scenarios in Gherkin syntax

#### Key features of our API testing solution:

- Behavior-Driven Development (BDD) Approach
    - Feature files written in business readable Gherkin syntax
    - Step definitions mapping to API test operations
- Comprehensive API Validation
    - Status code assertions
    - Response time measurements
    - Schema validation (JSON Schema)


#### Running the test with cucumber

```  mvn clean test -P cucumber-tests cluecumber-report:reporting  ```


To run Unit Test
```bash
mvn clean test -P unit-integration
```
