package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

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
        this.processInfo = 0;
    }

    public Activity(String name, int sDate, int eDate, double hourEst, Project newProject, int id, int proc)
    {
        this.name = name;
        this.startDate = sDate;
        this.endDate = eDate;
        this.hourEstimate = hourEst;
        this.project = newProject;
        this.id = id;
        this.processInfo = proc;
    }

    public void addDev(Developer dev)
    {
        if (devs.contains(dev) == false) 
            {
            dev.addActivity(this);
            devs.add(dev);
            }
    }

    public void removeDev(Developer dev)
    {
        if (devs.contains(dev) == true) 
            {
            dev.removeActivity(this);
            devs.remove(dev);
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

    public double computeHoursSpent() //prepost
    {
        double hoursSpent = 0; 
        for(int i = 0; i < sessions.size(); i++)
        {
            hoursSpent += sessions.get(i).getLength();
        }
        return hoursSpent;
    }

    public void setStartEndDate(int start, int end) //prepost
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

    public void setProcessInfo(int i) //prepost
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
        // loops over each week of Activity
        for (int i1 = 0; i1 < devList.size(); i1++)
        {
            // resets temp fields
            Developer dev = devList.get(i1);
            int maxactivities = 0;
            // loops until 5 loops or more than 10 devs are found
            // loop variable represents other Activities for given week. The returned list is sorted by adding to it in ascending order.
            for(int i2 = this.startDate; i2 <= this.endDate; i2++)
            {
                
                int tempactivities = 0;
                tempactivities = tempactivities + dev.viewSchedule(i2).size();
                if(tempactivities > maxactivities)
                {
                    maxactivities = tempactivities;
                }
            }
            if(devAvailability.containsKey(maxactivities))
            {
                devAvailability.get(maxactivities).add(dev);
            }
            else
            {
                ArrayList<Developer> devlist = new ArrayList<Developer>();
                devlist.add(dev);
                devAvailability.put(maxactivities, devlist);
            }
        }
        
        SortedSet<Integer> keys = new TreeSet<>(devAvailability.keySet());
        
        for (int key: keys)
        {
            ArrayList<Developer> devlist = devAvailability.get(key);
            int originalSize = devlist.size();
            int numDels = 0;
            for(int i = 0; i < originalSize; i++)
            {
                Developer dev = devlist.get(i - numDels);
                if(this.devs.contains(dev))
                {
                    numDels = numDels + 1;
                    devlist.remove(dev);
                    if(devlist.size() == 0)
                    {
                        devAvailability.remove(key);
                        break;
                    }
                }
            }
            
        }
        
        keys = new TreeSet<>(devAvailability.keySet());
        int numchosen = 0;
        for (int key: keys)
        {
            ArrayList<Developer> devlist = devAvailability.get(key);
            int originalSize = devlist.size();
            int numDels = 0;
            for(int i = 0; i < originalSize; i++)
            {
                Developer dev = devlist.get(i - numDels);
                if(numchosen > 10)
                {
                    numDels = numDels + 1;
                    devlist.remove(dev);
                    if(devlist.size() == 0)
                    {
                        devAvailability.remove(key);
                        break;
                    }
                }
                numchosen = numchosen + 1;
            }
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

    public int getStartDate() 
    {
        return startDate;
    }

    public int getEndDate() 
    {
        return endDate;
    }

    public double getHourEstimate() 
    {
        return hourEstimate;
    }

    // Get Developer ArrayList
    public ArrayList<Developer> getDeveloperList() 
    {
        return devs;
    }

    public int getProcess()
    {
        return this.processInfo;
    }

    public void setName(String newName)
    {
        this.name = newName;
    }

    public void setHourEst(double newHours)
    {
        this.hourEstimate = newHours;
    }

    public void deleteSession(Session session)
    {
        this.sessions.remove(session);
    }
}
