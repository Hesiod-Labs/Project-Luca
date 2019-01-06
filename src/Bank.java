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
    
    /**
     * Formats ZonedDateTime to be more readable.
     *
     * @param time The time to be formatted.
     * @return Formatted ZonedDateTime.
     */
    public static String formatTime(ZonedDateTime time)
    {
        String month = time.getMonth().toString();
        String day = Integer.toString(time.getDayOfMonth());
        String year = Integer.toString(time.getYear());
        String hour = Integer.toString(time.getHour());
        String minute = Integer.toString(time.getMinute());
        String second = Integer.toString(time.getSecond());
        String timeZone = time.getZone().toString();
        
        return month + " " + day + ", " + year + " (" + hour + ":" + minute + ":" + second + " " + timeZone + ")";
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
