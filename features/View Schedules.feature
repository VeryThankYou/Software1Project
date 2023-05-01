Feature: View Schedules

Scenario: Developer views their daily schedule
	Given a developer is logged in
	When the developer views their daily schedule
	Then the system displays the schedule

Scenario: Developer changes week for their view of schedule
	Given a developer is logged in
	When the developer views their daily schedule
	And changes week for their view
	Then a new view of their schedule is show
	