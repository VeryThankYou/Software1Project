package application;
import java.util.ArrayList;

public class LogPlan {

    private ArrayList<Developer> developerList;
    private ArrayList<Project> projectList;
    private Developer signedIn;

    public void addDeveloper(String credentials);
    {
        for (int i = 0; i < developerList.size(); i++)
        {
            if (developerList.get(i).getCredentials() == credentials)
            {
                return;
            }
        }

    }

    public void createProject(String name);
    {
        // TODO implement here
    }

    public void viewSchedule(Developer dev);
    {
        // TODO implement here
    }

    public getReport(Project project);
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
