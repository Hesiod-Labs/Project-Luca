import java.util.*;

public class Account
{
    private Balance accountBalance;
    private Set<User> allUsers;
    private Portfolio portfolio;
    
    public Account(Balance initialAmount)
    {
        this.accountBalance = initialAmount;
    }
    
    public void addUser(User user)
    {
        if(! allUsers.contains(user))
            allUsers.add(user);
        else
        allUsers.add(user);
    }
    
    
    
    /*
    private double totalFunds; // includes both portfolio and withdrawable funds
    private double portfolioValue; // current value of the portfolio (updated with profits and losses)
    private double portfolioFunds; // funds used to invest in the portfolio
    private double withdrawableFunds; // funds not being invested
    private ArrayList<User> allUsers; // list of all users associated with the account
    private Portfolio portfolio; // contains all assets to be traded
    
    public Account(double portfolioFunds, double withdrawableFunds)
    {
        this.portfolioFunds = portfolioFunds;
        this.withdrawableFunds = withdrawableFunds;
        this.totalFunds = portfolioFunds + withdrawableFunds;
    }
    
    public void directToPortfolio(double amount)
    {
    
    }
    
    public void transferToPortfolio(double amount)
    {
        updatePortfolioValue(getPortfolioValue() + amount);
        updateWithdrawableFunds(getWithdrawableFunds() - amount);
    }
    
    public void transferToWithDrawable(double amount)
    {
        updateWithdrawableFunds(getWithdrawableFunds() + amount);
        updatePortfolioValue(getPortfolioFunds() - amount);
    }
    
    public double getPortfolioFunds()
    {
        return portfolioFunds;
    }
    
    public double getTotalFunds()
    {
        return totalFunds;
    }
    
    public double getWithdrawableFunds()
    {
        return withdrawableFunds;
    }
    
    public void updatePortfolioValue(double portFolioValue)
    {
        this.portfolioValue = portFolioValue;
    }
    
    public void updateTotalFunds(double totalFunds)
    {
        this.totalFunds = getWithdrawableFunds() + getPortFolioValue();
    }
    
    public void updateWithdrawableFunds(double withdrawableFunds)
    {
        this.withdrawableFunds = withdrawableFunds;
    }
    
    public void addUser(User newUser)
    {
        allUsers.add(newUser);
    }
    
    public int numUsers(ArrayList<User> everyone)
    {
        int count = 0;
        
        for(User user : everyone)
            count++;
        
        return count;
    }
    */
}
