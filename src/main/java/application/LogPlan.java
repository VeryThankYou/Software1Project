package application;
import java.util.Arrays;
import java.util.Collections;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.*;

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
        addtxtDevelopers();
        addtxtProjects();
        addtxtActivities();
        activityUserConnection();
        addtxtSessions();
    }

    public void signIn(String id) 
    {
        String lowercaseId = id.toLowerCase();                                  // convert input to lowercase
        for (int i = 0; i < developerList.size(); i++)
        {
            String lowercaseDevId = developerList.get(i).getId().toLowerCase(); // convert ID to lowercase
            if (lowercaseDevId.equals(lowercaseId))
            {
                this.signedIn = developerList.get(i);
                //System.out.println(id);                                       // for testing
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
                return;
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
                return;
            }
        }
    }

    private void writeTotxt() throws IOException
    {
        File dir = new File("txtfiles");
        dir.delete();
        dir = new File("txtfiles");
        dir.mkdirs();
        // Create a FileWriter object to write to a file
        FileWriter writer = new FileWriter("txtfiles/projects.txt");
        for(int i = 0; i < projectList.size(); i++)
        {
            Project proj = projectList.get(i);
            String s = Integer.toString(proj.getId()) + "," + proj.getName() + ",";
            if(proj.getProjectLeader() != null){s = s + proj.getProjectLeader().getId();}
            writer.write(s + "\n");
        }
        FileWriter writer3 = new FileWriter("txtfiles/developers.txt");
        FileWriter writer2 = new FileWriter("txtfiles/session.txt");
        for(int i = 0; i < developerList.size(); i++)
        {
            Developer dev = developerList.get(i);
            String s = dev.getId() + "," + dev.getName();
            writer3.write(s + "\n");
            for(int i2 = 0; i2 < dev.getSessions().size(); i2++)
            {
                Session sesh = dev.getSessions().get(i2);
                LocalDate date = sesh.getDate();
                String s2 = Double.toString(sesh.getLength()) + "," + Integer.toString(date.getYear()) + "&" + Integer.toString(date.getMonthValue()) + "&" + Integer.toString(date.getDayOfMonth()) + "," + dev.getId() + "," + Integer.toString(sesh.getActId());
                writer2.write(s2 + "\n");
            }
        }
        FileWriter writer4 = new FileWriter("txtfiles/activities.txt");
        FileWriter writer5 = new FileWriter("txtfiles/activity_developer.txt");
        for(int i = 0; i < projectList.size(); i++)
        {
            Project proj = projectList.get(i);
            for(int i2 = 0; i2 < proj.getActivities().size(); i2 ++)
            {
                Activity act = proj.getActivities().get(i2);
                String s = act.getName() + "," + Integer.toString(act.getStartDate()) + "," + Integer.toString(act.getEndDate()) + "," + Double.toString(act.getHourEstimate()) + "," + Integer.toString(proj.getId()) + "," + Integer.toString(act.getId()) + "," + Integer.toString(act.getProcess());
                writer4.write(s + "\n");
                for(int i3 = 0; i3 < act.getDeveloperList().size(); i3++)
                {
                    Developer dev = act.getDeveloperList().get(i3);
                    String s2 = Integer.toString(act.getId()) + "," + dev.getId();
                    writer5.write(s2 + "\n");
                }
            }
        }
        writer.close();
        writer2.close();
        writer3.close();
        writer4.close();
        writer5.close();
    }

    private void loadProject(int id, String name)
    {
        Project proj = new Project(id, name);
        projectList.add(proj);
    }

    private void loadProject(int id, String name, Developer dev)
    {
        Project proj = new Project(id, name, dev);
        projectList.add(proj);
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
        Project vacation = getProject(9999999);
        ArrayList<Project> projects = new ArrayList<Project>();
        String lowercaseSearchString = searchString.toLowerCase();             // convert input to lowercase
        for (int i = 0; i < projectList.size(); i++)
        {
            String lowercaseName = projectList.get(i).getName().toLowerCase(); // convert name to lowercase
            String idAsString = Integer.toString(projectList.get(i).getId());
            String lowercaseId = idAsString.toLowerCase();                     // convert ID to lowercase
            if (lowercaseName.contains(lowercaseSearchString) || lowercaseId.contains(lowercaseSearchString))
            {
                projects.add(projectList.get(i));
            }       
        }
        if(projects.contains(vacation))
        {
            projects.remove(vacation);
        }
        return projects;
    }


    private void addtxtDevelopers() throws FileNotFoundException, IOException
    {
        File file = new File("txtfiles/developers.txt");
        BufferedReader txtReader = new BufferedReader(new FileReader(file));
        String row = txtReader.readLine();
        while (row != null)
        {
            String[] line = row.split(",");
            addDeveloper(line[0], line[1]);
            row = txtReader.readLine();
        }
        txtReader.close();
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

    private void addtxtProjects() throws FileNotFoundException, IOException
    {
        File file = new File("txtfiles/projects.txt");
        BufferedReader txtReader = new BufferedReader(new FileReader(file));
        String row = txtReader.readLine();
        while (row != null)
        {
            String[] line = row.split(",");
            if(line.length == 2)
            {
                loadProject(Integer.parseInt(line[0]), line[1]);    
            }
            else
            {
                loadProject(Integer.parseInt(line[0]), line[1], getDeveloper(line[2]));
            }
            row = txtReader.readLine();
        }
        txtReader.close();
    }

    private void addtxtActivities() throws FileNotFoundException, IOException
    {
        File file = new File("txtfiles/activities.txt");
        BufferedReader txtReader = new BufferedReader(new FileReader(file));
        String row = txtReader.readLine();
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
            row = txtReader.readLine();
        }
        txtReader.close();
    }

    private void activityUserConnection() throws FileNotFoundException, IOException
    {
        File file = new File("txtfiles/activity_developer.txt");
        BufferedReader txtReader = new BufferedReader(new FileReader(file));
        String row = txtReader.readLine();
        while (row != null)
        {
            String[] line = row.split(",");
            Activity act = findActivity(Integer.parseInt(line[0]));
            Developer dev = getDeveloper(line[1]);
            act.addDev(dev);
            row = txtReader.readLine();
        }
        txtReader.close();
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

    private void addtxtSessions() throws FileNotFoundException, IOException, NumberFormatException
    {
        File file = new File("txtfiles/session.txt");
        BufferedReader txtReader = new BufferedReader(new FileReader(file));
        String row = txtReader.readLine();
        while (row != null)
        {
            String[] line = row.split(",");
            String[] datedate = line[1].split("&");
            LocalDate date = LocalDate.of(Integer.parseInt(datedate[0]), Integer.parseInt(datedate[1]), Integer.parseInt(datedate[2]));
            Session sess = new Session(Double.parseDouble(line[0]), date, Integer.parseInt(line[3]));
            Activity act = findActivity(Integer.parseInt(line[3]));
            Developer dev = getDeveloper(line[2]);
            act.addSession(sess);
            dev.addSession(sess);
            row = txtReader.readLine();
        } 
        txtReader.close();
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
        proj.addActivity(act, this.signedIn);
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
                this.projectSearchMenu();
                return;
            }
            if(s.equals("3"))
            {
                createProjectMenu();
                return;
            }
            if(s.equals("4"))
            {
                try
                {
                    writeTotxt();
                } catch (Exception e){System.out.println("hej");}
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
            System.out.println("3. Add vacation");
            System.out.println("4. Delete vacation");
            System.out.println("5. Go back");
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
                addVacationMenu();
                return;
            }
            if(s.equals("4"))
            {
                deleteVacationMenu();
                return;
            }
            if(s.equals("5"))
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

    public void addVacationMenu()
    {
        int startDate;
        while (true) // Loops until the user enters a valid start date
        {
            System.out.println("Please enter a start time for the vacation in the form of a year and week numbar");
            System.out.println("Do this in the format yyyyww. Eg: 202304");
            System.out.println("Write q to go back");
            String sdateString = scanner.nextLine();
            if(sdateString.equals("q"))
            {
                scheduleMenu(currentWeeknum());
                return;
            }
            try
            {
                startDate = Integer.parseInt(sdateString);
                int[] yearweek = yearSlashWeek(startDate);
                Calendar calendar = Calendar.getInstance(new Locale("dan", "dk"));
                calendar.set(yearweek[0], 11, 31);
                int checkWeek = calendar.get(Calendar.WEEK_OF_YEAR);
                if(sdateString.length() == 6 && (yearweek[1] <= checkWeek && yearweek[1] > 0))
                {
                    break;
                }
            }
            catch(Exception e){}
            System.out.println("Invalid input. Please try again.");
        }

        int endDate;
        while(true) // Loops until the user enters a valid end date
        {
            System.out.println("Please enter an end time for the vacation in the form of a year and week numbar");
            System.out.println("Do this in the format yyyyww. Eg: 202304. It has to be later than " + Integer.toString(startDate));
            System.out.println("Write q to go back");
            String edateString = scanner.nextLine();
            if(edateString.equals("q"))
            {
                scheduleMenu(currentWeeknum());
                return;
            }
            try
            {
                endDate = Integer.parseInt(edateString);
                int[] yearweek = yearSlashWeek(endDate);
                Calendar calendar = Calendar.getInstance(new Locale("dan", "dk"));
                calendar.set(yearweek[0], 11, 31);
                int checkWeek = calendar.get(Calendar.WEEK_OF_YEAR);
                if(startDate <= endDate && (edateString.length() == 6 && (yearweek[1] <= checkWeek && yearweek[1] > 0)))
                {
                    break;                    
                }
            }
            catch(Exception e){}
            System.out.println("Invalid input. Please try again.");
        }
        getSignedIn().addVacation(startDate, endDate, getProject(9999999), getActivityNextId());
        System.out.println("Vacation added successfully.");
        scheduleMenu(currentWeeknum());
        return;
    }

    public void deleteVacationMenu()
    {
        ArrayList<Activity> vacList = new ArrayList<Activity>();
        for(int i = 0; i < signedIn.getActivities().size(); i ++)
        {
            if(signedIn.getActivities().get(i).getProject().getId() == 9999999)
            {
                Activity act = signedIn.getActivities().get(i);
                vacList.add(act);
                
            }
        }
        while(true)
        {
            for(int i = 0; i < vacList.size(); i++)
            {
                Activity act = vacList.get(i);
                int[] startYearWeek = yearSlashWeek(act.getStartDate());
                int[] endYearWeek = yearSlashWeek(act.getEndDate());
                System.out.println(Integer.toString(i + 1) + ") Vacation with start time week" + Integer.toString(startYearWeek[1]) + ", " + Integer.toString(startYearWeek[0]));
                System.out.println("       and with end time week " + Integer.toString(endYearWeek[1]) + ", " + Integer.toString(endYearWeek[0])); 
            }
            System.out.println("Select the number of the vacation you want to delete");
            System.out.println("Write " + Integer.toString(vacList.size() + 1) + " to go back");
            String s = scanner.nextLine();
            if(s.equals(Integer.toString(vacList.size() + 1)))
            {
                scheduleMenu(currentWeeknum());
                return;
            }
            try
            {
                Activity act = vacList.get(Integer.parseInt(s) - 1);
                Project vacProj = getProject(9999999);
                vacProj.deleteActivity(act);
                System.out.println("Vacation deleted successfully");
                scheduleMenu(currentWeeknum());
                return;
            }
            catch(Exception e){}
            System.out.println("Invalid input. Please try again");
        }

    }

    public void projectSearchMenu()
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
                        projectSearchMenu();
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
                projectSearchMenu();
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
        if(proj.getId() == 9999999)
        {
            System.out.println("Vacation isn't part of a project");
            scheduleMenu(currentWeeknum());
            return;
        }
        if(proj.isProjectLeader(signedIn))
        {
            viewProjectMenuAsLeader(proj);
            return;
        }
        printProjectOverview(proj);
        while(true)
        {
            System.out.println("1. View activities");
            System.out.println("2. Back");
            System.out.println("Choose option by its corresponding number");
            String s = scanner.nextLine();
            if(s.equals("1"))
            {
                viewActivityMenu(proj); // Calls the view activity menu
                return;
            }
            if(s.equals("2"))
            {
                menu1();
                return;
            }
            System.out.println("Invalid Input. Please try again");
        }
    }

    public void viewProjectMenuAsLeader(Project proj)
    {
        printProjectOverview(proj);
        while(true)
        {
            System.out.println("1. View or add activities");
            System.out.println("2. Edit project name");
            System.out.println("3. Make report");
            System.out.println("4. Assign project leader");
            System.out.println("5. Back");
            System.out.println("Choose option by its corresponding number");
            String s = scanner.nextLine();
            if(s.equals("1"))
            {
                viewActivityMenu(proj); // Calls the view activity menu
                return;
            }
            if(s.equals("2"))
            {
                while(true)
                {
                    System.out.println("What name do you want to give the project? (Max 50 chars, min 1, no commas)");
                    String s2 = scanner.nextLine();
                    if(!s2.contains(",") && (s2.length() <= 50 && s2.length() > 1))
                    {
                        proj.updateName(signedIn, s2);
                        viewProjectMenu(proj);
                        return;
                    }
                    System.out.println("Invalid input. Try again");
                }
            }
            if(s.equals("3")) 
            {
                try
                {
                    proj.makeReport(this.signedIn);
                    //Prints that the report has been made
                    System.out.println("Report has been made");
                    viewProjectMenu(proj);
                    return;
                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                    System.out.println("Report could not be made");
                }
            }
            if(s.equals("4")) 
            {
                while(true)
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
                            viewProjectMenu(proj);
                            return;
                        }
                        catch(Exception e){}
                    }
                    System.out.println("Invalid input. Please try again");
                }
            }
            if(s.equals("5"))
            {
                menu1();
                return;
            }
            System.out.println("Invalid Input. Please try again");
        }
    }


    private void addActivityMenu(Project proj) 
    // This functions adds a new activity to the project. 
    //  It asks for the name, start date, end date, estimated hours, and if any developers should be assigned.
    {
        System.out.println("Add activity to project: " + proj.getName() + "(" + proj.getId() + ")");
        String name;
        while(true) // Loops until the user enters a valid name
        {
            System.out.println("Please enter a name for the activity. Cannot contain commas");
            name = scanner.nextLine();
            if(!name.contains(",") && (!(name.length() > 50) && (name.length() > 0)))
            {
                break;
            }
            System.out.println("Name is too long or not given. Please try again");
        }
        int startDate;
        while (true) // Loops until the user enters a valid start date
        {
            System.out.println("Please enter a start time for the activity in the form of a year and week numbar");
            System.out.println("Do this in the format yyyyww. Eg: 202304");
            String sdateString = scanner.nextLine();
            try
            {
                startDate = Integer.parseInt(sdateString);
                int[] yearweek = yearSlashWeek(startDate);
                Calendar calendar = Calendar.getInstance(new Locale("dan", "dk"));
                calendar.set(yearweek[0], 11, 31);
                int checkWeek = calendar.get(Calendar.WEEK_OF_YEAR);
                if(sdateString.length() == 6 && (yearweek[1] <= checkWeek && yearweek[1] > 0))
                {
                    break;
                }
            }
            catch(Exception e){}
            System.out.println("Invalid start date. Please try again.");
        }

        int endDate;
        while(true) // Loops until the user enters a valid end date
        {
            System.out.println("Please enter an end time for the activity in the form of a year and week numbar");
            System.out.println("Do this in the format yyyyww. Eg: 202304. It has to be later than " + Integer.toString(startDate));
            String edateString = scanner.nextLine();
            try
            {
                endDate = Integer.parseInt(edateString);
                int[] yearweek = yearSlashWeek(endDate);
                Calendar calendar = Calendar.getInstance(new Locale("dan", "dk"));
                calendar.set(yearweek[0], 11, 31);
                int checkWeek = calendar.get(Calendar.WEEK_OF_YEAR);
                if(startDate < endDate && (edateString.length() == 6 && (yearweek[1] <= checkWeek && yearweek[1] > 0)))
                {
                    break;
                }
            }
            catch(Exception e){}
            System.out.println("Invalid end date. Please try again.");
        }

        double hours;
        while(true) // Loops until the user enters a valid estimated hours
        {
            System.out.println("Please enter an estimated amount of hours for the activity (as a positive number the format 0.0. Eg. 1.5 for one and a half hour):");
            String s = scanner.nextLine();
            try
            {
                hours = (double) Double.parseDouble(s);
                if(hours > 0) // Checks if the hours are positive
                {
                    break;
                } // If the hours are not valid, the user is asked to try again
                System.out.println("Invalid input. Please enter a positive number");
            }
            catch(Exception e)
            {
                System.out.println("Invalid input. Please try again");
            }
        }
        Activity act = new Activity(name, startDate, endDate, hours, proj, this.getActivityNextId());
        try
        {
            proj.addActivity(act, signedIn);
            // Prints that the activity has been added
            System.out.println("The activity, " + act.getName() + ", has been added to the project, " + proj.getName() + ", successfully!");
            viewActivityMenu(proj);
            return;
        }
        catch(Exception e){}
    }

    public void logHoursMenu(Activity act) // This function logs hours to an activity
    {

        System.out.println("Log hours menu for activity: " + act.getName() + "(ID: ebuc" + act.getId() + ")");
        LocalDate date = LocalDate.now();
        double hours;
        while(true) // Loops until the user enters a valid input
        {
            System.out.println("Do you want to log hours for today or another day?");
            System.out.println("1. Today");
            System.out.println("2. Another day");
            System.out.println("3. Go back");
            String s2 = scanner.nextLine();
            if(s2.equals("1"))
            {
                System.out.println("Logging hours for today");
            } else if(s2.equals("2"))
            {
                while(true)
                {
                    System.out.println("Enter the year for the date");
                    String syear = scanner.nextLine();
                    int year;
                    try
                    {
                        year = Integer.parseInt(syear);
                        if(year < 0 || year > 9999)
                        {
                            System.out.println("Invalid input, please write an integer between 0 and 9999");
                            continue;
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Invalid input, please write an integer");
                        continue;
                    }
                    System.out.println("Enter the month for the date as a number");
                    String smonth = scanner.nextLine();
                    int month;
                    try
                    {
                        month = Integer.parseInt(smonth);
                        if(month < 1 || month > 12)
                        {
                            System.out.println("Invalid input, please write an integer between 1 and 12");
                            continue;
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Invalid input, please write an integer");
                        continue;
                    }
                    System.out.println("Enter the day for the date as a number");
                    String sday = scanner.nextLine();
                    int day;
                    try
                    {
                        day = Integer.parseInt(sday);
                        if(day < 1 || day > 31)
                        {
                            System.out.println("Invalid input, please write an integer between 1 and 31");
                            continue;
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Invalid input, please write an integer");
                        continue;
                    }
                    try
                    {
                        date = LocalDate.of(year, month, day);  
                    }
                    catch(Exception e)
                    {
                        System.out.println("Invalid inputs, your values were not compatible with the calendar");
                        continue;
                    }
                    break;
                }
            }
            else if(s2.equals("3"))
            {
                viewActivityMenu(act.getProject());
                return;
            }
            else
            {
                System.out.println("Invalid input");
                continue;
            }
            while(true)
            {
                System.out.println("Enter how many hours you want to log. The input will be rounded down to nearest half hour.");
                String s3 = scanner.nextLine();
                try
                {
                    hours = Double.parseDouble(s3);  
                }
                catch(Exception e)
                {
                    System.out.println("Invalid input. Please try again");
                    continue;
                }
                if ((hours - (hours % 0.5)) >= 0.5)
                {
                    try
                    {
                        signedIn.logHours(act, date, (hours - (hours % 0.5)));
                        System.out.println("Hours have been logged successfully");
                        logHoursMenu(act);
                        return;
                    }
                    catch(Exception e){}
                }
                System.out.println("Invalid input. Please enter a positive number at least as big as 0.5");
            }
        }
    }

    public void sessionMenu()
    {
        System.out.println("View and edit sessions menu");
        String name;
        ArrayList<Session> sess = signedIn.getSessions();


        while(true)
        {
            for (int i = 0; i < 10 && i < sess.size(); i++)
            {
                Session ses = sess.get(i);
                System.out.print(i+1 + ":- ");
                System.out.println("Date: " + ses.getDate() + ", Length: " + ses.getLength() + ", Activity: " + findActivity(ses.getActId()).getName());
            }
            System.out.println("g: Go back");
            System.out.println("s: Search by year and month");
            System.out.println("Write the number of the session you want to edit, or the letters corresponding to one of the other options");
            name = scanner.nextLine();
            if(name.equals("g"))
            {
                scheduleMenu(currentWeeknum());
                return;
            }
            if(name.equals("s"))
            {
                while(true)
                {
                    System.out.println("Enter the year you want to search in");
                    String syear = scanner.nextLine();
                    int year;
                    try
                    {
                        year = Integer.parseInt(syear);
                        if(year < 0 || year > 9999)
                        {
                            System.out.println("Invalid input, please write an integer between 0 and 9999");
                            continue;
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Invalid input, please write an integer");
                        continue;
                    }
                    System.out.println("Enter the month you want to search in as a number between 1 and 12");
                    String smonth = scanner.nextLine();
                    int month;
                    try
                    {
                        month = Integer.parseInt(smonth);
                        if(month < 1 || month > 12)
                        {
                            System.out.println("Invalid input, please write an integer between 1 and 12");
                            continue;
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Invalid input, please write an integer");
                        continue;
                    }
                    ArrayList<Session> sessionlist = new ArrayList<Session>();
                    for(int i = 0; i < sess.size(); i++)
                    {
                        Session thisSession = sess.get(i);
                        if(thisSession.getDate().getYear() == year && thisSession.getDate().getMonthValue() == month)
                        {
                            sessionlist.add(thisSession);
                        }
                    }
                    if(sessionlist.size() == 0)
                    {
                        System.out.println("No sessions in the specified month");
                        sessionMenu();
                        return;
                    }
                    
                    while(true)
                    {
                        for(int i = 0; i < sessionlist.size(); i++)
                        {
                            Session thisSession = sessionlist.get(i);
                            System.out.print(i+1 + ":- ");
                            System.out.println("Date: " + thisSession.getDate() + ", Length: " + thisSession.getLength() + ", Activity: " + findActivity(thisSession.getActId()).getName());
                        }
                        System.out.println("Select the session you want to edit by writing its corresponding number");
                        System.out.println("Write g to go back");
                        String s = scanner.nextLine();
                        if(s.equals("g"))
                        {
                            sessionMenu();
                            return;
                        }
                        try
                        {
                            Session thisSession = sessionlist.get(Integer.parseInt(s) - 1);
                            editSessionMenu(thisSession);
                            return;
                        }
                        catch (Exception e)
                        {
                            System.out.println("Invalid input, please try again");
                        }
                    }
                }
            }
            int inputInt;
            try
            {
                inputInt = Integer.parseInt(name);
            }
            catch(Exception e)
            {
                System.out.println("Please enter a number");
                continue;
            }


            if(inputInt > 0 && inputInt <= sess.size())
            {
                editSessionMenu(sess.get(inputInt - 1));
                return;
            }
            
        }
    }

    public void editSessionMenu(Session theSes)
    {
        String name;
        while(true)
        {
            System.out.println("Session for activity: " + findActivity(theSes.getActId()).getName());
            System.out.println("Current length: " + theSes.getLength());
            System.out.println("Current date: " + theSes.getDate().toString());

            System.out.println("1: Change length");
            System.out.println("2: Change date");
            System.out.println("3: Delete session");
            System.out.println("4: Go back");
            name = scanner.nextLine();
            if(name.equals("1"))
            {
                System.out.println("Enter new length. The input will be rounded down to nearest half hour.");
                name = scanner.nextLine();
                double newLength;
                try
                {
                    newLength = Double.parseDouble(name);  
                }
                catch(Exception e)
                {
                    System.out.println("Invalid input. Please try again");
                    continue;
                }
                if ((newLength - (newLength % 0.5)) >= 0.5)
                {
                    theSes.setLength(newLength - (newLength % 0.5));
                    editSessionMenu(theSes);
                    return;
                }
                System.out.println("Invalid input. Please enter a positive number at least as big as 0.5");
                editSessionMenu(theSes);
                return;
            }
            if (name.equals("2"))
            {
                while(true)
                {
                    System.out.println("Enter the year for the new date");
                    String syear = scanner.nextLine();
                    int year;
                    try
                    {
                        year = Integer.parseInt(syear);
                        if(year < 0 || year > 9999)
                        {
                            System.out.println("Invalid input, please write an integer between 0 and 9999");
                            continue;
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Invalid input, please write an integer");
                        continue;
                    }
                    System.out.println("Enter the month for the new date as a number");
                    String smonth = scanner.nextLine();
                    int month;
                    try
                    {
                        month = Integer.parseInt(smonth);
                        if(month < 1 || month > 12)
                        {
                            System.out.println("Invalid input, please write an integer between 1 and 12");
                            continue;
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Invalid input, please write an integer");
                        continue;
                    }
                    System.out.println("Enter the day for the new date as a number");
                    String sday = scanner.nextLine();
                    int day;
                    try
                    {
                        day = Integer.parseInt(sday);
                        if(day < 1 || day > 31)
                        {
                            System.out.println("Invalid input, please write an integer between 1 and 31");
                            continue;
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Invalid input, please write an integer");
                        continue;
                    }
                    LocalDate date;
                    try
                    {
                        date = LocalDate.of(year, month, day);  
                    }
                    catch(Exception e)
                    {
                        System.out.println("Invalid inputs, your values were not compatible with the calendar");
                        continue;
                    }
                    theSes.setDate(date);
                    editSessionMenu(theSes);
                    return;
                }
            }
            if(name.equals("3"))
            {
                Activity act = findActivity(theSes.getActId());
                signedIn.deleteSession(theSes, act);
                System.out.println("Session deleted successfully");
                sessionMenu();
                return;
            }
            if(name.equals("4"))
            {
                sessionMenu();
                return;
            }
            System.out.println("Invalid input, please try again");
        }
    }

    public void createProjectMenu()
    {
        System.out.println("Create project menu");
        String name;
        while(true)
        {
            System.out.println("Please enter a name for the project. Cannot contain commas");
            name = scanner.nextLine();
            if(!name.contains(",") && (!(name.length() > 50) && (name.length() > 0)))
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

    public void viewActivityMenu(Project proj)
    {
        if(proj.isProjectLeader(signedIn))
        {
            viewActivityMenuAsLeader(proj);
            return;
        }
        printActivitiesOverview(proj);
        System.out.println("");
        while(true)
        {
            System.out.println("1. Log hours on activity");
            System.out.println("2. Back");
            System.out.println("Choose option by its corresponding number");
            String s = scanner.nextLine();
            if(s.equals("1"))
            {
                ArrayList<Activity> acts = proj.getActivities();
                while(true)
                {
                    for(int i = 0; i < acts.size(); i++)
                    {
                        System.out.println(Integer.toString(i + 1) + ". " + acts.get(i).getName());
                    }
                    System.out.println("");
                    System.out.println("Choose activity you want to log hours on by its number");
                    String s2 = scanner.nextLine();
                    try
                    {
                        int i = Integer.parseInt(s2) - 1;
                        Activity act = acts.get(i);
                        logHoursMenu(act);
                        return;
                    }
                    catch(Exception e)
                    {
                        System.out.println("Invalid input. Please try again");
                    }
                }
                
            }
            if(s.equals("2"))
            {
                viewProjectMenu(proj);
                return;
            }
            System.out.println("Invalid input. Please try again");
        }
    }

    public void viewActivityMenuAsLeader(Project proj)
    {
        System.out.println(proj.getName() + " Activity menu");
        printActivitiesOverview(proj);
        System.out.println("");
        while(true)
        {
            System.out.println("1. Log hours on activity");
            System.out.println("2. Add activity");
            System.out.println("3. Delete activity");
            System.out.println("4. Edit activity");
            System.out.println("5. Go back");
            System.out.println("Choose option by its corresponding number");
            String s = scanner.nextLine();
            ArrayList<Activity> acts = proj.getActivities();
            if(s.equals("1"))
            {
                while(true)
                {
                    for(int i = 0; i < acts.size(); i++)
                    {
                        System.out.println(Integer.toString(i + 1) + ". " + acts.get(i).getName());
                    }
                    System.out.println("");
                    System.out.println("Choose activity you want to log hours on by its number");
                    String s2 = scanner.nextLine();
                    try
                    {
                        int i = Integer.parseInt(s2) - 1;
                        Activity act = acts.get(i);
                        logHoursMenu(act);
                        return;
                    }
                    catch(Exception e)
                    {
                        System.out.println("Invalid input. Please try again");
                    }
                }
            }
            if(s.equals("2"))
            {
                addActivityMenu(proj);
                return;
            }
            if(s.equals("3"))
            {
                while(true)
                {
                    for(int i = 0; i < acts.size(); i++)
                    {
                        System.out.println(Integer.toString(i + 1) + ". " + acts.get(i).getName());
                    }
                    System.out.println("");
                    System.out.println("Choose activity you want to delete by its number");
                    String s2 = scanner.nextLine();
                    try
                    {
                        int i = Integer.parseInt(s2) - 1;
                        Activity act = acts.get(i);
                        proj.deleteActivity(act);
                        viewActivityMenu(proj);
                        return;
                    }
                    catch(Exception e)
                    {
                        System.out.println("Invalid input. Please try again");
                    }
                }
            }
            if(s.equals("4"))
            {
                while(true)
                {
                    for(int i = 0; i < acts.size(); i++)
                    {
                        System.out.println(Integer.toString(i + 1) + ". " + acts.get(i).getName());
                    }
                    System.out.println("");
                    System.out.println("Choose activity you want to edit by its number");
                    String s2 = scanner.nextLine();
                    try
                    {
                        int i = Integer.parseInt(s2) - 1;
                        Activity act = acts.get(i);
                        editActivityMenu(act);
                        return;
                    }
                    catch(Exception e)
                    {
                        System.out.println("Invalid input. Please try again");
                    }
                }
            }
            if(s.equals("5"))
            {
                viewProjectMenu(proj);
                return;
            }
            System.out.println("Invalid input. Please try again");
        }
    }

    public void editActivityMenu(Activity act)
    {
        System.out.println("Edit activity: " + act.getName());
        System.out.println("Current activity details:");
        System.out.println("From project " + act.getProject().getName() + " (" + Integer.toString(act.getProject().getId()) + ")");
        int[] start = yearSlashWeek(act.getStartDate());
        System.out.println("Start date: Week " + Integer.toString(start[1]) + ", " + Integer.toString(start[0]));
        int[] end = yearSlashWeek(act.getEndDate());
        System.out.println("End date: Week " + Integer.toString(end[1]) + ", " + Integer.toString(end[0]));
        System.out.println("Estimated hours of work: " + Double.toString(act.getHourEstimate()));
        System.out.println("Hours of work done on the activity: " + Double.toString(act.computeHoursSpent()));
        ArrayList<Developer> devs = act.getDeveloperList();
        System.out.println("Assigned developers:");
        if(devs.size() == 0)
        {
            System.out.println("    No developers assigned to activity");
        }
        for(int i = 0; i < devs.size(); i++)
        {
            Developer dev = devs.get(i);
            System.out.println("    " + dev.getName() + " (" + dev.getId() + ")");
        }
        int proc = act.getProcess();
        String s = "";
        switch(proc)
        {
            case 0: s = "Activity is planned";
                    break;
            case 1: s = "Activity is under development";
                    break;
            case 2: s = "Activity is done";
                    break;
        }
        System.out.println("Activity status: " + s);
        while(true)
        {
            System.out.println("1. Edit name");
            System.out.println("2. Edit start date");
            System.out.println("3. Edit end date");
            System.out.println("4. Edit estimated hours");
            System.out.println("5. Edit activity status");
            System.out.println("6. Add developer to activity");
            System.out.println("7. Unassign developer from activity");
            System.out.println("8. Go back");
            System.out.println("Choose option by its corresponding number");
            s = scanner.nextLine();
            if(s.equals("1"))
            {
                String name;
                while(true) // Loops until the user enters a valid name
                {
                    System.out.println("Please enter a new name for the activity");
                    name = scanner.nextLine();
                    if(!(name.length() > 50) && (name.length() > 0))
                    {
                        break;
                    }
                    System.out.println("Name is too long or not given. Please try again");
                }
                act.setName(name);
                editActivityMenu(act);
                return;
            }
            if(s.equals("2"))
            {
                int startDate;
                while (true) // Loops until the user enters a valid start date
                {
                    System.out.println("Please enter a start time for the activity in the form of a year and week numbar");
                    System.out.println("Do this in the format yyyyww. Eg: 202304");
                    String sdateString = scanner.nextLine();
                    try
                    {
                        startDate = Integer.parseInt(sdateString);
                        int[] yearweek = yearSlashWeek(startDate);
                        Calendar calendar = Calendar.getInstance(new Locale("dan", "dk"));
                        calendar.set(yearweek[0], 11, 31);
                        int checkWeek = calendar.get(Calendar.WEEK_OF_YEAR);
                        if(startDate < act.getEndDate() && (sdateString.length() == 6 && (yearweek[1] <= checkWeek && yearweek[1] > 0)))
                        {
                            act.setStartEndDate(startDate, act.getEndDate());
                            editActivityMenu(act);
                            return;
                        }
                    }
                    catch(Exception e){}
                    System.out.println("Invalid start date. Please try again.");
                }
            }
            if(s.equals("3"))
            {
                int endDate;
                while (true) // Loops until the user enters a valid start date
                {
                    System.out.println("Please enter a end time for the activity in the form of a year and week numbar");
                    System.out.println("Do this in the format yyyyww. Eg: 202304");
                    String sdateString = scanner.nextLine();
                    try
                    {
                        endDate = Integer.parseInt(sdateString);
                        int[] yearweek = yearSlashWeek(endDate);
                        Calendar calendar = Calendar.getInstance(new Locale("dan", "dk"));
                        calendar.set(yearweek[0], 11, 31);
                        int checkWeek = calendar.get(Calendar.WEEK_OF_YEAR);
                        if(endDate > act.getStartDate() && (sdateString.length() == 6 && (yearweek[1] <= checkWeek && yearweek[1] > 0)))
                        {
                            act.setStartEndDate(act.getStartDate(), endDate);
                            editActivityMenu(act);
                            return;
                        }
                    }
                    catch(Exception e){}
                    System.out.println("Invalid end date. Please try again.");
                }
            }
            if(s.equals("4"))
            {
                double hours;
                while(true) // Loops until the user enters a valid estimated hours
                {
                    System.out.println("Please enter an estimated amount of hours for the activity (as a positive number the format 0.0. Eg. 1.5 for one and a half hour):");
                    String s2 = scanner.nextLine();
                    try
                    {
                        hours = (double) Double.parseDouble(s2);
                        if(hours > 0) // Checks if the hours are positive
                        {
                            act.setHourEst(hours);
                            editActivityMenu(act);
                            return;
                        } // If the hours are not valid, the user is asked to try again
                        System.out.println("Invalid input. Please enter a positive number");
                    }
                    catch(Exception e)
                    {
                        System.out.println("Invalid input. Please try again");
                    }
                }
            }
            if(s.equals("5"))
            {
                int status;
                while(true) // Loops until the user enters a valid estimated hours
                {
                    
                    System.out.println("Please select what you want to update the status to by selecting its corresponding number");
                    System.out.println("1. Planned");
                    System.out.println("2. Under development");
                    System.out.println("3. Done");
                    String s2 = scanner.nextLine();
                    try
                    {
                        if(s2.equals("1") || (s2.equals("2") || s2.equals("3"))) // Checks if the hours are positive
                        {
                            status = Integer.parseInt(s2) - 1;
                            act.setProcessInfo(status);
                            editActivityMenu(act);
                            return;
                        } // If the hours are not valid, the user is asked to try again
                    }
                    catch(Exception e){}
                    System.out.println("Invalid input. Please try again");
                }
            }
            if(s.equals("6"))
            {
                addDeveloperMenu(act);
                return;
            }
            if(s.equals("7"))
            {
                deleteDeveloperMenu(act);
            }
            if(s.equals("8"))
            {
                viewActivityMenu(act.getProject());
                return;
            }
            System.out.println("Invalid input. Please try again");
        }
    }

    public void addDeveloperMenu(Activity act)
    {
        while(true)
        {
            System.out.println("Current developers assigned to activity " + act.getName());
            ArrayList<Developer> devs = act.getDeveloperList();
            if (devs.size() == 0) 
            {
                String noDevelopersString = "No developers assigned to the activity";
                System.out.println(noDevelopersString);
            }
            else 
            {
                for (int j = 0; j < devs.size(); j++) 
                {
                    String developerName = devs.get(j).getName();
                    String developerId = devs.get(j).getId();
                    System.out.println(developerName + " (" + developerId + ")");
                }
            }
            System.out.println("1. Add specific developer");
            System.out.println("2. View available developers to add");
            System.out.println("3. Go back");
            System.out.println("Choose option by selecting the corresponding number");
            String s = scanner.nextLine();
            if(s.equals("1"))
            {
                while(true)
                {
                    System.out.println("Write the user ID of the developer you want to add");
                    System.out.println("Write 'q' to go back");
                    String s2 = scanner.nextLine();
                    if(s2.equals("q"))
                    {
                        addDeveloperMenu(act);
                        return;
                    }
                    try
                    {
                        Developer dev = getDeveloper(s2);
                        act.addDev(dev);
                        addDeveloperMenu(act);
                        return;
                    }
                    catch(Exception e)
                    {}
                    System.out.println("Invalid input. Please try again");
                }
            }
            if(s.equals("2"))
            {
                Map<Integer, ArrayList<Developer>> mappings = act.showAvailableDevs(getDeveloperList());
                SortedSet<Integer> keys = new TreeSet<>(mappings.keySet());
                while(true)
                {
                    System.out.println("Here are the most available developers for the activity period which aren't currently assigned to it");
                    if(mappings.size() == 0)
                    {
                        System.out.println("No available developers");
                    }
                    for (int key: keys) 
                    {
                        ArrayList<Developer> devlist = mappings.get(key);
                        for(int i = 0; i < devlist.size(); i++)
                        {
                            System.out.println("Other activities in activity period = " + key +
                                ", developer = " + devlist.get(i).getName() + " (" + devlist.get(i).getId() + ")");
                        }
                        
                    }
                    System.out.println("Write the user ID of the developer you want to add");
                    System.out.println("Write 'q' to go back");
                    String s2 = scanner.nextLine();
                    if(s2.equals("q"))
                    {
                        addDeveloperMenu(act);
                        return;
                    }
                    try
                    {
                        Developer dev = getDeveloper(s2);
                        act.addDev(dev);
                        addDeveloperMenu(act);
                        return;
                    }
                    catch(Exception e){}
                    System.out.println("Invalid input. Please try again");
                }
            }
            if(s.equals("3"))
            {
                editActivityMenu(act);
                return;
            }
            System.out.println("Invalid input. Please try again");
        }
    }

    public void deleteDeveloperMenu(Activity act)
    {
        while(true)
        {
            System.out.println("Current developers assigned to activity " + act.getName());
            ArrayList<Developer> devs = act.getDeveloperList();
            if (devs.size() == 0) 
            {
                String noDevelopersString = "No developers assigned to the activity";
                System.out.println(noDevelopersString);
            }
            else 
            {
                for (int j = 0; j < devs.size(); j++) 
                {
                    String developerName = devs.get(j).getName();
                    String developerId = devs.get(j).getId();
                    System.out.println(Integer.toString( + 1) + ". " + developerName + " (" + developerId + ")");

                }
                System.out.println("Write the number of the developer you want to unassign from activity");
            }
            System.out.println("Write " + Integer.toString(devs.size() + 1) + " to go back");
            String s = scanner.nextLine();
            if(s.equals(Integer.toString(devs.size() + 1)))
            {
                editActivityMenu(act);
                return;
            }
            try
            {
                Developer dev = devs.get(Integer.parseInt(s) - 1);
                act.removeDev(dev);
                deleteDeveloperMenu(act);
                return;
            }
            catch(Exception e){}
            System.out.println("Invalid input. Please try again");
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

    public LocalDate dateInput(String userInput)
    {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("m/d/yyyy");
        LocalDate date = LocalDate.parse(userInput, dateFormat);
        return date;
    }
    
    public void printProjectOverview(Project proj)
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
            int[] temp = yearSlashWeek(earliestDate);
            int earliestDateYear = temp[0];
            int earliestDateWeek = temp[1];


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
            temp = yearSlashWeek(latestDate);
            int latestDateYear = temp[0];
            int latestDateWeek = temp[1];

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
            ArrayList<Activity> plannedActivities = new ArrayList<Activity>();
            ArrayList<Activity> currentActivities = new ArrayList<Activity>();
            ArrayList<Activity> doneActivities = new ArrayList<Activity>();
            for (int i = 0; i < proj.getActivities().size(); i++) 
            {
                Activity act = proj.getActivities().get(i);
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
            System.out.print("Planned Activities:");
            for(int i = 0; i < 31; i++){System.out.print(" ");}
            System.out.print("Current Activities:");
            for(int i = 0; i < 31; i++){System.out.print(" ");}
            System.out.println("Done Activities:");
            for(int i = 0; i < maxSize; i++)
            {
                Activity pAct; 
                Activity cAct; 
                Activity dAct; 
                try
                {
                    pAct = plannedActivities.get(i);
                    System.out.print(pAct.getName());
                    for(int i2 = 0; i2 < 50 - pAct.getName().length(); i2++){System.out.print(" ");}
                }
                catch(Exception e){for(int i2 = 0; i2 < 50; i2++){System.out.print(" ");}};
                try
                {
                    cAct = currentActivities.get(i);
                    System.out.print(cAct.getName());
                    for(int i2 = 0; i2 < 50 - cAct.getName().length(); i2++){System.out.print(" ");}
                }
                catch(Exception e){for(int i2 = 0; i2 < 50; i2++){System.out.print(" ");}};
                try
                {
                    dAct = doneActivities.get(i);
                    System.out.println(dAct.getName());
                }
                catch(Exception e){System.out.println("");};
            }
        }
    }

    public void printActivitiesOverview(Project proj)
    {
        // Prints Activity Menu:
        System.out.println("Activities assigned to the project, " + proj.getName() + "(" + proj.getId() + ")" + ":");

        // Activity list with developers assigned, start date, end date, and hours spent on activity so far compared to estimated hours.

        for (int i = 0; i < proj.getActivities().size(); i++) 
        {
            String activityName = "     " + (i + 1) + ") " + proj.getActivities().get(i).getName();
            System.out.println(activityName);

            // Start date
            int[] yearweek = yearSlashWeek(proj.getActivities().get(i).getStartDate());
            String startDateString = "          Start date: Week " + Integer.toString(yearweek[1]) + ", " + Integer.toString(yearweek[0]);
            System.out.println(startDateString);

            // End date
            yearweek = yearSlashWeek(proj.getActivities().get(i).getEndDate());
            String endDateString = "          Start date: Week " + Integer.toString(yearweek[1]) + ", " + Integer.toString(yearweek[0]);
            System.out.println(endDateString);

            // Hours spent on activity so far
            String hoursSpentString = "          Hours spent on activity so far: " + proj.getActivities().get(i).computeHoursSpent();
            System.out.println(hoursSpentString);

            // Estimated hours
            String estHoursString = "          Estimated hours: " + proj.getActivities().get(i).getHourEstimate();
            System.out.println(estHoursString);

            int process = proj.getActivities().get(i).getProcess();
            String processName = "";
            switch(process)
                {
                    case 0: processName = "Planned";
                            break;
                    case 1: processName = "In progress";
                            break;
                    case 2: processName = "Done";
                            break;
                }
            System.out.println("          Status: " + processName);

            // Developers assigned to activity
            if (proj.getActivities().get(i).getDeveloperList().size() == 0) 
            {
                String noDevelopersString = "          No developers assigned to the activity";
                System.out.println(noDevelopersString);
            }
            else 
            {
                String developersAssignedString = "          Developers assigned to the activity: ";
                System.out.println(developersAssignedString);
                for (int j = 0; j < proj.getActivities().get(i).getDeveloperList().size(); j++) 
                {
                    String developerName = "               " + (j + 1) + ") " + proj.getActivities().get(i).getDeveloperList().get(j).getName();
                    System.out.println(developerName);
                }
            }
        }
    }
}
