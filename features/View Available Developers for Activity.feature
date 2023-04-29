Feature: View Available Developers for Activity

Scenario: System shows available developers for an activity
	Given there is a project with name "TicTacToe" and id 2022001
	And the developer who is logged in is project leader for the project
	And there are available developers for an activity
	And they have selected an activity 
	When a project leader checks the activity for available developers
	Then the system outputs a list of developers

Scenario: System shows no available developers for an activity
	Given there is a project with name "TicTacToe" and id 2022001
	And the developer who is logged in is project leader for the project
	And there are no available developers for an activity
	When a project leader checks the activity for available developers
	Then the system shows that there are no available developers for the activity