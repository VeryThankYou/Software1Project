Feature: Make Project Report

Scenario: Project leader generates a report
    Given the project has been created
    And activities have been assigned to the project
    And the developer who is logged in is project leader for the project
    When clicked on ”Generate Report”
    Then the system generates a report of the hours worked on each activity and the expected rest work on the project.

Scenario: A developer fails to generate a report
    Given the project has been created
    And activities have been assigned to the project
    And the developer who is logged in is not project leader for the project
    And there is a project leader 
    When clicked on ”Generate Report”
    Then the system outputs an error message: "Not project leader error"