Feature: User - Update

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

  Scenario Outline: I update a user and the user is updated correctly.
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

  Scenario Outline: I update a users address and the user is updated correctly.
    Given the user has a "address.number" of <number>
    And the user has a "address.street" of "<street>"
    And the user has a "address.suburb" of "<suburb>"
    And the user has a "address.city" of "<city>"
    And the user has a "address.postcode" of "<postcode>"
    When I update the user
    Then I should receive a status code of 200
    And the response body should contain the updated user
    And the user should be updated
  Examples:
    | number | street     | suburb     | city        | postcode |
    | 33     | Test1 Road | Testerton1 | Testopolis1 | TST124   |
    | 22     | Test2 Road | Testerton1 | Testopolis1 | TST124   |
    | 22     | Test1 Road | Testerton2 | Testopolis1 | TST124   |
    | 22     | Test1 Road | Testerton1 | Testopolis2 | TST124   |
    | 22     | Test1 Road | Testerton1 | Testopolis1 | TST125   |
    | 33     | Test2 Road | Testerton2 | Testopolis2 | TST125   |

  Scenario Outline: I update a user with existing name and phone number values and the user is updated correctly.
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

  Scenario Outline: I update a users address with the existing values and the user is updated correctly.
    Given the user has a "address.number" of <number>
    And the user has a "address.street" of "<street>"
    And the user has a "address.suburb" of "<suburb>"
    And the user has a "address.city" of "<city>"
    And the user has a "address.postcode" of "<postcode>"
    When I update the user
    Then I should receive a status code of 200
    And the response body should contain the updated user
    And the user should be updated
  Examples:
    | number | street     | suburb     | city        | postcode |
    | 11     | Test1 Road | Testerton1 | Testopolis1 | TST124   |
    | 22     | Test Road  | Testerton1 | Testopolis1 | TST124   |
    | 22     | Test1 Road | Testerton  | Testopolis1 | TST124   |
    | 22     | Test1 Road | Testerton1 | Testopolis  | TST124   |
    | 22     | Test1 Road | Testerton1 | Testopolis1 | TST123   |
    | 11     | Test Road  | Testerton  | Testopolis  | TST123   |

  Scenario Outline: I update a user with the no phone number value and the user is updated correctly.
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

  Scenario Outline: I update a users address with empty values and the user is updated correctly.
    Given the user has a "address.number" of <number>
    And the user has a "address.street" of "<street>"
    And the user has a "address.suburb" of "<suburb>"
    And the user has a "address.city" of "<city>"
    And the user has a "address.postcode" of "<postcode>"
    When I update the user
    Then I should receive a status code of 200
    And the response body should contain the updated user
    And the user should be updated
  Examples:
    | number | street     | suburb     | city        | postcode |
    | null   | Test2 Road | Testerton2 | Testopolis2 | TST125   |
    |        | Test2 Road | Testerton2 | Testopolis2 | TST125   |
    | 33     | null       | Testerton2 | Testopolis2 | TST125   |
    | 33     |            | Testerton2 | Testopolis2 | TST125   |
    | 33     | Test2 Road | null       | Testopolis2 | TST125   |
    | 33     | Test2 Road |            | Testopolis2 | TST125   |
    | 33     | Test2 Road | Testerton2 | null        | TST125   |
    | 33     | Test2 Road | Testerton2 |             | TST125   |
    | 33     | Test2 Road | Testerton2 | Testopolis2 | null     |
    | 33     | Test2 Road | Testerton2 | Testopolis2 |          |
    | 33     | Test2 Road | Testerton2 | Testopolis2 | TST125   |

  Scenario: I update a user with the no phone number field and the user is updated correctly.
    Given the user has no "phoneNumber" field
    When I update the user
    Then I should receive a status code of 200
    And the user has a "phoneNumber" of "null"
    And the response body should contain the updated user
    And the user should be updated

  Scenario Outline: I update a users address with missing values and the user is updated correctly.
    Given the user has no "address.<field-name>" field
    When I update the user
    Then I should receive a status code of 200
    And the user has a "address.<field-name>" of <null>
    And the response body should contain the updated user
    And the user should be updated
  Examples:
    | field-name | null   |
    | number     | null   |
    | street     | "null" |
    | suburb     | "null" |
    | city       | "null" |
    | postcode   | "null" |

  Scenario: I update a user with an existing email and the update fails.
    Given the user has an "email" of "test_one@email.test"
    And the user has a "firstName" of "Test2"
    And the user has a "lastName" of "User2"
    And the user has a "phoneNumber" of "5551235"
    When I update the user
    Then I should receive a status code of 400

  Scenario Outline: I update a user with missing values and the update fails.
    Given the user has an "email" of "<email>"
    And the user has a "firstName" of "<first-name>"
    And the user has a "lastName" of "<last-name>"
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

  Scenario Outline: I update a user with missing fields and the update fails.
    Given the user has no "<field-name>" field
    When I update the user
    Then I should receive a status code of 400
  Examples:
    | field-name |
    | email      |
    | firstName  |
    | lastName   |

  Scenario: I update a user with an empty user and the update fails.
    Given there is a new user
    When I update the user
    Then I should receive a status code of 400