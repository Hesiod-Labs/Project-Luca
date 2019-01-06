import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Stack;

/** Represents the balance associated with a user, portfolio, or bank. */
public class Balance
{
    /** Contains all previous balances of an entity. */
    private Stack<Balance> balanceHistory = new Stack<>();
    
    /** The amount associated with a specific balance in time. */
    private double balanceAmount; // = 0
    
    /** The date and time in which the balance was created. */
    private ZonedDateTime balanceTimeStamp;
    
    /** For a trading transaction, the associated asset. */
    private Transaction associatedTransaction;
    
    /**
     * Constructor to creates a time-specific balance with an associated transaction.
     * @param transaction The transaction associated with the new balance.
     */
    public Balance(Transaction transaction)
    {
        this.balanceTimeStamp = ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS);
        this.associatedTransaction = transaction;
        this.balanceAmount = transaction.getTransactionAmount();
    }
    
    /**
     * Constructor that creates a time-specific balance of an entity with only an amount.
     * @param amountToChange Amount input into the balance. Can be positive or negative.
     */
    public Balance(double amountToChange)
    {
        this.balanceTimeStamp = ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS);
        this.balanceAmount = amountToChange;
    }
    
    public Balance()
    {
        this(0);
    }
    // TODO Currently uses first constructor only
    public void updateBalance(Balance newBalance)
    {
        double amountToChange = newBalance.getBalanceAmount();
        double current;
        if(getBalanceHistory().isEmpty())
            current = 0;
        else
            current = getBalanceHistory().peek().getBalanceAmount();
        newBalance.setBalanceAmount(amountToChange + current);
        getBalanceHistory().add(newBalance);
    }
    // TODO Should be used before updateBalance
    public static Balance transferTo(Transaction transaction)
    {
        double changeToBalBy = transaction.getTransactionAmount();
        Balance newToBal = new Balance(changeToBalBy);
        newToBal.setAssociatedTransaction(transaction);
        return newToBal;
    }
    // TODO Should be used before updateBalance
    public static Balance transferFrom(Transaction transaction)
    {
        double changeFromBalBy = - transaction.getTransactionAmount();
        Balance newFromBal = new Balance(changeFromBalBy);
        newFromBal.setAssociatedTransaction(transaction);
        return newFromBal;
    }
    
    public double getCurrentBalance()
    {
        if(getBalanceHistory().isEmpty())
            return 0;
        else
            return getBalanceHistory().peek().getBalanceAmount();
    }
    
    //******************************** GETTER METHODS ********************************//
    
    public double getBalanceAmount()
    {
        return balanceAmount;
    }
    
    public ZonedDateTime getBalanceTimeStamp()
    {
        return balanceTimeStamp;
    }
    
    public Transaction getAssociatedTransaction()
    {
        return associatedTransaction;
    }
    
    public Stack<Balance> getBalanceHistory()
    {
        return balanceHistory;
    }
    
    //******************************** SETTER METHODS ********************************//
    
    public void setBalanceAmount(double balanceAmount)
    {
        this.balanceAmount = balanceAmount;
    }
    
    public void setBalanceTimeStamp(ZonedDateTime balanceTimeStamp)
    {
        this.balanceTimeStamp = balanceTimeStamp;
    }
    
    public void setAssociatedTransaction(Transaction associatedTransaction)
    {
        this.associatedTransaction = associatedTransaction;
    }
    
    public void setBalanceHistory(Stack<Balance> balanceHistory)
    {
        this.balanceHistory = balanceHistory;
    }
}
