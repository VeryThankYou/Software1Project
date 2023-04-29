package application;
import java.util.ArrayList;

import io.cucumber.java.tlh.vaj; 

public class Project 
{
    private String name;
    private int projectID;
    private Developer projectLeader;
    private ArrayList<Activity> activities;

    public Project(int id, String newName)
    {
        this.name = newName;
        this.projectID = id;
        this.activities = new ArrayList<Activity>();
        this.projectLeader = null;
    }

    public Project(int id, String newName, Developer leader)
    {
        this.name = newName;
        this.projectID = id;
        this.projectLeader = leader;
        this.activities = new ArrayList<Activity>();
    }

    public void updateLeader(Developer dev, Developer loggedIn) throws UserNotLeaderException
    {
        if (isProjectLeader(loggedIn))
        {
            this.projectLeader = dev;
        }
        throw new UserNotLeaderException("Not Project Leader Error");
    }

    public void addActivity(String newName, int sDate, int eDate, double hourEst, int nextId, Developer loggedIn) throws UserNotLeaderException
    {
        if(isProjectLeader(loggedIn))
        {
            Activity act = new Activity(newName, sDate, eDate, hourEst, this, nextId);
            activities.add(act);
        }
        throw new UserNotLeaderException("Not project leader error");
    }

    public void addActivity(Activity act, Developer loggedIn) throws UserNotLeaderException
    {
        if(isProjectLeader(loggedIn))
        {
            activities.add(act);
        }
        throw new UserNotLeaderException("Not project leader error");
    }

    public void addActivity(Activity act)
    {
        activities.add(act);
    }

    private void updateName(Developer loggedIn, String newName)
    {
        if (isProjectLeader(loggedIn))
        {
            name = newName;
        }
    }

    public void makeReport()
    {

        // Project report - name of project
        System.out.println("Project report for " + name);

        // project id
        System.out.println("Project ID: " + projectID);

        // project leader
        System.out.println("Project Leader: " + projectLeader.getName());

        // start date (from activity)
        System.out.println("Start Date: " + activities.get(0).getEndDate());

        // end date (from activity)
        System.out.println("End Date: " + activities.get(0).getEndDate());

        // hours estimated for project

        double estHours = 0;
        for (int i = 0; i < activities.size(); i++)
        {
            estHours = estHours + activities.get(i).getHourEstimate();
        }
        System.out.println("Hours estimated for project: " + estHours);

        // hours spent on project
        double workedHours = 0;
        for (int i = 0; i < activities.size(); i++)
        {
            workedHours = workedHours + activities.get(i).computeHoursSpent();
        }
        System.out.println("Hours spent on project: " + workedHours);

        // hours spent on each activity
        for (int i = 0; i < activities.size(); i++)
        {
            System.out.println("Hours spent on activity " + activities.get(i).getName() + ": " + activities.get(i).computeHoursSpent());
        }

    }

    public Boolean isProjectLeader(Developer dev)
    {
        if (this.projectLeader == null||projectLeader.getId().equals(dev.getId()))
        {
            return true;
        }
        return false;
    }

    public String getName()
    {
        return name;
    }

    public int getId()
    {
        return projectID;
    }

    public ArrayList<Activity> getActivities()
    {
        return activities;
    }

    public Developer getProjectLeader()
    {
        return projectLeader;
    }
}
