import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Stack;

/**
 * Last edited: 12/30/18 20:48
 * Represents the balance associated with either a user, portfolio, or bank
 */
public class Balance
{
    private double currentAmount = 0;
    private ZonedDateTime balanceTimeStamp;
    private static Stack<> balanceHistory; // TODO reconsider having the Stack be made of Transactions
    // private ZonedDateTime balanceTimeStamp; The time stamp associated with the exeDate should be fine
    
    public Balance() // TODO probably need to make this not an empty constructor
    {
       // this.currentAmount; // TODO update current amount to represent current balance
        // balanceHistory.push(something needs to go in here)
        this.balanceTimeStamp = ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS);
    }
    
    public double getCurrentAmount()
    {
        return currentAmount;
    }
    
    public static Stack<Transaction> getBalanceHistory()
    {
        return balanceHistory;
    }
}
