package application;
import java.util.Arrays;
import java.util.Collections;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.*;
import javax.naming.directory.SearchResult;

import io.cucumber.docstring.DocStringTypeRegistryDocStringConverter;

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
        if(proj.isProjectLeader(signedIn))
        {
            viewProjectMenuAsLeader(proj);
            return;
        }
        printProjectOverview(proj);
        while(true)
        {
            System.out.println("1. View activity");
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
            System.out.println("1. View activity");
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
                    System.out.println("What name do you want to give the project? (Max 50 chars, min 1)");
                    String s2 = scanner.nextLine();
                    if(s2.length() <= 50 && s2.length() > 1)
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
            System.out.println("Please enter a name for the activity");
            name = scanner.nextLine();
            if(!(name.length() > 50) && (name.length() > 0))
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
                System.out.println("Please enter the date you want to log hours for (in the format dd/MM/yyyy):");
                date = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
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

            System.out.println("Please enter the amount of hours you want to log (as a positiv number between 0-24 hours in the format 0.0. Eg. 1.5 for one and a half hour):");
            String s = scanner.nextLine();
            try
            {
                boolean addMore = false;
                do {

                double hours = (double) Double.parseDouble(s);
                signedIn.markHours(act, date, hours); // markHours function from the Developer class
                System.out.println("Hours have been logged, do you want to log more hours?");
                
                System.out.println("1. Yes");
                System.out.println("2. No");
                String s3 = scanner.nextLine();
                if(s3.equals("1"))
                {
                    addMore = true;
                }
                else if(s3.equals("2"))
                {
                    addMore = false;
                }
                else
                {
                    System.out.println("Invalid input");
                }
                
                } while (addMore);

                break;
            }
            catch(Exception e)
            {
                System.out.println("Invalid input. Please try again");
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
                System.out.println("Date: " + ses.getDate() + "Length: " + ses.getLength() );
            }
            System.out.println("11: Go back");
            System.out.println("12: Search by month");
            name = scanner.nextLine();
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


            if(inputInt > 0 && inputInt < sess.size())
            {
                editSessionMenu(sess.get(inputInt));
            }
            if(inputInt == 11)
            {
                scheduleMenu(currentWeeknum());
                return;
            }
            if(inputInt == 12)
            {
                while(true)
                {
                    System.out.println("Enter the number of the month, e.g. 3 or 12, to search for sessions in that month");
                    name = scanner.nextLine();
                    if (inputInt < 0 || inputInt > 12)
                    {
                        continue;
                    }
                    ArrayList<Session> searchedSess = signedIn.searchSessionsByMonth(inputInt);
                    for(int i = 0; i < searchedSess.size(); i++)
                    {
                        Session ses = searchedSess.get(i);
                        System.out.print(i+1 + ":- ");
                        System.out.println("Date: " + ses.getDate() + "Length: " + ses.getLength() );
                    }
                    name = scanner.nextLine();
                    int searchInputInt;
                    try
                    {
                        searchInputInt = Integer.parseInt(name);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Please enter a number");
                        continue;
                    }
                    if(searchInputInt > 0 && searchInputInt < sess.size())
                    {
                        editSessionMenu(sess.get(searchInputInt));
                        break;
                    }
                }
            }
        }
    }

    public void editSessionMenu(Session theSes)
    {
        String name;
        while(true)
        {
            System.out.println("1: Change length");
            System.out.println("2: Change date");
            name = scanner.nextLine();
            if(name.equals("1"))
            {
                System.out.println("Enter new length");
                name = scanner.nextLine();
                float newLength;
                try
                {
                    newLength = Float.parseFloat(name);  
                }
                catch(Exception e)
                {
                    System.out.println("Please enter a multiple of 0.5");
                    continue;
                }
                if (newLength % 0.5 == 0)
                {
                    System.out.println("Please enter a multiple of 0.5");
                    continue;
                }
                theSes.setLength(newLength);
                break;
            }
            if (name.equals("2"))
            {
                System.out.println("Enter new date in d/m/yyyy");
                name = scanner.nextLine();
                LocalDate date;
                try
                {
                    date = dateInput(name);  
                }
                catch(Exception e)
                {
                    System.out.println("Please enter a number with format d/m/yyyy");
                    continue;
                }
                theSes.setDate(date);
                break;
            }
        }
        System.out.println("Date: " + theSes.getDate() + "Length: " + theSes.getLength() );
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
                    System.out.println("Choose activity you want to mark hours on by its number");
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
                    System.out.println("Choose activity you want to mark hours on by its number");
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
        System.out.println("Start date: Week " + Integer.toString(start[1]) + ", " + Integer.toString(start[1]));
        int[] end = yearSlashWeek(act.getStartDate());
        System.out.println("Start date: Week " + Integer.toString(end[1]) + ", " + Integer.toString(end[1]));
        
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
            temp = yearSlashWeek(earliestDate);
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
                catch(Exception e){};
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
