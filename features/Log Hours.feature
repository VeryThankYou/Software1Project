Feature: Log Hours

Scenario: Developer logs hours on an activity
	Given a developer is logged in
	And they have selected an activity
	When the developer logs the number of hours worked on the activity
	Then the system records the hours worked on the activity


Scenario: Developer fails to log hours on an activity
	Given a developer is logged in
	And they have selected an activity
	When leaves the log hours field empty
	Then the system outputs an error message