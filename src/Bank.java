import java.time.*;
import java.time.temporal.ChronoUnit;

public class Bank
{
    private static Balance bankBalance = new Balance();
    
    public Bank(Transaction firstTransaction)
    {
        Balance initial = new Balance(firstTransaction);
        bankBalance.updateBalance(initial);
    }
    
    public Bank(double initialAmount)
    {
        Balance initial = new Balance(initialAmount);
        bankBalance.updateBalance(initial);
    }
    
    public Bank()
    {
        Balance createdAt = new Balance();
        bankBalance.updateBalance(createdAt);
    }
    
    // Deposits money into the bank and updates the bankBalance
    public static void deposit(Transaction transaction)
    {
        transaction.setResolveDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
        transaction.setTransactionStatus(Transaction.Status.DEPOSITED);
        
        getBankBalance().updateBalance(Balance.transferTo(transaction));
    }
    
    public static void withdraw(Transaction transaction)
    {
        transaction.setResolveDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
        transaction.setTransactionStatus(Transaction.Status.WITHDRAWN);
        
        User requestedBy = transaction.getReqUser();
        
        requestedBy.getUserBalance().updateBalance(Balance.transferTo(transaction));
        getBankBalance().updateBalance(Balance.transferFrom(transaction));
    }
    
    public static Balance getBankBalance()
    {
        return bankBalance;
    }
    
    public void setBankBalance(Balance bankBalance)
    {
        Bank.bankBalance = bankBalance;
    }
}
