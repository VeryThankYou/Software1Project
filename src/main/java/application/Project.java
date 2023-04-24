package application;
import java.util.ArrayList; 

public class Project 
{
    private String name;
    private int projectID;
    private Developer projectLeader;
    private ArrayList<Activity> activities;

    public Project(int id, String newName)
    {
        name = newName;
        projectID = id;
    }

    public Project(int id, String newName, Developer leader)
    {
        name = newName;
        projectID = id;
        projectLeader = leader;
    }

    public void updateLeader(Developer dev, Developer loggedIn)
    {
        if (isProjectLeader(loggedIn))
        {
            projectLeader = dev;
        }
    }

    public void addActivity(String newName, int sDate, int eDate, double hourEst)
    {
        Activity act = new Activity(newName, sDate, eDate, hourEst, this);
        activities.add(act);
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
        double workedHours = 0;
        for (int i = 0; i < activities.size(); i++)
        {
            workedHours = workedHours + activities.get(i).computeHoursSpent();
        }
        System.out.println(workedHours);
    }

    public Boolean isProjectLeader(Developer dev)
    {
        if (projectLeader == null||projectLeader == dev)
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
