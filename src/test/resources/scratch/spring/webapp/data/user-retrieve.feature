Feature: User - Create

  Background:
    Given there is a new user
    And the user has an "email" of "test_one@email.test"
    And the user has a "firstName" of "Test"
    And the user has a "lastName" of "User"
    And I create the user
    And there is another new user
    And the user has an "email" of "test_two@email.test"
    And the user has a "firstName" of "Test2"
    And the user has a "lastName" of "User2"
    And I create the user

  Scenario: I retrieve an existing user and it is returned in the response.
    When I request an existing user
    Then I should receive a status code of 200
    And the response body should contain the requested user

  Scenario: I retrieve a user that does not exist and the retrieve fails.
    Given I request a user with an ID of "99"
    Then I should receive a status code of 404

  Scenario: I retrieve a user with an invalid ID and the retrieve fails.
    Given I request a user with an ID of "invalid"
    Then I should receive a status code of 400

  Scenario: I retrieve all existing users and they are returned in the response.
    When I request all existing user
    Then I should receive a status code of 200
    And the response body should contain all the requested users