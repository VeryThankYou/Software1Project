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
        System.out.println("Project Report for " + name);

        // project id
        System.out.println("Project ID: " + projectID);

        // project leader

        if (projectLeader == null)
        {
            System.out.println("Project Leader: None");

        }
        else 
        {
            System.out.println("Project Leader: " + projectLeader.getName());
        }

        if (activities.size() == 0)

        {
            System.out.println("No activities in project");
        }


        else
        {

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
            System.out.println("Hours spent on activity: ");
            for (int i = 0; i < activities.size(); i++)
            {
            System.out.println("          " + (i+1) + ") " + activities.get(i).getName() + ": " + activities.get(i).computeHoursSpent());
            }

        }
            // Developer list
            for (int i = 0; i < activities.size(); i++)
            {
                if (activities.get(i).getDeveloperList().size() == 0)
                {
                    System.out.println("No developers assigned to the activity " + activities.get(i).getName());
                }
                else
                {
                    System.out.println("Developers assigned to the activity " +  activities.get(i).getName() + ": ");
                    for (int i2 = 0; i2 < activities.get(i).getDeveloperList().size(); i2++)
                    {
                        System.out.println("          " + (i2+1) + ") " + activities.get(i).getDeveloperList().get(i2).getName());
                    }
                }
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
