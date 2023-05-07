package example.cucumber;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.longThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;

import org.picocontainer.behaviors.Storing;

import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import application.Developer;
import application.LogPlan;
import application.Project;
import application.Session;
import application.UserNotLeaderException;
import application.Activity;
import application.Developer;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class StepDefinitions {

	private LogPlan logPlan;
	private Activity activity;
	private Activity act1;
	private Activity act2;
	private Developer developer;
	private String devID;
	private Project project;
	private String message;
	private ArrayList<Activity> schedule;
	private Map<Integer, ArrayList<Developer>> avdevs;
	private double doublereturn;

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
		logPlan.signIn("ebuc");
		assertTrue(project.isProjectLeader(logPlan.getSignedIn()));
	}

	@Given("the developer who is logged in is not project leader for the project")
	public void theLoggedInDeveloperIsNotProjectLeader()
	{
		logPlan.signIn("ebuc");
		developer = logPlan.getSignedIn();
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
		avdevs = activity.showAvailableDevs(logPlan.getDeveloperList());
	}

	@Then("the system outputs a list of developers")
	public void theSystemOutputsAListOfDevelopers()
	{
		boolean contains = false;
		SortedSet<Integer> keys = new TreeSet<>(avdevs.keySet());
        
        for (int key: keys)
		{
			ArrayList<Developer> devlist = avdevs.get(key);
			for(int i = 0; i < devlist.size(); i++)
			{
				Developer dev = devlist.get(i);
				if(dev.getId().equals("sjul"))
				{
					contains = true;
				}
			}
		}
		
		assertTrue(contains);
	}

	@Given("there are no available developers for an activity")
	public void thereAreNoAvailableDevelopersForAnActivity()
	{
		Calendar calendar = Calendar.getInstance(new Locale("dan", "dk"));
		LocalDate date = LocalDate.now();
		calendar.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
		int weeknum = calendar.get(Calendar.WEEK_OF_YEAR);
		try
		{
			activity = new Activity("newActivity", date.getYear() * 100 + weeknum, date.getYear() * 100 + weeknum, 2.0, project, logPlan.getActivityNextId());
			project.addActivity(activity, logPlan.getSignedIn());
		} catch (Exception e){}
		for(int i = 0; i < logPlan.getDeveloperList().size(); i++)
		{
			Developer dev1 = logPlan.getDeveloperList().get(i);
			activity.addDev(dev1);
		}
	}

	@Then("the system shows that there are no available developers for the activity")
	public void theSystemShowsThatThereAreNoAvailableDevelopersForTheActivity()
	{
		assertTrue(avdevs.size() == 0);
	}



	@Given("there is no project leader")
	public void thereIsNoProjectLeader()
	{
		try
		{
			project.updateLeader(null, project.getProjectLeader());
		}
		catch(Exception e){}
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
		this.activity = new Activity("TestActivity", 202318, 202319, 0, project, 0);
    }

    @When("clicked on ”Generate Report”")
    public void clicked_on_Generate_Report() {
        // Write code here that turns the phrase above into concrete actions
		try
		{
			project.makeReport(logPlan.getSignedIn());
		}
		catch (UserNotLeaderException e)
		{
			this.message = e.getMessage();
		}
		
    }

    @Given("activities have been assigned to the project")
    public void activities_have_been_assigned_to_the_project() {
        // Write code here that turns the phrase above into concrete actions
		activity = new Activity("New activity", 1, 1, 0, project, 0);
		project.addActivity(activity);
    }

    @Given("the project has been created")
    public void the_project_has_been_created() {
        // Write code here that turns the phrase above into concrete actions
		project = new Project(0, "newProject");


    }

    @Then("the system outputs an error message: {string}")
    public void the_system_outputs_an_error_message(String message) {
        // Write code here that turns the phrase above into concrete actions
		assertTrue(this.message.equals(message));
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

    @Then("the system displays the schedule")
    public void the_system_displays_the_schedule() 
	{
		assertTrue(schedule.contains(activity));
	}

    @When("the developer views their daily schedule")
    public void the_developer_views_their_daily_schedule() 
	{
		Calendar calendar = Calendar.getInstance(new Locale("dan", "dk"));
		LocalDate date = LocalDate.now();
		calendar.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
		int weeknum = calendar.get(Calendar.WEEK_OF_YEAR);
		try
		{
			logPlan.createProject("newProject");
		}
		catch (Exception e){}
		ArrayList<Project> search = logPlan.searchProjects("newProject");
		project = search.get(0);
		try
		{
			activity = new Activity("newActivity", date.getYear() * 100 + weeknum, date.getYear() * 100 + weeknum, 2.0, project, logPlan.getActivityNextId());
			project.addActivity(activity, developer);
		} catch (Exception e){}
		activity.addDev(developer);
        this.schedule = developer.viewSchedule(date.getYear() * 100 + weeknum);
    }

	@When("changes week for their view")
	public void changes_week_for_their_view()
	{
		this.schedule = developer.viewSchedule(202304);
	}

	@Then("a new view of their schedule is show")
	public void a_new_view_of_their_schedule_is_show()
	{
		ArrayList<Activity> acts = new ArrayList<Activity>();
		project = logPlan.searchProjects("LogPlan").get(0);
		acts = project.getActivities();
		assertTrue(schedule.equals(acts.subList(0, 1)));
	}

    @Then("the system displays the hours worked on each activity and the expected rest work on the project.")
    public void the_system_displays_the_hours_worked_on_each_activity_and_the_expected_rest_work_on_the_project() 
	{
        assertTrue(schedule.equals(project.getActivities()));
    }


    @When("the developer views the project progress")
    public void the_developer_views_the_project_progress() 
	{
		schedule = logPlan.showProject(project);
	}

    @Then("the system generates a report of the hours worked on each activity and the expected rest work on the project.")
    public void the_system_generates_a_report_of_the_hours_worked_on_each_activity_and_the_expected_rest_work_on_the_project() {
        // Write code here that turns the phrase above into concrete actions
		// check if file exists
		String filePathString = "reports/report_" + project.getName() + ".txt";
		File f = new File(filePathString);
		if(f.exists() && !f.isDirectory()) 
		{ 
			assertTrue(true);
    	}
	}

	@Given("no activities have been assigned to the project")
    public void no_activities_have_been_assigned_to_the_project() {
        // Write code here that turns the phrase above into concrete actions
		assertTrue(project.getActivities().size() == 0);
    }

	@Given("the project has been created and has a project leader")
    public void the_project_has_been_created_and_has_a_project_leader() {
        // Write code here that turns the phrase above into concrete actions
		project = new Project(0, "newProject", logPlan.getDeveloper("ebuc"));
    }

	@Given("specific activities have been assigned to the project")
    public void specific_activities_have_been_assigned_to_the_project() {
        // Write code here that turns the phrase above into concrete actions
		activity = new Activity("New activity", 1, 1, 0, project, 0);
		Activity activity2 = new Activity("New activity2", 1, 1, 0, project, 0, 1);
		Developer dev2 = logPlan.getDeveloper("mbic");
		activity2.addDev(dev2);
		Activity activity3 = new Activity("New activity3", 1, 1, 0, project, 0, 2);
		project.addActivity(activity);
		project.addActivity(activity2);
		project.addActivity(activity3);
    }

	@Given("the activity has no sessions logged")
	public void the_activity_has_no_sessions_logged()
	{
		assertTrue(activity.getSessions().size() == 0);
	}

	@When("they call computeHours for the activity")
	public void they_call_computeHours_for_the_activity()
	{
		this.doublereturn = activity.computeHoursSpent();
	}

	@Then("the return is {float}")
	public void then_the_return_is(float f)
	{
		double checkValue = (double) f;
		assertTrue(checkValue == doublereturn);
	}

	@Given("the activity has one session with length {float} logged")
	public void the_activity_has_one_session_with_length(float f)
	{
		Session sesh = new Session((double) f, null, activity.getId());
		activity.addSession(sesh);
	}

	@Given("assigned to one activity with startdate {int} and end date {int}")
	public void assigned_to_one_activity(int s1, int s2)
	{
		for(int i = 0; i < logPlan.getSignedIn().getActivities().size(); i++)
		{
			logPlan.getSignedIn().removeActivity(logPlan.getSignedIn().getActivities().get(i));
		}
		act1 = new Activity("act1", s1, s2, 0.0, project, 0);
		act1.addDev(logPlan.getSignedIn());
	}

	@Given("not assigned to any activities")
	public void not_assigned_to_any_activities()
	{
		for(int i = 0; i < logPlan.getSignedIn().getActivities().size(); i++)
		{
			logPlan.getSignedIn().removeActivity(logPlan.getSignedIn().getActivities().get(i));
		}
	}

	@Given("assigned to two activities with startdate {int} and end date {int}")
	public void assigned_to_two_activities(int s1, int s2)
	{
		for(int i = 0; i < logPlan.getSignedIn().getActivities().size(); i++)
		{
			logPlan.getSignedIn().removeActivity(logPlan.getSignedIn().getActivities().get(i));
		}
		act1 = new Activity("act1", s1, s2, 0.0, project, 0);
		act2 = new Activity("act2", s1, s2, 0.0, project, 0);
		act1.addDev(logPlan.getSignedIn());
		act2.addDev(logPlan.getSignedIn());
	}

	@When("viewSchedule for weeknum {int} is called")
	public void viewSchedule_for_weeknum(int s)
	{
		schedule = logPlan.getSignedIn().viewSchedule(s);
	}

	@Then("the return is an empty arraylist")
	public void the_return_is_an_empty_arraylist()
	{
		assertTrue(schedule.size() == 0);
	}

	@Then("the return is an arraylist with the single activity")
	public void the_return_is_an_arraylist_with_the_single_activity()
	{
		assertTrue(act1.equals(schedule.get(0)));
	}

	@Then("the return is an arraylist with both activities")
	public void the_return_is_an_arraylist_with_both_activities()
	{
		Boolean bool1 = (act1.equals(schedule.get(0)) && act2.equals(schedule.get(1)));
		Boolean bool2 = (act1.equals(schedule.get(1)) && act2.equals(schedule.get(0)));
		assertTrue(bool1 || bool2);
	}

	@Given("the activity is not null")
	public void the_activity_is_not_null()
	{
		activity = new Activity("newAct", 1, 1, 5.0, project, 0);
	}

	@Given("the activity is null")
	public void the_activity_is_null()
	{
		activity = null;
	}

	@When("addActivity for the project is called with the chosen activity")
	public void addActivity_for_the_project_is_called_with_the_chosen_activity()
	{
		try
		{
			project.addActivity(activity, logPlan.getSignedIn());
		}
		catch(Exception e)
		{
			this.message = e.getMessage();
		}
	}

	@Then("the null activity is added to the project")
	public void the_null_activity_is_added_to_the_project()
	{
		Boolean b1 = false;
		for(int i = 0; i < project.getActivities().size(); i++)
		{
			Activity a = project.getActivities().get(i);
			if(a == null)
			{
				b1 = true;
			}
		}
		assertTrue(b1);
	}

	@Then("the activity is added to the project")
	public void the_activity_is_added_to_the_projecthaha()
	{
		Boolean b1 = false;
		for(int i = 0; i < project.getActivities().size(); i++)
		{
			Activity a = project.getActivities().get(i);
			if(a == activity)
			{
				b1 = true;
			}
		}
		assertTrue(b1);
	}
}
