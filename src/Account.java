import java.util.*;

/** An account within Luca. */
public class Account
{
    /** The amount of funds in both the Bank and Portfolio. */
    private static Balance accountBalance; // TODO Write method that adds portfolio and bank balances
    
    /** The Bank holds funds not for trading. */
    private static Bank bank;
    
    /** The Portfolio holds assets and funds used for trading. */
    private static Portfolio portfolio;
    
    /** All users associated with the account. */
    // TODO Set is an interface
    private static SortedSet<User> allUsers;
    
    public Account(Balance initialAmount, Bank b, SortedSet<User> users, Portfolio port)
    {
        accountBalance = initialAmount;
        bank = b;
        allUsers = users;
        portfolio = port;
    }
    
    /** Constructor that creates an empty account (i.e. no Balance, Bank, User(s), or Portfolio) */
    public Account()
    {
        this(null, null, null, null);
    }
    
    /**
     * //TODO Requires admin permissions.
     * Adds a user to the account.
     * @param user User to be added.
     */
    public static void addUser(User user)
    {
        getAllUsers().add(user);
    }
    
    /**
     * //TODO Requires admin permissions.
     * Removes a user from the account.
     * @param user User to be removed.
     */
    public static void removeUser(User user)
    {
        getAllUsers().remove(user);
    }
    
    //******************************** GETTER METHODS ********************************//
    
    public static Balance getAccountBalance()
    {
        return accountBalance;
    }
    
    public static Bank getBank()
    {
        return bank;
    }
    
    public static Portfolio getPortfolio()
    {
        return portfolio;
    }
    
    public static Set<User> getAllUsers()
    {
        return allUsers;
    }
    
    //******************************** SETTER METHODS ********************************//
    
    public static void setAccountBalance(Balance accountBalance)
    {
        Account.accountBalance = accountBalance;
    }
    
    public static void setBank(Bank bank)
    {
        Account.bank = bank;
    }
    
    public static void setPortfolio(Portfolio portfolio)
    {
        Account.portfolio = portfolio;
    }
    
    public static void setAllUsers(SortedSet<User> allUsers)
    {
        Account.allUsers = allUsers;
    }
}
