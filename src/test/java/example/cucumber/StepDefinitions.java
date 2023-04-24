package example.cucumber;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.picocontainer.behaviors.Storing;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import application.Developer;
import application.LogPlan;

public class StepDefinitions {

	private LogPlan logPlan;

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

	@When("a developer is added to an activity")
	public void addDevToAct()
	{
		
	}
	
}
