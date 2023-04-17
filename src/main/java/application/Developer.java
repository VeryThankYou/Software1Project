package application;

import java.time.LocalDate;
import java.util.ArrayList;

public class Developer 

{
    private String id;
    private String name;
    private ArrayList<Activity> activities = new ArrayList<>();
    private ArrayList<Session> sessions = new ArrayList<>();

    public Developer(String newId, String newName)
    {
        id = newId;
        name = newName;
    }

    public ArrayList<Activity> viewSchedule(int weeknum)
    {
        ArrayList<Activity> weekActs = new ArrayList<>();
        for (int i = 0 ; i < activities.size() ; i++)
        {
            int[] frame = activities.get(i).getDates();
            if(weeknum >= frame[1] && weeknum <= frame[0])
            {
                weekActs.add(activities.get(i));
            }    
        }
        return weekActs;
    }

    public void viewProjects()
    {
        // TODO implement here
    }

    public void markHours(Activity activity, LocalDate date, Double time)
    {
        Session session = new Session(time, date);
        activity.addSession(session);
        sessions.add(session);
    }
    
    public String getId()
    {
        return id;
    }
}
