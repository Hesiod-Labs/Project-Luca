import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Bank implements Banking
{
    private static Balance bankBalance;
    private static ArrayList<Transaction> transactionHistory;
    
    public Bank(Balance balance)
    {
        Bank.bankBalance = balance;
    }
    
    // TODO Remember that each transaction type will be used in the requestTransaction and makeTransaction methods.
    //  So write these as the ACTIONS (assuming privileges have already been checked)
    // Deposits money into the bank and updates the bankBalance
    public void deposit(Transaction transaction) // TODO Make into try/catch to consider negative amounts
    {
        getBankBalance().updateBalance(transaction);
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
    
}
