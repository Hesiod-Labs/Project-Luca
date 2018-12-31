import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Stack;

/**
 * Last edited: 12/30/18 20:48
 * Represents the balance associated with either a user, portfolio, or bank
 */
public class Balance
{
    private double amount;
    private ZonedDateTime balanceTimeStamp;
    private static Stack<Double> balanceHistory;
    
    public Balance(double amount)
    {
        this.amount = amount;
        this.balanceTimeStamp = ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS);
        balanceHistory.add(amount);
    }
    
    public double getCurrentAmount()
    {
        double current = balanceHistory.pop();
        return current;
    }
    
    public double getAmount()
    {
        return amount;
    }
    
    public ZonedDateTime getBalanceTimeStamp()
    {
        return balanceTimeStamp;
    }
    
    public void setAmount(double amount)
    {
        this.amount = amount;
    }
    
    public void setBalanceTimeStamp(ZonedDateTime balanceTimeStamp)
    {
        this.balanceTimeStamp = balanceTimeStamp;
    }
    
}
