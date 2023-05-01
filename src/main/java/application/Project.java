package application;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

import io.cucumber.java.tlh.vaj; 

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
            return;
        }
        throw new UserNotLeaderException("Not Project Leader Error");
    }

    public void addActivity(String newName, int sDate, int eDate, double hourEst, int nextId, Developer loggedIn) throws UserNotLeaderException
    {
        if(isProjectLeader(loggedIn))
        {
            Activity act = new Activity(newName, sDate, eDate, hourEst, this, nextId);
            activities.add(act);
        }
        throw new UserNotLeaderException("Not project leader error");
    }

    public void addActivity(Activity act, Developer loggedIn) throws UserNotLeaderException
    {
        if(isProjectLeader(loggedIn))
        {
            activities.add(act);
            return;
        }
        throw new UserNotLeaderException("Not project leader error");
    }

    public void addActivity(Activity act)
    {
        activities.add(act);
    }

    public void updateName(Developer loggedIn, String newName)
    {
        if (isProjectLeader(loggedIn))
        {
            name = newName;
        }
    }


public void makeReport(Developer loggedIn) throws UserNotLeaderException 
{
    if (isProjectLeader(loggedIn))
    {
        // Create a FileWriter object to write to a file
        try 
        {
            // Directory for reports
            File dir = new File("Reports");
            dir.mkdir();
            // Create a FileWriter object to write to a file
            FileWriter writer = new FileWriter("Reports/report_"+name+".txt");

            // Project report - name of project
            String projectName = "Project Report for " + name;
            System.out.println(projectName);
            writer.write(projectName + "\n");

            // project id
            String projectIDString = "Project ID: " + projectID;
            System.out.println(projectIDString);
            writer.write(projectIDString + "\n");

            // project leader
            if (projectLeader == null) 
            {
                String noProjectLeader = "Project Leader: None";
                System.out.println(noProjectLeader);
                writer.write(noProjectLeader + "\n");
            } 
            else 
            {
                String projectLeaderName = "Project Leader: " + projectLeader.getName();
                System.out.println(projectLeaderName);
                writer.write(projectLeaderName + "\n");
            }

            if (activities.size() == 0) 
            {
                String noActivities = "No activities in project";
                System.out.println(noActivities);
                writer.write(noActivities + "\n");
            } 
            else 
            {
                // start date (from activity)
                // takes the earliest start date from all activities
                int earliestDate = 1000000;
                for (int i = 0; i < activities.size(); i++) 
                {
                    if (activities.get(i).getStartDate() < earliestDate) 
                    {
                        earliestDate = activities.get(i).getStartDate();
                    }
                }

                // converts the earliest date to a year and week
                int earliestDateYear = (int) Math.floor(earliestDate / 100);
                int earliestDateYearMinus = (int) earliestDateYear*100;
                int earliestDateWeek = earliestDate - earliestDateYearMinus;


                String startDate = "Start Date: Week " + earliestDateWeek + ", " + earliestDateYear;
                System.out.println(startDate);
                writer.write(startDate + "\n");

                // end date (from activity)
                // takes the latest end date from all activities
                int latestDate = 0;
                for (int i = 0; i < activities.size(); i++) 
                {
                    if (activities.get(i).getEndDate() > latestDate) 
                    {
                        latestDate = activities.get(i).getEndDate();
                    }
                }

                // converts the latest date to a year and week
                int latestDateYear =  (int) Math.floor(latestDate / 100);
                int latestDateYearMinus = (int)latestDateYear*100;
                int latestDateWeek = latestDate - latestDateYearMinus;

                String endDate = "End Date: Week " + latestDateWeek + ", " + latestDateYear;
                System.out.println(endDate);
                writer.write(endDate + "\n");

                // hours estimated for project
                double estHours = 0;
                for (int i = 0; i < activities.size(); i++) 
                {
                    estHours = estHours + activities.get(i).getHourEstimate();
                }
                String estHoursString = "Hours estimated for project: " + estHours;
                System.out.println(estHoursString);
                writer.write(estHoursString + "\n");

                // hours spent on project
                double workedHours = 0;
                for (int i = 0; i < activities.size(); i++) 
                {
                    workedHours = workedHours + activities.get(i).computeHoursSpent();
                }
                String workedHoursString = "Hours spent on project: " + workedHours;
                System.out.println(workedHoursString);
                writer.write(workedHoursString + "\n");

                ArrayList<Activity> plannedActivities = new ArrayList<Activity>();
                ArrayList<Activity> currentActivities = new ArrayList<Activity>();
                ArrayList<Activity> doneActivities = new ArrayList<Activity>();
                for (int i = 0; i < this.getActivities().size(); i++) 
                {
                    Activity act = this.getActivities().get(i);
                    int process = act.getProcess();
                    switch(process)
                    {
                        case 0: plannedActivities.add(act);
                                break;
                        case 1: currentActivities.add(act);
                                break;
                        case 2: doneActivities.add(act);
                                break;
                    }
                }
                Integer[] sizes = new Integer[]{plannedActivities.size(), currentActivities.size(), doneActivities.size()};
                int maxSize = Collections.max(Arrays.asList(sizes));
                String sColumns = "";
                sColumns = sColumns + "Planned Activities:";
                for(int i = 0; i < 31; i++){sColumns = sColumns + " ";}
                sColumns = sColumns + "Current Activities:";
                for(int i = 0; i < 31; i++){sColumns = sColumns + " ";}
                sColumns = sColumns + "Done Activities:";
                System.out.println(sColumns);
                writer.write(sColumns + "\n");
                for(int i = 0; i < maxSize; i++)
                {
                    Activity pAct; 
                    Activity cAct; 
                    Activity dAct; 
                    try
                    {
                        pAct = plannedActivities.get(i);
                        sColumns = pAct.getName();
                        for(int i2 = 0; i2 < 50 - pAct.getName().length(); i2++){sColumns = sColumns + " ";}
                    }
                    catch(Exception e){for(int i2 = 0; i2 < 50; i2++){sColumns = sColumns + " ";}}
                    try
                    {
                        cAct = currentActivities.get(i);
                        sColumns = sColumns + cAct.getName();
                        for(int i2 = 0; i2 < 50 - cAct.getName().length(); i2++){sColumns = sColumns + " ";}
                    }
                    catch(Exception e){for(int i2 = 0; i2 < 50; i2++){sColumns = sColumns + " ";}}
                    try
                    {
                        dAct = doneActivities.get(i);
                        sColumns = sColumns + dAct.getName();
                    }
                    catch(Exception e){};
                    System.out.println(sColumns);
                    writer.write(sColumns + "\n");
                }

                // hours spent on each activity
                System.out.println("Hours spent on activity: ");
                writer.write("Hours spent on activity: \n");
                for (int i = 0; i < activities.size(); i++) 
                {
                    String activityHoursString = "          " + (i + 1) + ") " + activities.get(i).getName() + ": " + activities.get(i).computeHoursSpent();
                    System.out.println(activityHoursString);
                    writer.write(activityHoursString + "\n");
                }
            }

            // Developer list
            for (int i = 0; i < activities.size(); i++) 
            {
                if (activities.get(i).getDeveloperList().size() == 0) 
                {
                    String noDevelopersString = "No developers assigned to the activity: " + activities.get(i).getName();
                    System.out.println(noDevelopersString);
                    writer.write(noDevelopersString + "\n");
                }
                else 
                {
                    String developersAssignedString = "Developers assigned to the activity, " + activities.get(i).getName() + ": ";
                    System.out.println(developersAssignedString);
                    writer.write(developersAssignedString + "\n");
                    for (int j = 0; j < activities.get(i).getDeveloperList().size(); j++) 
                    {
                        String developerName = "          " + (j + 1) + ") " + activities.get(i).getDeveloperList().get(j).getName();
                        System.out.println(developerName);
                        writer.write(developerName + "\n");
                    }
                }
            }
            writer.close();
        }
        catch (IOException e) 
        {
            System.out.println("An error occurred in makeReport.");
            e.printStackTrace();
        }
        return;
    }
    throw new UserNotLeaderException("Not project leader error");
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

    public void deleteActivity(Activity act)
    {
        for(int i = 0; i < act.getDeveloperList().size(); i ++)
        {
            Developer dev = act.getDeveloperList().get(i);
            dev.deleteActivity(act);
        }
        activities.remove(act);
    }
}
