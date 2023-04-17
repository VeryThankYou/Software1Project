package application;

import java.time.LocalDate;

public class Session 

{
    private Double length;
    private LocalDate date;

    public Session(Double newTime, LocalDate newDate)
    {
        length = newTime;
        date = newDate;
    }

    public void setDate(LocalDate newDate)
    {
        date = newDate;
    }

    public void setLength(double newTime)
    {
        length = newTime;
    }

    public double getLength()
    {
        return length;
    }

    public LocalDate getDate()
    {
        return date;
    }
}
