package example.cucumber;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.picocontainer.behaviors.Storing;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import application.Developer;
import application.LogPlan;
import application.Project;
import application.Activity;
import application.Developer;
import java.time.LocalDate;

public class StepDefinitions {

	private LogPlan logPlan;
	private Activity activity;
	private Developer developer;

	@When("I do nothing")
	public void iDoNothing() {
	}

	@Then("everything is okay")
	public void everythingIsOkay() {
		assertTrue(true);
	}

	@Given("there is a developer with id {string} and name {string")
	public void thereIsADeveloperWithIdAndName(String id, String name)
	{
		logPlan.addDeveloper(id, name);
	}

	@Given("the developer with id {String} logs in")
	public void theDeveloperIsLoggedIn(String id)
	{
		logPlan.signIn(id);
	}

	@When("the developer creates new project with name {name}")
	public void theDeveloperCreatesNewProject(String name)
	{
		logPlan.createProject(name);
	}

	@Then("the project with name {String} is added to the list of projects")
	public void theNewProjectIsAddedToTheListOfProjects(String name)
	{
		boolean b = false;
		ArrayList<Project> searchList = logPlan.searchProjects(name);
		for (int i = 0; i < searchList.size(); i++) {
			if (searchList.get(i).getName().equals(name) ) 
			{
				b = true;
			}
		}
		assertTrue(b);
	}

	@When("a developer is added to an activity")
	public void addDevToAct()
	{
		
	}
	
	@When("the developer logs the number of hours {double} worked on an activity {Activity}")
	public void theDeveloperLogsTheNumberOfHoursWorkedOnAnActivity(double hours, Activity activity, LocalDate date)
	{
		developer.markHours(activity, date, hours);
	}

	@Then("the system records the hours {double} worked on the activity {Activity}")
	public void theSystemRecordsTheHoursWorkedOnTheActivity(double hours, Activity activity)
	{
		assertTrue(activity.getActivityCompHours(activity) == hours);
	}

	@When ("leaves the log hours field empty {null}")
	public void leaveLogHoursFieldEmpty(LocalDate date)
	{
		developer.markHours(activity, date, null);
	}

	@Then (" the system outputs an error message {string}")
	public void theSystemOutputsAnErrorMessage(String message)
	{
		assertTrue(message == "Error: Hours field is empty");
	}

}
