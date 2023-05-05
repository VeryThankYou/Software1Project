package application;

import java.time.LocalDate;

public class Session 

{
    private Double length;
    private LocalDate date;
    private int actid;

    public Session(Double newTime, LocalDate newDate, int newActid)
    {
        length = newTime;
        date = newDate;
        actid = newActid;
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

    public int getActId()
    {
        return actid;
    }
}
