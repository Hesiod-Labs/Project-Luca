import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Bank implements Banking
{
    private static Balance bankBalance;
    private ArrayList<Transaction> transactionHistory;
    
    public Bank()
    {
        // TODO Decide what to put in here
    }
    
    // TODO Remember that each transaction type will be used in the requestTransaction and makeTransaction methods.
    //  So write these as the ACTIONS (assuming privileges have already been checked)
    // Deposits money into the bank and updates the bankBalance
    // TODO Figure out how to connect Bank and Portfolio to allow transactions between them
    public void deposit(Transaction transaction) // TODO Make into try/catch to consider negative amounts
    {
        bankBalance.updateBalance(transaction);
        transaction.setExeDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
        transaction.setStatus(Transaction.Status.DEPOSITED);
    }
    
    public void withdraw(Transaction transaction)
    {
        bankBalance.updateBalance(transaction);
        transaction.setExeDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
        transaction.setStatus(Transaction.Status.WITHDRAWN);
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
