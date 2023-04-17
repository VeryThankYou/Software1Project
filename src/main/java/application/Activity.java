package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity
{

    private String name;
    private int startDate;
    private int endDate;
    private double hourEstimate;
    private ArrayList<Developer> devs = new ArrayList<>();
    private ArrayList<Session> sessions = new ArrayList<>();
    private int processInfo;


    public Activity(String name, int sDate, int eDate, double hourEst)
    {
        this.name = name;
        this.startDate = sDate;
        this.endDate = eDate;
        this.hourEstimate = hourEst;
    }

    public void addDev(Developer dev)
    {
        if (devs.contains(dev) == false) 
            {
            devs.add(dev);
            }
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
        Map<Integer, ArrayList<Developer>> devAvailability = new HashMap<Integer, ArrayList<Developer>>();
        ArrayList<Developer> weekDevs = new ArrayList<>();
        for (int i = startDate; i <= endDate; i++)
        {
            int j = 0;
            weekDevs.clear();
            while (weekDevs.size() < 10 && j < 5)
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
    
}
