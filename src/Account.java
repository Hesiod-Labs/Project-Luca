import java.util.*;

public class Account
{
    private static Balance accountBalance;
    private static Bank bank;
    private static  Set<User> allUsers;
    private static Portfolio portfolio;
    private static Queue<Transaction> transactionRequests;
    private static Stack<Transaction> transactionHistory;
    
    public Account(Balance initialAmount, Bank bank, Set<User> users, Portfolio portfolio)
    {
        Account.accountBalance = initialAmount;
        Account.bank = bank;
        Account.allUsers = users;
        Account.portfolio = portfolio;
    }
    
    public void requestTransaction(Transaction transaction)
    {
        transactionRequests.add(transaction);
    }
    
    public void makeTransaction(Transaction.Status updatedStatus)
    {
        // 1. Check to see the type of the transaction
        // 2. Update bank and portfolio balances // TODO make accountBalance update method
        // 3. 
        Transaction request = transactionRequests.poll();
        request.setStatus(updatedStatus);
    
        Transaction.Type type = null;
        switch(type)
        {
            case DEPOSIT: bank.deposit(request);
                break;
            case WITHDRAW: bank.withdraw(request);
                break;
            case BUY: portfolio.buyOrder(request);
                break;
            case SELL: portfolio.sellOrder(request);
                break;
            case SHORT: portfolio.shortOrder(request);
                break;
            case COVER: portfolio.coverOrder(request);
                break;
        }
        
        transactionHistory.add(request);
        // Account.updateBalance(request);
        
    }
    
    public void addUser(User user)
    {
        if(! allUsers.contains(user))
            allUsers.add(user);
        else
        allUsers.add(user);
    }
    
    // TODO make try/catch with exception if no user is found
    public void removeUser(User user)
    {
        if(!allUsers.contains(user))
            System.out.println("No such user exists.");
        else
            allUsers.remove(user);
    }
    
    public Balance getAccountBalance()
    {
        return accountBalance;
    }
    
    public Bank getBank()
    {
        return bank;
    }
    
    public Set<User> getAllUsers()
    {
        return allUsers;
    }
    
    public Portfolio getPortfolio()
    {
        return portfolio;
    }
    
    public void setAccountBalance(Balance accountBalance)
    {
        this.accountBalance = accountBalance;
    }
    
    public void setBank(Bank bank)
    {
        this.bank = bank;
    }
    
    public void setAllUsers(Set<User> allUsers)
    {
        this.allUsers = allUsers;
    }
    
    public void setPortfolio(Portfolio portfolio)
    {
        this.portfolio = portfolio;
    }
}
