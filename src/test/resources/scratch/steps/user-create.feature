Feature: User - Create

  Background:
    Given there is a new user
    And the user has an "email" of "test_one@email.test"
    And the user has a "firstName" of "Test"
    And the user has a "lastName" of "User"
    And the user has a "phoneNumber" of "5551234"

  Scenario: I create a new user and the user is persisted correctly.
    When I create the user
    Then I should receive a status code of 201
    And the response body should contain the new user
    And the new user should be persisted

  Scenario Outline: I create a user with no phone number value and the creation succeeds.
    Given there is a new user
    And the user has an "email" of "test_one@email.test"
    And the user has a "firstName" of "Test"
    And the user has a "lastName" of "User"
    And the user has a "phoneNumber" of "<phone-number>"
    And I create the user
    Then I should receive a status code of 201
    And the response body should contain the new user
    And the new user should be persisted
  Examples:
    | phone-number |
    |              |
    | null         |

  Scenario: I create a user with no phone number field and the creation succeeds.
    Given there is a new user
    And the user has an "email" of "test_one@email.test"
    And the user has a "firstName" of "Test"
    And the user has a "lastName" of "User"
    And I create the user
    Then I should receive a status code of 201
    And the user has a "phoneNumber" of "null"
    And the response body should contain the new user
    And the new user should be persisted

  Scenario: I create a new user with an existing id and the creation fails.
    Given I create the user
    When the user has an "id" of the last created user
    And I create the user again
    Then I should receive a status code of 400

  Scenario: I create the same user twice and the second creation fails.
    Given I create the user
    When I create the user again
    Then I should receive a status code of 400

  Scenario: I create a user with an existing email and the creation fails.
    Given I create the user
    When there is another new user
    And the user has an "email" of "test_one@email.test"
    And the user has a "firstName" of "Test2"
    And the user has a "lastName" of "User2"
    And the user has a "phoneNumber" of "5551235"
    And I create the user
    Then I should receive a status code of 400

  Scenario Outline: I create a user with existing name and phone number values and the user is persisted correctly.
    Given I create the user
    When there is another new user
    And the user has an "email" of "test_two@email.test"
    And the user has a "firstName" of "<first-name>"
    And the user has a "lastName" of "<last-name>"
    And the user has a "phoneNumber" of "<phone_number>"
    And I create the user
    Then I should receive a status code of 201
    And the response body should contain the new user
    And the new user should be persisted
  Examples:
    | first-name | last-name | phone_number |
    | Test       | User2     | 5551235      |
    | Test2      | User      | 5551235      |
    | Test2      | User2     | 5551234      |
    | Test       | User      | 5551234      |

  Scenario Outline: I create a user with missing values and the creation fails.
    Given there is a new user
    And the user has an "email" of "<email>"
    And the user has a "firstName" of "<first-name>"
    And the user has a "lastName" of "<last-name>"
    And the user has a "phoneNumber" of "5551234"
    When I create the user
    Then I should receive a status code of 400
  Examples:
    | email               | first-name | last-name |
    |                     | Test       | User      |
    | null                | Test       | User      |
    | test_one@email.test |            | User      |
    | test_one@email.test | null       | User      |
    | test_one@email.test | Test       |           |
    | test_one@email.test | Test       | null      |

  Scenario Outline: I create a user with missing fields and the creation fails.
    Given there is a new user
    And the user has an "<email>" of "test_one@email.test"
    And the user has a "<first-name>" of "Test"
    And the user has a "<last-name>" of "User"
    And the user has a "phoneNumber" of "5551234"
    When I create the user
    Then I should receive a status code of 400
  Examples:
    | email | first-name | last-name |
    |       | firstName  | lastName  |
    | email |            | lastName  |
    | email | firstName  |           |

  Scenario: I create a user with an invalid field and the creation fails.
    Given the user has an "invalid" of "true"
    When I create the user
    Then I should receive a status code of 400

  Scenario: I create an empty user and the creation fails.
    Given there is a new user
    When I create the user
    Then I should receive a status code of 400