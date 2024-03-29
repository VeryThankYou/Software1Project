Feature: Create Activity in Project

Scenario: Project leader assigns activities to project
    Given there is a project with name "TicTacToe" and id 2022001
    And the developer who is logged in is project leader for the project
    When the developer adds activity with name "testAct", enddate 202310, startdate 202312 and hour estimate 20.0 to the project 
    Then the activity is added to the project

Scenario: Developer fails to assigns activities to project 
    Given a developer is logged in
    And there is a project with name "TicTacToe" and id 2022001
    And the developer who is logged in is not project leader for the project
    And there is a project leader 
    When the developer adds activity with name "testAct", enddate 202310, startdate 202312 and hour estimate 20.0 to the project 
    Then the system outputs "Not project leader error"