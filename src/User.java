import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Represents a user of the accounting tool, Luca
 * @author hLabs
 */
public class User
{
    private String username; // Initials of first, middle, and last names, plus three random numbers (e.g. ABC123)
    private String password; // TODO for now, this is input by the user, but should be randomly generated for security
    private String firstName;
    private String middleInit;
    private String lastName;
    private ZonedDateTime timeCreated; // Timestamp at which point the user was created in Luca
    private String userStatus; // REGULAR or ADMIN
    private boolean adminPriv; // True if ADMIN and False if REGULAR // TODO will need more formal Java Security implementation
    private double contribution; // Amount user has invested into the portfolio
    private Balance userBalance; // The amount user has. // TODO Decide if this will represent current value of investment or something else
    private Set<User> allUsers; // TODO Determine if this should be in User or Account class
    
    /**
     * Main method that prints out the user information
     * @param args
     */
    public static void main(String[] args)
    {
        User user = new User("Ryan", "D", "Tatton", "dog", true, 500);
        System.out.println("Name: " + user.getFirstName().toUpperCase() + " " + user.getMiddleInit().toUpperCase() + "." + " " + user.getLastName().toUpperCase());
        System.out.println("Username: " + user.getUsername() + " (" + user.getUserStatus() + ")");
        System.out.println("Password: " + user.getPassword());
        System.out.println("Amount Invested: $" + user.getContribution());
        System.out.println("Time of User Creation: " + user.formattedTimeCreated(user.getTimeCreated()));
    }
    
    /**
     * Constructor for initializing a user
     * @param first First name of the user (case does not matter)
     * @param middleInit Middle initial of the user (case does not matter)
     * @param last Last name of the user (case does not matter)
     * @param password Password for logging in
     * @param adminPriv Boolean that allows for admin privileges
     * @param contribution Amount of money invested into Hesiod Financial
     */
    public User(String first, String middleInit, String last, String password, boolean adminPriv, double contribution)
    {
        this.firstName = first.toUpperCase();
        this.middleInit = middleInit.toUpperCase();
        this.lastName = last.toUpperCase();
        
        String userArray[] = new String[6]; // Array to hold elements to create username
        
        userArray[0] = String.valueOf(first.charAt(0)); // first name initial
        userArray[1] = String.valueOf(middleInit.charAt(0)); // middle name initial
        userArray[2] = String.valueOf(last.charAt(0)); // last name initial
        
        Random randomGen = new Random(); // To generate the three random numbers following the user initials
    
        // Generates three random numbers and adds them to userArray. Numbers generated are between 0 and 9
        IntStream.range(3, 6).forEach(i -> {
            int num = randomGen.nextInt(9);
            userArray[i] = Integer.toString(num);
        });
        
        // To concatenate the initials and numbers
        StringBuilder nameBuilder = new StringBuilder();
        String name;
        
        // Create the username and assign it to the user
        for(String s : userArray)
        {
            nameBuilder.append(s);
            name = nameBuilder.toString();
            this.username = name;
        }
        
        this.password = password;
        this.timeCreated = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        
        // Allows or denies ADMIN privileges
        if(adminPriv)
            this.userStatus = UserStatus.ADMIN.name();
        else
            this.userStatus = UserStatus.REGULAR.name();
        
        this.contribution = contribution;
    }
    
    public enum UserStatus
    {
        REGULAR, ADMIN
    }
    
    public void changeAdminAccess(User user, boolean access)
    {
        if(access)
            user.setAdminPriv(true);
        else
            user.setAdminPriv(false);
    }
    
    // Standard getter/setter methods and a method to format ZoneDateTime output
    public String getUsername()
    {
        return username;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public String getFirstName()
    {
        return firstName;
    }
    
    public String getMiddleInit()
    {
        return middleInit;
    }
    
    public String getLastName()
    {
        return lastName;
    }
    
    public String getUserStatus()
    {
        return userStatus;
    }
    
    public double getContribution()
    {
        return contribution;
    }
    
    public ZonedDateTime getTimeCreated()
    {
        return timeCreated;
    }
    
    public boolean getAdminPriv()
    {
        return adminPriv;
    }
    
    public String formattedTimeCreated(ZonedDateTime time)
    {
        String month = time.getMonth().toString();
        String day = Integer.toString(time.getDayOfMonth());
        String year = Integer.toString(time.getYear());
        String hour  = Integer.toString(time.getHour());
        String minute = Integer.toString(time.getMinute());
        String second = Integer.toString(time.getSecond());
        String timeZone = time.getZone().toString();
        
        return month + " " + day + ", " + year + " (" + hour + ":" + minute + ":" + second + " " + timeZone + ")";
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public void setAdminPriv(boolean adminPriv)
    {
        this.adminPriv = adminPriv;
    }
    
}
