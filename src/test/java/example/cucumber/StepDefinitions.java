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
	private String devID;
	private Project project;

	@Given("there is a developer with id {string} and name {string")
	public void thereIsADeveloperWithIdAndName(String id, String name)
	{
		devID = id;
		logPlan.addDeveloper(id, name);
	}

	@Given("a developer is logged in")
	public void theDeveloperIsLoggedIn()
	{
		logPlan.signIn(devID);
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

	@When("the developer creates project without a name {null}")
	public void theDeveloperCreatesProjectWithoutAName()
	{
		logPlan.createProject(null);
	}

	@Given("there is a project with name {string} and id {int}")
	public void thereIsAProjectWithNameAndId(String name, int id)
	{
		boolean b = false;
		ArrayList<Project> projList = logPlan.getProjectList();
		for (int i = 0; i < projList.size(); i++) 
		{
			if (projList.get(i).getId() == id && projList.get(i).getName().equals(name)) 
			{
				b = true;
				project = projList.get(i);
			}
		}
		assertTrue(b);
	}

	@Given("the developer who is logged in is project leader for the project")
	public void theLoggedInDeveloperIsProjectLeader()
	{
		assertTrue(project.isProjectLeader(logPlan.getSignedIn()));
	}

	@When("the developer adds activity with name {String}, enddate {int}, startdate {int} and hour estimate {double} to the project")
	public void theDeveloperAddsActivityToTheProject(String name, int eDate, int sDate, double hourEst)
	{
		project.addActivity(name, eDate, sDate, hourEst);
	}

	@Then("the activity is added to the project")
	public void theActivityIsAddedToTheProject()
	{
		boolean b = false;
		ArrayList<Activity> actList = project.getActivities();
		for (int i = 0; i < actList.size(); i++) 
		{
			if (actList.get(i).getId() == activity.getId()) 
			{
				b = true;
			}
		}
		
		assertTrue(b);
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

    @When("the developer logs {string} hours worked on the {string}")
    public void the_developer_logs_hours_worked_on_the(String s, String s2) {
        // Write code here that turns the phrase above into concrete actions
    }

}
