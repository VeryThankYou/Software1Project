Feature: Log Hours

Scenario: Developer logs hours on an activity
	Given a developer is logged in
	And they have selected an activity
	When the developer logs "8" hours worked on the "activity"
	Then the system records "8" hours worked on the "activity"


Scenario: Developer fails to log hours on an activity
	Given a developer is logged in
	And they have selected an activity
	When the developer logs "null" hours worked on the "activity"
	Then the system outputs "Error: Hours field is empty"