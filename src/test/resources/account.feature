Feature: Get Account Details
  Scenario: Verify Blog  Details
    Given User is authorized for the request with username "admin" and password "admin"
    When  User calls the "/blogs" with path param as 3951
    Then  Response should have status code as 200
    And   resposne should have name as  "RestAssured Testing"