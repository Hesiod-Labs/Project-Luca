import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Stack;

/**
 * Last edited: 12/30/18 20:48
 * Represents the balance associated with either a user, portfolio, or bank
 */
public class Balance
{
    private double current;
    private double amount;
    private ZonedDateTime balanceTimeStamp;
    private static Stack<Double> balanceHistory;
    
    public Balance(double amount)
    {
        this.amount = amount;
        this.balanceTimeStamp = ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS);
        balanceHistory.add(amount);
    }
    
    public void updateBalance(Transaction transaction)
    {
        Transaction.Type type = transaction.getType();
        switch(type) {
            case DEPOSIT: setCurrent(getCurrent() + amount);
            case WITHDRAW: setCurrent(getCurrent() - amount);
            case BUY: setCurrent(getCurrent() + amount);
            case SELL: setCurrent(getCurrent() - amount);
            case SHORT: setCurrent(getCurrent() + amount);
            case COVER: setCurrent(getCurrent() - amount);
        }
        balanceHistory.add(amount);
    }
    
    public double getCurrent()
    {
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
    
    public void setCurrent(double current)
    {
        this.current = current;
    }
}
