package application;

public class UserNotLeaderException extends Exception
{
    public UserNotLeaderException(String errString)
    {
        super(errString);
    }
}
