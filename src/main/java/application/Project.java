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

    public void addActivity(String newName, int sDate, int eDate, double hourEst, int nextId)
    {
        Activity act = new Activity(newName, sDate, eDate, hourEst, this, nextId);
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
