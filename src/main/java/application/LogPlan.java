package application;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LogPlan {

    private ArrayList<Developer> developerList;
    private ArrayList<Project> projectList;
    private Developer signedIn;

    public void signIn(String id) 
    {
        for (int i = 0; i < developerList.size(); i++)
        {
            if (developerList.get(i).getId().equals(id))
            {
                this.signedIn = developerList.get(i);
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

    public void createProject(String name)
    {
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int y = c.get(Calendar.YEAR)/100;
    
        for (int i = projectList.size(); i > 0; i--) {
            int projID = projectList.get(i).getId();
            int projYear = projID/1000; 
            if (projYear == y) 
            {
                Project p = new Project(projID+1,name);
                projectList.add(p);
            }
        }
    }

    public void viewSchedule(Developer dev)
    {
        // TODO implement here
    }

    public void getReport(Project project)
    {
        for (int i = 0; i < projectList.size(); i++)
        {
            if (projectList.get(i) == project)
            {
                project.makeReport();
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
}
