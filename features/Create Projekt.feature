Feature:

Scenario: Developer creates a new project
    Given a developer is logged in
    When they create a new project
    And they enter the project details
    Then the new project should be created 
    And added to the list of active projects

Scenario: Developer fails to creates a new project
    Given a developer is logged in
    When they create a new project
    And leaves the project details empty Then the system outputs an error message