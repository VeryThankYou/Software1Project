package example.cucumber;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.picocontainer.behaviors.Storing;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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

	@When("a developer is added to an activity")
	public void addDevToAct()
	{
		
	}
	
}
