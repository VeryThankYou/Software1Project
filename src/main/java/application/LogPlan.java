package application;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;

public class LogPlan 
{
    private ArrayList<Developer> developerList;
    private ArrayList<Project> projectList;
    private Developer signedIn;
    private int activityNextId;

    public LogPlan() throws FileNotFoundException, IOException
    {
        developerList = new ArrayList<Developer>();
        projectList = new ArrayList<Project>();
        signedIn = null;
        addCsvDevelopers();
        addCsvProjects();
        addCsvActivities();
        activityUserConnection();
        addCsvSessions();
    }

    public void signIn(String id) 
    {
        for (int i = 0; i < developerList.size(); i++)
        {
            if (developerList.get(i).getId().equals(id))
            {
                this.signedIn = developerList.get(i);
                System.out.println(id);
                return;
            }
        }
    }

    public void addDeveloper(String credentials, String name)
    {
        for (int i = 0; i < developerList.size(); i++)
        {
            if (developerList.get(i).getId().equals(credentials))
            {
                return;
            }
        }
        Developer dev = new Developer(credentials, name);
        developerList.add(dev);
    }

    public void createProject(String name) throws Exception
    {
        if(name == null)
        {
            throw new Exception("More information needed");
        }
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int y = c.get(Calendar.YEAR);
        
        for (int i = projectList.size()-1; i >= 0; i--) {
            int projID = projectList.get(i).getId();
            int projYear = projID/1000; 
            if (projYear == y) 
            {
                Project p = new Project(projID+1,name);
                projectList.add(p);
            }
        }

    }

    private void loadProject(int id, String name)
    {
        Project proj = new Project(id, name);
        projectList.add(proj);
    }
    public void viewSchedule(Developer dev)
    {
        // TODO implement here
    }

    public void getReport(Project project) throws UserNotLeaderException
    {
        for (int i = 0; i < projectList.size(); i++)
        {
            if (projectList.get(i) == project)
            {
                project.makeReport(signedIn);
            }
        }
    }
    
    public ArrayList<Project> searchProjects(String searchString)
    {
        ArrayList<Project> projects = new ArrayList<Project>();
        for (int i = 0; i < projectList.size(); i++)
        {
            String idAsString = Integer.toString(projectList.get(i).getId());
            if (projectList.get(i).getName().contains(searchString) || idAsString.contains(searchString))
            {
                projects.add(projectList.get(i));
            }       
        }
        return projects;
    }

    private void addCsvDevelopers() throws FileNotFoundException, IOException
    {
        File file = new File("csvfiles/developers.csv");
        BufferedReader csvReader = new BufferedReader(new FileReader(file));
        String row = csvReader.readLine();
        while (row != null)
        {
            String[] line = row.split(",");
            addDeveloper(line[0], line[1]);
            row = csvReader.readLine();
        }
        csvReader.close();
    }

    public ArrayList<Developer> getDeveloperList()
    {
        return developerList;
    }

    public ArrayList<Project> getProjectList()
    {
        return projectList;
    }

    public Developer getSignedIn()
    {
        return signedIn;
    }

    private void addCsvProjects() throws FileNotFoundException, IOException
    {
        File file = new File("csvfiles/projects.csv");
        BufferedReader csvReader = new BufferedReader(new FileReader(file));
        String row = csvReader.readLine();
        while (row != null)
        {
            String[] line = row.split(",");
            loadProject(Integer.parseInt(line[0]), line[1]);
            row = csvReader.readLine();
        }
        csvReader.close();
    }

    private void addCsvActivities() throws FileNotFoundException, IOException
    {
        File file = new File("csvfiles/activities.csv");
        BufferedReader csvReader = new BufferedReader(new FileReader(file));
        String row = csvReader.readLine();
        while (row != null)
        {
            String[] line = row.split(",");
            Project proj = getProject(Integer.parseInt(line[4]));
            Activity act = new Activity(line[0], Integer.parseInt(line[1]), Integer.parseInt(line[2]), Double.parseDouble(line[3]), proj, Integer.parseInt(line[5]), Integer.parseInt(line[6]));
            proj.addActivity(act);
            if(act.getId() >= activityNextId)
            {
                activityNextId = act.getId() + 1;
            }
            row = csvReader.readLine();
        }
        csvReader.close();
    }

    private void activityUserConnection() throws FileNotFoundException, IOException
    {
        File file = new File("csvfiles/activity_developer.csv");
        BufferedReader csvReader = new BufferedReader(new FileReader(file));
        String row = csvReader.readLine();
        while (row != null)
        {
            String[] line = row.split(",");
            Activity act = findActivity(Integer.parseInt(line[0]));
            Developer dev = getDeveloper(line[1]);
            dev.addActivity(act);
            act.addDev(dev);
            row = csvReader.readLine();
        }
        csvReader.close();
    }

    public Project getProject(int id)
    {
        for(int i = 0; i < projectList.size(); i ++)
        {
            if(id == projectList.get(i).getId())
            {
                return projectList.get(i);
            }
        }
        return null;
    }

    public Developer getDeveloper(String id)
    {
        for(int i = 0; i < developerList.size(); i ++)
        {
            if(id.equals(developerList.get(i).getId()))
            {
                return developerList.get(i);
            }
        }
        return null;
    }

    private void addCsvSessions() throws FileNotFoundException, IOException, NumberFormatException
    {
        File file = new File("csvfiles/session.csv");
        BufferedReader csvReader = new BufferedReader(new FileReader(file));
        String row = csvReader.readLine();
        while (row != null)
        {
            String[] line = row.split(",");
            String[] datedate = line[1].split("&");
            LocalDate date = LocalDate.of(Integer.parseInt(datedate[0]), Integer.parseInt(datedate[1]), Integer.parseInt(datedate[2]));
            Session sess = new Session(Double.parseDouble(line[0]), date);
            Activity act = findActivity(Integer.parseInt(line[3]));
            Developer dev = getDeveloper(line[2]);
            act.addSession(sess);
            dev.addSession(sess);
            row = csvReader.readLine();
        } 
        csvReader.close();
    }

    private Activity findActivity(int id)
    {
        for(int i1 = 0; i1 < projectList.size(); i1 ++)
        {
            for (int i2 = 0; i2 < projectList.get(i1).getActivities().size(); i2 ++)
            {
                if(projectList.get(i1).getActivities().get(i2).getId() == id)
                {
                    return projectList.get(i1).getActivities().get(i2);
                }
            }
        }
        return null;
    }

    public void addActivityToProject(Project proj, String newName, int sDate, int eDate, double hourEst) throws UserNotLeaderException
    {
        Developer loggedIn = this.signedIn;
        proj.addActivity(newName, sDate, eDate, hourEst, this.activityNextId, loggedIn);
        this.activityNextId = this.activityNextId + 1;
    }

    public void addActivityToProject(Project proj, Activity act) throws UserNotLeaderException
    {
        Developer loggedIn = this.signedIn;
        proj.addActivity(act, signedIn);
        this.activityNextId = this.activityNextId + 1;
    }

    public int getActivityNextId() 
    {
        activityNextId = activityNextId + 1;
        return activityNextId - 1;
    }

    public ArrayList<Activity> showProject(Project proj)
    {
        return proj.getActivities();
    }
}
