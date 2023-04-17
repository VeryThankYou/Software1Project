package application;

import java.time.LocalDate;

public class Session 

{

    private Developer developer;
    private Activity activity;
    private Double length;
    private LocalDate date;

    public void updateInfo()
    {
    // Get current date
    LocalDate currentDate = LocalDate.now();

    // Update date to current date
    setDate(currentDate);

    // Update length based on developer's activity
    if (activity == Activity.HOUR) 
    {
        setLength(length + 1.0); // Add 1.0 to the current length for hour activity
    } else if (activity == Activity.HALFHOUR) 
    {
        setLength(length + 0.5); // Add 0.5 to the current length for halfhour activity
    }

    // Display updated information
    System.out.println("Session updated: Developer: " + developer.getName() +
            ", Activity: " + activity + ", Length: " + length + ", Date: " + date);
    }
}
