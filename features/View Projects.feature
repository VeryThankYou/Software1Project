Feature: View Projets

Scenario: Project leader views project progress
    Given a project leader is logged in
    And the project has been created
    And activities have been assigned to the project
    When the project leader views the project progress
    Then the system displays the hours worked on each activity and the expected rest work on the project.

Scenario: Developer views project progress
    Given a developer is logged in
    And the project has been created
    And activities have been assigned to the project
    When the developer views the project progress
    Then the system displays the hours worked on each activity and the expected rest work on the project.