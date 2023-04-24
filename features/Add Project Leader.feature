Feature: Add Project Leader 

Scenario: A developer adds project leader to project
    Given a developer is logged in
    And the project has been created
    And there is no project leader
    When the developer adds an project leader to project 
    Then a project leader is added to project

Scenario: A developer adds project leader to project with a leader
    Given a developer is logged in
    And the project has been created
    And the project has a project leader
    And a different developer has been assigned as project leader 
    When the developer adds an project leader to project
    Then the system outputs an error message