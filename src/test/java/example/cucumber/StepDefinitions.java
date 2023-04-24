package example.cucumber;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;

public class StepDefinitions {

	private LogPlan logPlan;
	private Activity activity;
	private Developer developer;
	private String devID;
	private Project project;

	public StepDefinitions(LogPlan logplan) throws FileNotFoundException, IOException 
	{
		this.logPlan = new LogPlan();
	}
	@Given("there is a developer with id {string} and name {string}")
	public void thereIsADeveloperWithIdAndName(String id, String name)
	{
		devID = id;
		logPlan.addDeveloper(id, name);
	}

	@Given("a developer is logged in")
	public void theDeveloperIsLoggedIn()
	{
		logPlan.signIn("ebuc");
	}

	@When("the developer creates new project with name {string}")
	public void theDeveloperCreatesNewProject(String name)
	{
		logPlan.createProject(name);
	}

	@Then("the project with name {string} is added to the list of projects")
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

	@When("the developer creates project without a name")
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

	@Given("the developer who is logged in is not project leader for the project")
	public void theLoggedInDeveloperIsNotProjectLeader()
	{
		assertFalse(project.isProjectLeader(logPlan.getSignedIn()));
	}

	@Given("there are available developers for an activity")
	public void thereAreAvailableDevelopersForActivity()
	{
		logPlan.addDeveloper("sjul", "Simon Julendal");
	}

	@When("a project leader checks the activity for available developers")
	public void aProjectLeaderChecksTheActivityForAvailableDevelopers()
	{
		activity.showAvailableDevs(logPlan.getDeveloperList())
	}

	@Then("the system outputs a list of developers")
	public void theSystemOutputsAListOfDevelopers()
	{
		
	}

	@Given("there are no available developers for an activity")
	public void thereAreNoAvailableDevelopersForAnActivity()
	{
	}

	@Then("the system shows that there are no available developers for the activity")
	public void theSystemShowsThatThereAreNoAvailableDevelopersForTheActivity()
	{
		activity.showAvailableDevs(new ArrayList<Developer>());
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

	@Given("there is no project leader")
	public void thereIsNoProjectLeader()
	{
		assertTrue(project.getProjectLeader() == null);
	}

	@Given("there is a project leader")
	public void thereIsAProjectLeader()
	{
		assertTrue(project.getProjectLeader() != null);
	}

	@When("the developer sets themself as project leader")
	public void theDeveloperAddsAProjectLeader() 
	{
		project.updateLeader(developer, logPlan.getSignedIn());
	}

	@Then("the developer is project leader")
	public void theDeveloperIsProjectLeader()
	{
		assertTrue(project.getProjectLeader().getId().equals(developer.getId()));
	}
	
	@When("the developer logs {float} hours worked on the activity")
	public void theDeveloperLogsTheNumberOfHoursWorkedOnAnActivity(Float hours, Activity activity, LocalDate date)
	{
		developer.markHours(activity, date, (double) hours);
	}

	@Then("the system records {float} hours worked on the activity")
	public void theSystemRecordsTheHoursWorkedOnTheActivity(Float hours, Activity activity)
	{
		assertTrue(activity.getActivityCompHours(activity) == hours);
	}

	@When ("the developer does not log any hours worked on the activity")
	public void leaveLogHoursFieldEmpty(LocalDate date, Activity activity)
	{
		developer.markHours(activity, date, null);
	}

	@Then ("the system outputs {string}")
	public void theSystemOutputsAnErrorMessage(String message)
	{
		assertTrue(message == "Error: Hours field is empty");
	}

    @When("the developer logs {string} hours worked on the {string}")
    public void the_developer_logs_hours_worked_on_the(String s, String s2) {
        // Write code here that turns the phrase above into concrete actions
    }

    @Given("they have selected an activity")
    public void they_have_selected_an_activity(Activity activity) {
        // Write code here that turns the phrase above into concrete actions

		activity = new Activity(devID, 0, 0, 0, project, 0);
    }

    @When("clicked on ”Generate Report”")
    public void clicked_on_Generate_Report() {
        // Write code here that turns the phrase above into concrete actions
		project.makeReport();
    }

    @Given("activities have been assigned to the project")
    public void activities_have_been_assigned_to_the_project() {
        // Write code here that turns the phrase above into concrete actions
		activity = new Activity(devID, 0, 0, 0, project, 0);
		project.addActivity(activity);
    }

    @Given("the project has been created")
    public void the_project_has_been_created() {
        // Write code here that turns the phrase above into concrete actions
		project = new Project(0, devID);


    }

    @Then("the system outputs an error message: {string}")
    public void the_system_outputs_an_error_message(String message) {
        // Write code here that turns the phrase above into concrete actions
		assertTrue(message == "Error: Not Allowed. You are not the project leader");

    }


}
