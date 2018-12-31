import java.util.ArrayList;

public class Bank
{
    private static Balance bankBalance;
    private ArrayList<Transaction> transactionHistory;
    
    public Bank()
    {
        // TODO Decide what to put in here
    }
    
    public static Balance getBankBalance()
    {
        return bankBalance;
    }
    
    public static void setBankBalance(Balance bankBalance)
    {
        Bank.bankBalance = bankBalance;
    }
    
}
