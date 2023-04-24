Feature: Make Project Report

Scenario: Project leader generates a report
    Given the developer who is logged in is project leader for the project
    And the project has been created
    And activities have been assigned to the project
    When clicked on ”Generate Report”
    Then the system generates a report of the hours worked on each activity and the expected rest work on the project.

Scenario: A developer fails to generate a report
    Given the developer who is logged in is not project leader for the project
    And the project has been created
    And there is a project leader 
    And activities have been assigned to the project
    When clicked on ”Generate Report”
    Then the system outputs an error message: "Error: Not Allowed. You are not the project leader"