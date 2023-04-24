Feature: View Schedules

Scenario: Developer views their daily schedule
	Given a developer is logged in
	When the developer views their daily schedule
	Then the system displays the schedule