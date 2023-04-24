Feature: Add Project Leader 

Scenario: A developer adds project leader to project
    Given a developer is logged in
    And there is a project with name "TicTacToe" and id 2022001
    And there is no project leader
    When the developer sets themself as project leader 
    Then the developer is project leader

Scenario: A developer adds project leader to project with a leader
    Given a developer is logged in
    And there is a project with name "TicTacToe" and id 2022001
    And there is a project leader
    And the developer who is logged in is not project leader for the project 
    When the developer sets themself as project leader
    Then the system outputs "Not Project Leader Error"