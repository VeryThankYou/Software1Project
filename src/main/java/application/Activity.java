package application;

import java.util.ArrayList;

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

    public void showAvailableDevs()
    {
        ArrayList<Developer> availableDevs = new ArrayList<>();
        for (int i = startDate; i <= endDate; i++)
        {
            for (int j = 0; j < devs.size(); j++)
            {
                devs.get(j).viewSchedule(i);
            }
        }  

    }


    
}
