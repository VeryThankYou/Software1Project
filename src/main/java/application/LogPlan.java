package application;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;

public class LogPlan 
{

    private ArrayList<Developer> developerList;
    private ArrayList<Project> projectList;
    private Developer signedIn;

    public LogPlan() throws FileNotFoundException, IOException
    {
        developerList = new ArrayList<Developer>();
        signedIn = null;
        addCsvDevelopers();
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

    public void addCsvDevelopers() throws FileNotFoundException, IOException
    {
        File file = new File("csvfiles\\developers.csv");
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

}
