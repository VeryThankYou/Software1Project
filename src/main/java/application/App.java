package application;

import java.io.FileNotFoundException;
import java.io.IOException;

public class App 
{
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        LogPlan app = new LogPlan();
        for(int i = 0; i < app.getDeveloperList().size(); i++)
        {
            Developer dev = app.getDeveloperList().get(i);
            System.out.println(dev.getId());
        }
        for(int i = 0; i < app.getProjectList().size(); i++)
        {
            Project proj = app.getProjectList().get(i);
            System.out.println(proj.getName());
            System.out.println(proj.getId());
            for(int i2 = 0; i2 < proj.getActivities().size(); i2 ++)
            {
                Activity act = proj.getActivities().get(i2);
                System.out.println(act.getName());
            }
        }

        Project project = app.getProject(2023001);
        project.makeReport();
    }
}
