import java.time.*;
import java.time.temporal.ChronoUnit;

public class Bank
{
    private static Balance bankBalance = new Balance();
    private static String bankName;
    
    public Bank(String name,Transaction firstTransaction)
    {
        bankName = name;
        Balance initial = new Balance(firstTransaction);
        bankBalance.updateBalance(initial);
    }
    
    public Bank(String name, double initialAmount)
    {
        bankName = name;
        Balance initial = new Balance(initialAmount);
        bankBalance.updateBalance(initial);
    }
    
    public Bank(String name)
    {
        bankName = name;
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
        
        User requestedBy = transaction.getRequestUser();
        
        requestedBy.getUserBalance().updateBalance(Balance.transferTo(transaction));
        getBankBalance().updateBalance(Balance.transferFrom(transaction));
    }
    
    public static Balance getBankBalance()
    {
        return bankBalance;
    }
    
    public static String getBankName()
    {
        return bankName;
    }
    
    public static void setBankBalance(Balance bankBalance)
    {
        Bank.bankBalance = bankBalance;
    }
    
    public static void setBankName(String bankName)
    {
        Bank.bankName = bankName;
    }
}
