package application;

import java.time.LocalDate;
import java.util.ArrayList;

public class Developer 

{
    private String id;
    private String name;
    private Session session;
    private Activity activity;

    public Developer(String newId, String newName)
    {
        id = newId;
        name = newName;
    }

    public ArrayList<Activity> viewSchedule(int weeknum)
    {
        // TODO implement here

    }

    public void viewProjects()
    {
        // TODO implement here
    }

    public void markHours(Activity activity, LocalDate date, Double time)
    {
        // TODO implement here

    }
    
    public String getId()
    {
        return id;
    }
}
