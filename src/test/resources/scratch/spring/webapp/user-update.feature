Feature: User - Update

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

  Scenario Outline: I update an existing user and the user is updated correctly.
    Given the user has an "email" of "<email>"
    And the user has a "firstName" of "<first-name>"
    And the user has a "lastName" of "<last-name>"
    When I update the user
    Then I should receive a status code of 200
    And the response body should contain the updated user
    And the user should be updated
  Examples:
    | email                 | first-name | last-name |
    | test_two@email.test   | Test2      | User2     |
    | test_three@email.test | Test2      | User2     |
    | test_two@email.test   | Test3      | User2     |
    | test_two@email.test   | Test2      | User3     |
    | test_three@email.test | Test3      | User3     |

  Scenario Outline: I update an existing user with the name values as an existing and the user is updated correctly.
    Given the user has an "email" of "<email>"
    And the user has a "firstName" of "<first-name>"
    And the user has a "lastName" of "<last-name>"
    When I update the user
    Then I should receive a status code of 200
    And the response body should contain the updated user
    And the user should be updated
  Examples:
    | email               | first-name | last-name |
    | test_two@email.test | Test       | User2     |
    | test_two@email.test | Test2      | User      |

  Scenario: I update an existing user with the email of an existing user and update fails.
    Given the user has an "email" of "test_one@email.test"
    And the user has a "firstName" of "Test2"
    And the user has a "lastName" of "User2"
    When I update the user
    Then I should receive a status code of 400

  Scenario Outline: I update an existing user with missing values and the update fails.
    Given there is a new user
    And the user has an "email" of "<email>"
    And the user has a "firstName" of "<first-name>"
    And the user has a "lastName" of "<last-name>"
    When I update the user
    Then I should receive a status code of 400
  Examples:
    | email               | first-name | last-name |
    |                     | Test       | User      |
    | test_one@email.test |            | User      |
    | test_one@email.test | Test       |           |

  Scenario Outline: I update an existing user with null values and the update fails.
    Given there is a new user
    And the user has an "email" of "<email>"
    And the user has a "firstName" of "<first-name>"
    And the user has a "lastName" of "<last-name>"
    When I update the user
    Then I should receive a status code of 400
  Examples:
    | email               | first-name | last-name |
    | null                | Test       | User      |
    | test_one@email.test | null       | User      |
    | test_one@email.test | Test       | null      |

  Scenario Outline: I update an existing user with missing fields and the update fails.
    Given there is a new user
    And the user has an "<email>" of "test_one@email.test"
    And the user has a "<first-name>" of "Test"
    And the user has a "<last-name>" of "User"
    When I update the user
    Then I should receive a status code of 400
  Examples:
    | email | first-name | last-name |
    |       | firstName  | lastName  |
    | email |            | lastName  |
    | email | firstName  |           |

  Scenario: I update an existing user with an empty user and the update fails.
    Given there is a new user
    When I update the user
    Then I should receive a status code of 400