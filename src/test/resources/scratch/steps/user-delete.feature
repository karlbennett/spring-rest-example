Feature: User - Delete

  Background:
    Given there is a new user
    And the user has an "email" of "test_one@email.test"
    And the user has a "firstName" of "Test"
    And the user has a "lastName" of "User"
    And the user has a "phoneNumber" of "5551234"
    And the user has a "address.number" of 11
    And the user has a "address.street" of "Test Road"
    And the user has a "address.suburb" of "Testerton"
    And the user has a "address.city" of "Testopolis"
    And the user has a "address.postcode" of "TST123"
    And I create the user
    And there is another new user
    And the user has an "email" of "test_two@email.test"
    And the user has a "firstName" of "Test2"
    And the user has a "lastName" of "User2"
    And the user has a "phoneNumber" of "5551235"
    And the user has a "address.number" of 22
    And the user has a "address.street" of "Test1 Road"
    And the user has a "address.suburb" of "Testerton1"
    And the user has a "address.city" of "Testopolis1"
    And the user has a "address.postcode" of "TST124"
    And I create the user

  Scenario: I delete an existing user and it is returned in the response.
    When I delete an existing user
    Then I should receive a status code of 200
    And the response body should contain the requested user

  Scenario: I delete an existing user twice and the delete fails.
    Given I delete an existing user
    When I delete an existing user
    Then I should receive a status code of 404

  Scenario: I delete a user that does not exist and the delete fails.
    Given I delete a user with an ID of "99"
    Then I should receive a status code of 404

  Scenario: I delete a user with an invalid ID and the delete fails.
    Given I delete a user with an ID of "invalid"
    Then I should receive a status code of 400