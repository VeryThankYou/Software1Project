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
import java.util.Scanner;
import java.util.*;
import javax.naming.directory.SearchResult;

public class LogPlan 
{
    private ArrayList<Developer> developerList;
    private ArrayList<Project> projectList;
    private Developer signedIn;
    private int activityNextId;
    private Scanner scanner = new Scanner(System.in);

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
        System.out.println("Invalid user ID. Please try again.\n");
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
        if(name == null || name.equals(""))
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

        public void createProject(String name, Developer dev) throws Exception
    {
        if(name == null || name.equals(""))
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
                Project p = new Project(projID+1,name, dev);
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

    public void loginPage()
    {
        System.out.println("Welcome to LogPlan!");
        while(true)
        {
            System.out.println("Please sign in:");
            System.out.print("User ID: ");
            String s = scanner.nextLine();
            this.signIn(s);
            if(!(signedIn == null))
            {
                break;
            }
        }
        this.menu1();
    }

    public void menu1()
    {
        System.out.println("Welcome " + signedIn.getName() + "!");
        String s;
        while(true)
        {
            System.out.println("What would you like to do?");
            System.out.println("1. View your schedule");
            System.out.println("2. View a project");
            System.out.println("3. Create new project");
            System.out.println("4. Exit LogPlan");
            System.out.print("To select an option, write the corresponding number and press enter: ");
            s = scanner.nextLine();
            if(s.equals("1"))
            {
                this.scheduleMenu(currentWeeknum());
                return;
            } 
            if(s.equals("2"))
            {
                this.projectSearch();
                return;
            }
            if(s.equals("3"))
            {
                createProjectMenu();
                return;
            }
            if(s.equals("4"))
            {
                return;
            }
            System.out.println("Not a valid input, please try again.\n");
            
        }
        
    }

    public void scheduleMenu(int weeknum)
    {
        int[] yearWeek = yearSlashWeek(weeknum);
        String s;
        while(true)
        {
            System.out.println("Here is your schedule for week " + Integer.toString(yearWeek[1]) + ", " + Integer.toString(yearWeek[0]));
            ArrayList<Activity> acts = getSignedIn().viewSchedule(weeknum);
            if(acts.size() == 0)
            {
                System.out.println("No activities this week.");
            }
            for(int i = 0; i < acts.size(); i++)
            {
                System.out.println(Integer.toString(i + 1) + ". " + acts.get(i).getName() + " from the project " + acts.get(i).getProject().getName());
            }
            System.out.println("\nWhat do you want to do?");
            System.out.println("1. View schedule for another week");
            System.out.println("2. View/edit your work sessions");
            System.out.println("3. Go back");
            System.out.println("l. Log hours on any of the activities above. To log hours, write 'l' plus the corresponding activity number. Eg: l1");
            System.out.println("a. View project for one of the activities above. To view project, write 'a' plus the corresponding activity number. Eg: a1");
            s = scanner.nextLine();
            if(s.equals("1"))
            {
                int newWeekNum;
                String s2;
                while(true)
                {
                    System.out.println("Please write the year and week number of the week you want to view the schedule for.");
                    System.out.println("Format is as follows: yyyyww. Eg: 202243");
                    s2 = scanner.nextLine();
                    try
                    {
                        newWeekNum = Integer.parseInt(s2);
                        int[] yearweek = yearSlashWeek(newWeekNum);
                        Calendar calendar = Calendar.getInstance(new Locale("dan", "dk"));
                        calendar.set(yearweek[0], 11, 31);
                        int checkWeek = calendar.get(Calendar.WEEK_OF_YEAR);
                        if(s2.length() == 6 && (yearweek[1] <= checkWeek && yearweek[1] > 0))
                        {
                            scheduleMenu(newWeekNum);
                            return;
                        }
                    }
                    catch (Exception e) {}
                    System.out.println("Invalid input. Try again");
                }
            }
            if(s.equals("2"))
            {
                sessionMenu();
                return;
            }
            if(s.equals("3"))
            {
                menu1();
                return;
            }
            if(s.substring(0,1).equals("l"))
            {
                try
                {
                    if(Integer.parseInt(s.substring(1,s.length())) - 1 < acts.size())
                    {
                        Activity act = acts.get(Integer.parseInt(s.substring(1,s.length())) - 1);
                        logHoursMenu(act);
                        return;
                    }
                }
                catch (Exception e){}
            }
            if(s.substring(0,1).equals("a"))
            {
                try
                {
                    if(Integer.parseInt(s.substring(1,s.length())) - 1 < acts.size())
                    {
                        Activity act = acts.get(Integer.parseInt(s.substring(1,s.length())) - 1);
                        viewProjectMenu(act.getProject());
                        return;
                    }
                }
                catch (Exception e)
                {
                }
            }
            System.out.println("Invalid input. Try again.");
        }
        
    }

    public void projectSearch()
    {
        System.out.println("Which project are you looking for?");
        ArrayList<Project> projs;
        String s;
        while(true)
        {
            s = scanner.nextLine();
            projs = searchProjects(s);
            if(projs.size() == 0)
            {
                System.out.println("No projects found. What now?");
                while(true)
                {
                    System.out.println("1. Search again");
                    System.out.println("2. Go back");
                    s = scanner.nextLine();
                    if(s.equals("1"))
                    {
                        projectSearch();
                        return;
                    }
                    if(s.equals("2"))
                    {
                        menu1();
                        return;
                    }
                }
            }
            break;
        }
        while(true)
        {
            for(int i = 0; i < projs.size(); i++)
            {
                Project proj = projs.get(i);
                System.out.println(Integer.toString(i + 1) + ". " + proj.getName());
            }
            System.out.println(Integer.toString(projs.size() + 1) + ". Search again");
            System.out.println(Integer.toString(projs.size() + 2) + ". Go back");
            System.out.println("Which project are you looking for?");
            s = scanner.nextLine();
            if(s.equals(Integer.toString(projs.size() + 1)))
            {
                projectSearch();
                return;
            }
            if(s.equals(Integer.toString(projs.size() + 2)))
            {
                menu1();
                return;
            }
            try
            {
                Project proj = projs.get(Integer.parseInt(s) - 1);
                viewProjectMenu(proj);
                return;
            }
            catch (Exception e)
            {
                System.out.println("Invalid input. Try again.");
            }
        }
    }
    
    public void viewProjectMenu(Project proj)
    {
        // Prints the menu for a project
        System.out.println("Project menu for project " + proj.getName());
        // Prints the id of the project
        System.out.println("Project id: " + proj.getId());
        // Prints the project leader if there is one
        if(proj.getProjectLeader() != null)
        {
            System.out.println("Project leader: " + proj.getProjectLeader().getName());
        }
        else
        {
            System.out.println("No project leader");
        }
        // Checks if there are any activities in the project

        if (proj.getActivities().size() == 0) 
            {
                String noActivities = "No activities in project";
                System.out.println(noActivities);
            } 
            else 
            {
                // start date (from activity)
                // takes the earliest start date from all activities
                int earliestDate = 1000000;
                for (int i = 0; i < proj.getActivities().size(); i++) 
                {
                    if (proj.getActivities().get(i).getStartDate() < earliestDate) 
                    {
                        earliestDate = proj.getActivities().get(i).getStartDate();
                    }
                }

                // converts the earliest date to a year and week
                int earliestDateYear = (int) Math.floor(earliestDate / 100);
                int earliestDateYearMinus = (int) earliestDateYear*100;
                int earliestDateWeek = earliestDate - earliestDateYearMinus;


                String startDate = "Start Date: Week " + earliestDateWeek + ", " + earliestDateYear;
                System.out.println(startDate);

                // end date (from activity)
                // takes the latest end date from all activities
                int latestDate = 0;
                for (int i = 0; i < proj.getActivities().size(); i++) 
                {
                    if (proj.getActivities().get(i).getEndDate() > latestDate) 
                    {
                        latestDate = proj.getActivities().get(i).getEndDate();
                    }
                }

                // converts the latest date to a year and week
                int latestDateYear =  (int) Math.floor(latestDate / 100);
                int latestDateYearMinus = (int)latestDateYear*100;
                int latestDateWeek = latestDate - latestDateYearMinus;

                String endDate = "End Date: Week " + latestDateWeek + ", " + latestDateYear;
                System.out.println(endDate);

                // hours estimated for project
                double estHours = 0;
                for (int i = 0; i < proj.getActivities().size(); i++) 
                {
                    estHours = estHours + proj.getActivities().get(i).getHourEstimate();
                }
                String estHoursString = "Hours estimated for project: " + estHours;
                System.out.println(estHoursString);

                // hours spent on project
                double workedHours = 0;
                for (int i = 0; i < proj.getActivities().size(); i++) 
                {
                    workedHours = workedHours + proj.getActivities().get(i).computeHoursSpent();
                }
                String workedHoursString = "Hours spent on project: " + workedHours;
                System.out.println(workedHoursString);

                // hours spent on each activity
                System.out.println("Hours spent on activity: ");
                for (int i = 0; i < proj.getActivities().size(); i++) 
                {
                    String activityHoursString = "          " + (i + 1) + ") " + proj.getActivities().get(i).getName() + ": " + proj.getActivities().get(i).computeHoursSpent();
                    System.out.println(activityHoursString);
                }
            }

        // Prints the menu options with a makeReport option if the user is a project leader and without if user is not the project leader. 
        // The makeReport option is also available if the user is not a project leader but the project has no project leader.

        int menu = 0;
        while(true)
        {
            if(proj.getProjectLeader() == null || proj.getProjectLeader().equals(this.signedIn)) // Checks if the user is the project leader or if the project has no project leader
            {
                System.out.println("1. View activity");
                System.out.println("2. Edit project");
                System.out.println("3. Make report");
                System.out.println("4. Back");
                menu = 1;
            }
            if(proj.getProjectLeader() == null && !proj.getProjectLeader().equals(this.signedIn)) // Checks if the project has no project leader and if the user is not the project leader
            {
                System.out.println("1. View activity");
                System.out.println("2. Edit project");
                System.out.println("3. Make report");
                System.out.println("4. Assign project leader");
                System.out.println("5. Back");
                menu = 2;
            }
            else // If the user is not the project leader and the project has a project leader
            {
                System.out.println("1. View activity");
                System.out.println("2. Back");
                menu = 3;
            }
            // Reads the user input and calls the appropriate method
            String s = scanner.nextLine();

            if(s.equals("1"))
            {
                viewActivityMenu(proj);
            }
            else if(s.equals("2"))
            {
                editProjectMenu(proj);
            }
            else if(s.equals("3"))
            {
                if(proj.getProjectLeader() == null || proj.getProjectLeader().equals(this.signedIn))
                {
                    try
                    {
                    proj.makeReport(this.signedIn);
                    //Prints that the report has been made
                    System.out.println("Report has been made");
                    return;
                    }
                    catch(Exception e)
                    {
                        System.out.println("Report could not be made");
                    }
                }
                else
                {
                    //Ask for the id of the developer that should be the project leader
                    System.out.println("Write the user id of the developer you want as project leader");
                    String leaderID = scanner.nextLine();
                    Developer dev = getDeveloper(leaderID);
                    if(dev != null)
                    {
                        try
                        {
                            proj.updateLeader(dev, this.signedIn);
                            return;
                        }
                        catch(Exception e){}
                    }
                }
            }
            else if(s.equals("4"))
            {
                menu1();
                return;
            }
            else
            {
            System.out.println("Invalid input");
            }



            
        }

    }

    private void editProjectMenu(Project proj) 
    {
        // Prints the menu options
        System.out.println("Edit Project: " + proj.getName());
    }

    private void viewActivityMenu(Project proj) 
    {
        // Prints Activity Menu:
        System.out.println("Activities: ");

        // This only prints developers assigned to the activity so far.
        // We can improve this.

        // Developer list
        for (int i = 0; i < proj.getActivities().size(); i++) 
        {
            if (proj.getActivities().get(i).getDeveloperList().size() == 0) 
            {
                String noDevelopersString = "No developers assigned to the activity: " + proj.getActivities().get(i).getName();
                System.out.println(noDevelopersString);
            }
            else 
            {
                String developersAssignedString = "Developers assigned to the activity, " + proj.getActivities().get(i).getName() + ": ";
                System.out.println(developersAssignedString);
                for (int j = 0; j < proj.getActivities().get(i).getDeveloperList().size(); j++) 
                {
                    String developerName = "          " + (j + 1) + ") " + proj.getActivities().get(i).getDeveloperList().get(j).getName();
                    System.out.println(developerName);
                }
            }
        }
        
    }

    public void logHoursMenu(Activity act)
    {
        System.out.println(act.getName());
    }

    public void sessionMenu()
    {
        System.out.println("Session menu");
    }

    public void createProjectMenu()
    {
        System.out.println("Create project menu");
        String name;
        while(true)
        {
            System.out.println("Please enter a name for the project");
            name = scanner.nextLine();
            if(!(name.length() > 50) && (name.length() > 0))
            {
                break;
            }
            System.out.println("Name is too long or not given. Please try again");
        }
        while(true)
        {
            System.out.println("Do you want to specify a project leader?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            String s = scanner.nextLine();
            if(s.equals("2"))
            {
                try
                {
                    createProject(name);
                    Project proj = getProjectList().get(getProjectList().size() - 1);
                    viewProjectMenu(proj);
                    return;
                }
                catch(Exception e)
                {}  
            }
            if(s.equals("1"))
            {
                System.out.println("Write the user id of the developer you want as project leader");
                String leaderID = scanner.nextLine();
                Developer dev = getDeveloper(leaderID);
                if(dev != null)
                {
                    try
                    {
                        createProject(name, dev);
                        Project proj = getProjectList().get(getProjectList().size() - 1);
                        viewProjectMenu(proj);
                        return;
                    }
                    catch(Exception e){}
                }
            }
        }
    }

    public int currentWeeknum()
    {
        Calendar calendar = Calendar.getInstance(new Locale("dan", "dk"));
		LocalDate date = LocalDate.now();
		calendar.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
		int weeknum = calendar.get(Calendar.WEEK_OF_YEAR);
        return date.getYear() * 100 + weeknum;
    }

    public int[] yearSlashWeek(int yearweeknum)
    {
        int year = yearweeknum / 100;
        int week = yearweeknum - 100 * year;
        return new int[]{year, week};
    }
}
