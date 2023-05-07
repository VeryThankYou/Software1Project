Feature: Test computeHours

Scenario: Test 1
Given a developer is logged in
And they have selected an activity
And the activity has no sessions logged
When they call computeHours for the activity
Then the return is 0.0

Scenario: Test 2
Given a developer is logged in
And they have selected an activity
And the activity has one session with length 2.0 logged
When they call computeHours for the activity
Then the return is 2.0

Scenario: Test 2
Given a developer is logged in
And they have selected an activity
And the activity has one session with length 1.5 logged
And the activity has one session with length 4.5 logged
When they call computeHours for the activity
Then the return is 6.0