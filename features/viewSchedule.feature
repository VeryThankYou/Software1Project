Feature: viewSchedule Test

Scenario: Test 1
	Given a developer is logged in
    And not assigned to any activities
	When viewSchedule for weeknum 202311 is called
	Then the return is an empty arraylist

Scenario: Test 2
	Given a developer is logged in
    And assigned to one activity with startdate 202318 and end date 202320
	When viewSchedule for weeknum 202311 is called
	Then the return is an empty arraylist

Scenario: Test 3
	Given a developer is logged in
    And assigned to one activity with startdate 202318 and end date 202320
	When viewSchedule for weeknum 202319 is called
	Then the return is an arraylist with the single activity

Scenario: Test 4
	Given a developer is logged in
    And assigned to two activities with startdate 202318 and end date 202320
	When viewSchedule for weeknum 202319 is called
	Then the return is an arraylist with both activities