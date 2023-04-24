Feature: Create Project

Scenario: Developer creates a new project
    Given a developer is logged in
    When the developer creates new project with name "testProj"
    Then the project with name "testProj" is added to the list of projects

Scenario: Developer fails to creates a new project
    Given a developer is logged in
    When the developer creates project without a name
    Then the system outputs "More information needed"