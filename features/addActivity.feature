Feature: addActivity Test

Scenario: Test 1
    Given there is a project with name "TicTacToe" and id 2022001
    And the developer who is logged in is not project leader for the project
    And the activity is null
    When addActivity for the project is called with the chosen activity
    Then the system outputs "Not project leader error"

Scenario: Test 2
    Given a developer is logged in
    And there is a project with name "TicTacToe" and id 2022001
    And the developer who is logged in is project leader for the project
    And the activity is null
    When addActivity for the project is called with the chosen activity
    Then the null activity is added to the project

Scenario: Test 3
    Given there is a project with name "TicTacToe" and id 2022001
    And the developer who is logged in is not project leader for the project
    And the activity is not null
    When addActivity for the project is called with the chosen activity
    Then the system outputs "Not project leader error"

Scenario: Test 4
    Given a developer is logged in
    And there is a project with name "TicTacToe" and id 2022001
    And the developer who is logged in is project leader for the project
    And the activity is not null
    When addActivity for the project is called with the chosen activity
    Then the activity is added to the project