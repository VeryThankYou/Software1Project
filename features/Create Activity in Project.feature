Feature: Create Activity in Project

Scenario: Project leader assigns activities to project
    Given a project leader is logged in
    And the project has been created
    When the project leader assigns activities to the project 
    Then the system outputs an error message

Scenario: Developer fails to assigns activities to project
    Given a developer is logged in
    And the project has been created
    And a different developer has been assigned as project leader 
    When the developer assigns activities to the project
    Then the system outputs an error message