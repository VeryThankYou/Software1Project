Feature: Make Project Report

Scenario: Project leader generates a report
    Given a project leader is logged in
    And the project has been created
    And activities have been assigned to the project
    When the project leader click on ”Generate Report”
    Then the system generates a report of the hours worked on each activity and the expected rest work on the project.

Scenario: A developer fails to generate a report
    Given a developer is logged in
    And the project has been created
    And a different developer has been assigned as project leader 
    And activities have been assigned to the project
    When the developer click on ”Generate Report”
    Then the system outputs an error message