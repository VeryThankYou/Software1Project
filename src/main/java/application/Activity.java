package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity
{
    private Project project;
    private String name;
    private int startDate;
    private int endDate;
    private double hourEstimate;
    private ArrayList<Developer> devs = new ArrayList<>();
    private ArrayList<Session> sessions = new ArrayList<>();
    private int processInfo;
    private int id;


    public Activity(String name, int sDate, int eDate, double hourEst, Project newProject, int id)
    {
        this.name = name;
        this.startDate = sDate;
        this.endDate = eDate;
        this.hourEstimate = hourEst;
        this.project = newProject;
        this.id = id;
    }

    public Activity(String name, int sDate, int eDate, double hourEst, Project newProject, int id, int proc)
    {
        this.name = name;
        this.startDate = sDate;
        this.endDate = eDate;
        this.hourEstimate = hourEst;
        this.project = newProject;
        this.id = id;
    }

    public void addDev(Developer dev)
    {
        if (devs.contains(dev) == false) 
            {
            devs.add(dev);
            }
    }


    public void addSession(Session session)
    {
        sessions.add(session);
    }

    public int[] getDates()
    {
        int[] out = {startDate, endDate};
        return out;
    }

    public double computeHoursSpent()
    {
        double hoursSpent = 0; 
        for(int i = 0; i < sessions.size(); i++)
        {
            hoursSpent += sessions.get(i).getLength();
        }
        return hoursSpent;
    }

    public void setStartEndDate(int start, int end)
    {
        if (start > -1 && start < 54 && end > -1 && end < 54) 
        {
            startDate = start;
            endDate = end;
        } //Throw invalid date exception
        else 
        {
            System.out.println("Invalid date");
        }
    }

    public void setProcessInfo(int i)
    {
        //0 is to-do
        //1 is doing
        //2 is done
        if (i < -1 && i > 3)
        {
            processInfo = i;
        }
    }

    public Map<Integer, ArrayList<Developer>> showAvailableDevs(ArrayList<Developer> devList)
    {
        // inits map of week and most available devs that week
        Map<Integer, ArrayList<Developer>> devAvailability = new HashMap<Integer, ArrayList<Developer>>();
        // inits temp list of devs to add to the map at end of loop
        ArrayList<Developer> weekDevs = new ArrayList<>();
        // loops over each week of Activity
        for (int i = startDate; i <= endDate; i++)
        {
            // resets temp fields
            int j = 0;
            weekDevs.clear();
            // loops until 5 loops or more than 10 devs are found
            // loop variable represents other Activities for given week. The returned list is sorted by adding to it in ascending order.
            while (weekDevs.size() <= 10 && j <= 5)
            {
                for (int k = 0; k < devList.size() ; k++)
                {
                    if (devList.get(k).viewSchedule(i).size() <= j)
                    {
                        weekDevs.add(devList.get(k));
                    }
                }
                j++;
            }
            devAvailability.put(i, weekDevs);
        }  

    return devAvailability;
    }
    
    public Project getProject()
    {
        return project;
    }

    public double getActivityCompHours(Activity activity) {
		return activity.computeHoursSpent();
	}

    public int getId()
    {
        return id;
    }

    public String getName() 
    {
        return name;
    }
}
