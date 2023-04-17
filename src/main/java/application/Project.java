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

    private void updateLeader(Developer dev, Developer loggedIn)
    {
        if (isProjectLeader(loggedIn))
        {
            projectLeader = dev;
        }
    }

    private void addActivity()
    {
        Activity act = new Activity();
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
        // TODO: implement method
    }

    public Boolean isProjectLeader(Developer dev)
    {
        if (projectLeader == null||projectLeader == dev)
        {
            return true;
        }
        return false;
    }
}
