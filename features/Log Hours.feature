Feature: Log Hours

Scenario: Developer logs hours on an activity
	Given a developer is logged in
	And they have selected an activity
	When the developer logs 8.0 hours worked on the activity
	Then the system records 8.0 hours worked on the activity



Scenario: Developer fails to log hours on an activity
	Given a developer is logged in
	And they have selected an activity
	When the developer does not log any hours worked on the activity
	Then the system outputs "Error: Hours field is empty"