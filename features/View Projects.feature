Feature: View Projects

Scenario: Project leader views project progress
    Given there is a project with name "LogPlan" and id 2023001
    And the developer who is logged in is project leader for the project
    And activities have been assigned to the project
    When the developer views the project progress
    Then the system displays the hours worked on each activity and the expected rest work on the project.

Scenario: Developer views project progress
    Given there is a project with name "LogPlan" and id 2023001
    And a developer is logged in
    And activities have been assigned to the project
    When the developer views the project progress
    Then the system displays the hours worked on each activity and the expected rest work on the project.