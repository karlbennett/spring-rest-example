Feature: User - Update

  Background:
    Given there is a new user
    And the user has an "email" of "test_one@email.test"
    And the user has a "firstName" of "Test"
    And the user has a "lastName" of "User"
    And the user has a "phoneNumber" of "5551234"
    And the user has a "address" of "null"
    And I create the user
    And there is another new user
    And the user has an "email" of "test_two@email.test"
    And the user has a "firstName" of "Test2"
    And the user has a "lastName" of "User2"
    And the user has a "phoneNumber" of "5551235"
    And the user has a "address" of "null"
    And I create the user

  Scenario Outline: I update an existing user and the user is updated correctly.
    Given the user has an "email" of "<email>"
    And the user has a "firstName" of "<first-name>"
    And the user has a "lastName" of "<last-name>"
    And the user has a "phoneNumber" of "<phone-number>"
    When I update the user
    Then I should receive a status code of 200
    And the response body should contain the updated user
    And the user should be updated
  Examples:
    | email                 | first-name | last-name | phone-number |
    | test_two@email.test   | Test2      | User2     | 5551235      |
    | test_three@email.test | Test2      | User2     | 5551235      |
    | test_two@email.test   | Test3      | User2     | 5551235      |
    | test_two@email.test   | Test2      | User3     | 5551235      |
    | test_two@email.test   | Test2      | User2     | 5551236      |
    | test_three@email.test | Test3      | User3     | 5551236      |

  Scenario Outline: I update an existing user with the same name and phone number values as an existing and the user is updated correctly.
    Given the user has an "email" of "<email>"
    And the user has a "firstName" of "<first-name>"
    And the user has a "lastName" of "<last-name>"
    And the user has a "phoneNumber" of "<phone-number>"
    When I update the user
    Then I should receive a status code of 200
    And the response body should contain the updated user
    And the user should be updated
  Examples:
    | email               | first-name | last-name | phone-number |
    | test_two@email.test | Test       | User2     | 5551235      |
    | test_two@email.test | Test2      | User      | 5551235      |
    | test_two@email.test | Test2      | User2     | 5551234      |

  Scenario Outline: I update an existing user with the no phone number value and the user is updated correctly.
    Given the user has an "email" of "test_two@email.test"
    And the user has a "firstName" of "Test2"
    And the user has a "lastName" of "User2"
    And the user has a "phoneNumber" of "<phone-number>"
    When I update the user
    Then I should receive a status code of 200
    And the response body should contain the updated user
    And the user should be updated
  Examples:
    | phone-number |
    |              |
    | null         |

  Scenario: I update an existing user with the no phone number field and the user is updated correctly.
    Given the user has no "phoneNumber" field
    When I update the user
    Then I should receive a status code of 200
    And the user has a "phoneNumber" of "null"
    And the response body should contain the updated user
    And the user should be updated

  Scenario: I update an existing user with the email of an existing user and update fails.
    Given the user has an "email" of "test_one@email.test"
    And the user has a "firstName" of "Test2"
    And the user has a "lastName" of "User2"
    And the user has a "phoneNumber" of "5551235"
    When I update the user
    Then I should receive a status code of 400

  Scenario Outline: I update an existing user with missing values and the update fails.
    Given there is a new user
    And the user has an "email" of "<email>"
    And the user has a "firstName" of "<first-name>"
    And the user has a "lastName" of "<last-name>"
    And the user has a "phoneNumber" of "5551235"
    When I update the user
    Then I should receive a status code of 400
  Examples:
    | email               | first-name | last-name |
    |                     | Test       | User      |
    | null                | Test       | User      |
    | test_one@email.test |            | User      |
    | test_one@email.test | null       | User      |
    | test_one@email.test | Test       |           |
    | test_one@email.test | Test       | null      |

  Scenario Outline: I update an existing user with missing fields and the update fails.
    Given the user has no "<field-name>" field
    When I update the user
    Then I should receive a status code of 400
  Examples:
    | field-name |
    | email      |
    | firstName  |
    | lastName   |

  Scenario: I update an existing user with an empty user and the update fails.
    Given there is a new user
    When I update the user
    Then I should receive a status code of 400