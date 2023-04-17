package application;
import java.util.ArrayList;

public class LogPlan {

    private ArrayList<Developer> developerList;
    private ArrayList<Project> projectList;
    private Developer signedIn;

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
        // TODO implement here
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
    
}
