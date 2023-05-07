package application;

import java.time.LocalDate;
import java.util.ArrayList;

import io.cucumber.java.en_old.Ac;

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

    // Preconditions:
    // - weeknum is a valid week number (between 1 and 52)
    // Postconditions:
    // - the returned ArrayList is not null
    // - the returned ArrayList contains only non-null Activity objects
    // - the returned ArrayList contains all Activity objects with a frame that includes weeknum
    public ArrayList<Activity> viewSchedule(int weeknum)
    {
        int year = weeknum / 100;
        int week = weeknum - 100 * year;
        assert ((week >= 1) && (week <= 52)) : "week is not a valid week number";
        ArrayList<Activity> weekActs = new ArrayList<>();
        for (int i = 0 ; i < activities.size() ; i++)
        {
            assert activities.get(i) != null : "activity " + i + " is null";
            int[] frame = activities.get(i).getDates();
            if(weeknum >= frame[0] && weeknum <= frame[1])
            {
                weekActs.add(activities.get(i));
            }    
        }
        assert weekActs != null : "returned ArrayList is null";
        for (Activity act : weekActs)
        {
            assert act != null : "returned ArrayList contains null Activity objects";
        }
        for (Activity act : activities)
        {
            int[] frame = act.getDates();
            if (weeknum >= frame[0] && weeknum <= frame[1])
            {
                assert weekActs.contains(act) : "returned ArrayList does not contain all Activity objects with a frame that includes weeknum";
            }
        }
        return weekActs;
    }

    public void markHours(Activity activity, LocalDate date, Double time) throws Exception
    { 
        if(time == null)
        {
            throw new Exception("Error: Hours field is empty");
        }
        Session session = new Session(time, date, activity.getId());
        activity.addSession(session);
        sessions.add(session);
    }

    public void addSession(Session sess)
    {
        sessions.add(sess);
    }
    
    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void addActivity(Activity act)
    {
        activities.add(act);
    }

    public ArrayList<Session> getSessions()
    {
        return sessions;
    }

    

    public void removeActivity(Activity act)
    {
        activities.remove(act);
    }

    public void addVacation(int startDate, int endDate, Project vacProj, int neextActId) 
    {
        Activity vacAct = new Activity("Vacation", endDate, endDate, 0, vacProj, neextActId);
        vacProj.addActivity(vacAct);
        vacAct.addDev(this);
    }

    public ArrayList<Activity> getActivities()
    {
        return activities;
    }

    public void deleteActivity(Activity act)
    {
        activities.remove(act);
    }

    public void deleteSession(Session session, Activity act)
    {
        act.deleteSession(session);
        this.sessions.remove(session);
    }
}
