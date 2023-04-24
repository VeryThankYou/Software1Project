Feature: View Available Developers for Activity

Scenario: System shows available developers for an activity
	Given the developer who is logged in is project leader for the project
	And the project has been created
	And there are available developers for an activity
	When a project leader selects an activity for the project
	Then the system shows the available developers for the activity

Scenario: System shows no available developers for an activity
	Given the developer who is logged in is project leader for the project
	And the project has been created
	And there are no available developers for an activity
	When a project leader selects an activity for the project 
	Then the system shows that there are no available developers for the activity