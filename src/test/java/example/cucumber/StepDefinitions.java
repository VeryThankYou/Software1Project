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
import application.UserNotLeaderException;
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
	private String message;

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
		developer = logPlan.getSignedIn();
	}

	@When("the developer creates new project with name {string}")
	public void theDeveloperCreatesNewProject(String name)
	{
		try
		{
			logPlan.createProject(name);
		}
		catch (Exception e)
		{
			this.message = e.getMessage();
		}
	}

	@Then("the project with name {string} is added to the list of projects")
	public void theNewProjectIsAddedToTheListOfProjects(String name)
	{
		boolean b = false;
		ArrayList<Project> searchList = logPlan.getProjectList();
		for (int i = 0; i < searchList.size(); i++) 
		{
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
		try
		{
			logPlan.createProject(null);
		}
		catch (Exception e)
		{
			this.message = e.getMessage();
		}
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

		if(project.isProjectLeader(logPlan.getSignedIn()) && project.getProjectLeader() == null)
		{
			try
			{
				Developer dev2 = logPlan.getDeveloper("waca");
				project.updateLeader(dev2, logPlan.getSignedIn());
			}
			catch(Exception e)
			{}
		}
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
		activity.showAvailableDevs(logPlan.getDeveloperList());
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
	public void thereIsAProjectLeader() throws UserNotLeaderException
	{
		if(project.getProjectLeader() == null)
		{
			Developer dev2 = logPlan.getDeveloper("mbic");
			try
			{
				project.updateLeader(dev2, logPlan.getSignedIn());
			}
			catch(UserNotLeaderException e)
			{}
			
		}
		assertTrue(project.getProjectLeader() != null);
	}

	@When("the developer sets themself as project leader")
	public void theDeveloperAddsAProjectLeader() 
	{
		try
		{
			project.updateLeader(developer, logPlan.getSignedIn());
		}
		catch (UserNotLeaderException e)
		{
			message = e.getMessage();
		}
	}

	@Then("the developer is project leader")
	public void theDeveloperIsProjectLeader()
	{
		assertTrue(project.getProjectLeader().getId().equals(developer.getId()));
	}
	
	@When("the developer logs {float} hours worked on the activity")
	public void theDeveloperLogsTheNumberOfHoursWorkedOnAnActivity(Float hours)
	{
		LocalDate date = LocalDate.now();
		try
		{
			developer.markHours(activity, date, (double) hours);
		}
		catch (Exception e)
		{
			this.message = e.getMessage();
		}
	}

	@Then("the system records {float} hours worked on the activity")
	public void theSystemRecordsTheHoursWorkedOnTheActivity(Float hours)
	{
		double doob = hours;
		assertTrue(activity.getActivityCompHours(activity) == doob);
	}

	@When ("the developer does not log any hours worked on the activity")
	public void leaveLogHoursFieldEmpty()
	{
		LocalDate date = LocalDate.now();
		try
		{
			developer.markHours(activity, date, null);
		}
		catch (Exception e)
		{
			this.message = e.getMessage();
		}
	}

	@Then ("the system outputs {string}")
	public void theSystemOutputsAnErrorMessage(String message)
	{
		assertTrue(this.message.equals(message));
	}

    @When("the developer logs {string} hours worked on the {string}")
    public void the_developer_logs_hours_worked_on_the(String s, String s2) {
        // Write code here that turns the phrase above into concrete actions
    }

    @Given("they have selected an activity")
    public void they_have_selected_an_activity() 
	{
        // Write code here that turns the phrase above into concrete actions
		this.activity = new Activity("TestActivity", 0, 0, 0, project, 0);
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

    @When("the developer adds activity with name {string}, enddate {int}, startdate {int} and hour estimate {float} to the project")
    public void the_developer_adds_activity_with_name_enddate_startdate_and_hour_estimate_to_the_project(String s, int i, int i2, float f) {
		try
		{
			activity = new Activity(s, i, i2, (double) f, project, logPlan.getActivityNextId());
			logPlan.addActivityToProject(project, activity);
		}
		catch(UserNotLeaderException e)
		{
			this.message = e.getMessage();
		}
	}


}
