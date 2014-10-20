Feature: User - Create

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

  Scenario: I create a new user and the user is persisted correctly.
    Given I create the user
    Then I should receive a status code of 201
    And the response body should contain the new user
    And the new user should be persisted

  Scenario Outline: I create a user with no phone number value and the creation succeeds.
    Given the user has a "phoneNumber" of "<phone-number>"
    When I create the user
    Then I should receive a status code of 201
    And the response body should contain the new user
    And the new user should be persisted
  Examples:
    | phone-number |
    |              |
    | null         |

  Scenario Outline:  create a user with missing address values and the user is persisted correctly.
    Given I create the user
    When the user has an "email" of "test_two@email.test"
    And the user has a "firstName" of "Test2"
    And the user has a "lastName" of "User2"
    And the user has a "phoneNumber" of "5551235"
    And the user has a "address.number" of <number>
    And the user has a "address.street" of "<street>"
    And the user has a "address.suburb" of "<suburb>"
    And the user has a "address.city" of "<city>"
    And the user has a "address.postcode" of "<postcode>"
    And I create the user
    Then I should receive a status code of 201
    And the response body should contain the new user
    And the new user should be persisted
  Examples:
    | number | street     | suburb     | city        | postcode |
    | null   | Test1 Road | Testerton1 | Testopolis1 | TST124   |
    |        | Test1 Road | Testerton1 | Testopolis1 | TST124   |
    | 22     | null       | Testerton1 | Testopolis1 | TST124   |
    | 22     |            | Testerton1 | Testopolis1 | TST124   |
    | 22     | Test1 Road | null       | Testopolis1 | TST124   |
    | 22     | Test1 Road |            | Testopolis1 | TST124   |
    | 22     | Test1 Road | Testerton1 | null        | TST124   |
    | 22     | Test1 Road | Testerton1 |             | TST124   |
    | 22     | Test1 Road | Testerton1 | Testopolis1 | null     |
    | 22     | Test1 Road | Testerton1 | Testopolis1 |          |

  Scenario: I create a user with no phone number field and the creation succeeds.
    Given the user has no "phoneNumber" field
    When I create the user
    Then I should receive a status code of 201
    And the user has a "phoneNumber" of "null"
    And the response body should contain the new user
    And the new user should be persisted

  Scenario Outline: I create a user with an address that is missing a field and the creation succeeds.
    Given the user has no "address.<field-name>" field
    When I create the user
    Then I should receive a status code of 201
    And the user has a "address.<field-name>" of <null>
    And the response body should contain the new user
    And the new user should be persisted
  Examples:
    | field-name | null   |
    | number     | null   |
    | street     | "null" |
    | suburb     | "null" |
    | city       | "null" |
    | postcode   | "null" |

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
    And the user has a "address.number" of 22
    And the user has a "address.street" of "Test1 Road"
    And the user has a "address.suburb" of "Testerton1"
    And the user has a "address.city" of "Testopolis1"
    And the user has a "address.postcode" of "TST124"
    And I create the user
    Then I should receive a status code of 400

  Scenario Outline: I create a user with existing name and phone number values and the user is persisted correctly.
    Given I create the user
    When the user has an "email" of "test_two@email.test"
    And the user has a "firstName" of "<first-name>"
    And the user has a "lastName" of "<last-name>"
    And the user has a "phoneNumber" of "<phone_number>"
    And the user has a "address.number" of 22
    And the user has a "address.street" of "Test1 Road"
    And the user has a "address.suburb" of "Testerton1"
    And the user has a "address.city" of "Testopolis1"
    And the user has a "address.postcode" of "TST124"
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

  Scenario Outline: I create a user with existing address values and the user is persisted correctly.
    Given I create the user
    When the user has an "email" of "test_two@email.test"
    And the user has a "firstName" of "Test2"
    And the user has a "lastName" of "User2"
    And the user has a "phoneNumber" of "5551235"
    And the user has a "address.number" of <number>
    And the user has a "address.street" of "<street>"
    And the user has a "address.suburb" of "<suburb>"
    And the user has a "address.city" of "<city>"
    And the user has a "address.postcode" of "<postcode>"
    And I create the user
    Then I should receive a status code of 201
    And the response body should contain the new user
    And the new user should be persisted
  Examples:
    | number | street     | suburb     | city        | postcode |
    | 11     | Test1 Road | Testerton1 | Testopolis1 | TST124   |
    | 22     | Test Road  | Testerton1 | Testopolis1 | TST124   |
    | 22     | Test1 Road | Testerton  | Testopolis1 | TST124   |
    | 22     | Test1 Road | Testerton1 | Testopolis  | TST124   |
    | 22     | Test1 Road | Testerton1 | Testopolis1 | TST123   |
    | 11     | Test Road  | Testerton  | Testopolis  | TST123   |

  Scenario: I create a user with an invalid field and the user is persisted correctly.
    Given the user has an "invalid" of "true"
    When I create the user
    Then I should receive a status code of 201
    And the user has no "invalid" field
    And the response body should contain the new user
    And the new user should be persisted

  Scenario Outline: I create a user with missing values and the creation fails.
    Given the user has an "email" of "<email>"
    And the user has a "firstName" of "<first-name>"
    And the user has a "lastName" of "<last-name>"
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
    Given the user has no "<field-name>" field
    When I create the user
    Then I should receive a status code of 400
  Examples:
    | field-name |
    | email      |
    | firstName  |
    | lastName   |

  Scenario: I create an empty user and the creation fails.
    Given there is a new user
    When I create the user
    Then I should receive a status code of 400