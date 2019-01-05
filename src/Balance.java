import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Stack;

/** Represents the balance associated with a user, portfolio, or bank. */
public class Balance
{
    /** Contains all previous balances of an entity. */
    private static Stack<Balance> balanceHistory = new Stack<>();
    
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
        
        double amountChanged = transaction.getTransactionAmount();
        double current;
        
        if(balanceHistory.isEmpty())
            current = 0;
        else
            current = balanceHistory.peek().getBalanceAmount();
        
        this.balanceAmount = amountChanged + current;
        
        // balanceHistory.add(this);
    }
    
    /**
     * Constructor that creates a time-specific balance of an entity with only an amount.
     * @param amountChanged Amount input into the balance. Can be positive or negative.
     */
    public Balance(double amountChanged)
    {
        this.balanceTimeStamp = ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS);
        
        double current;
        
        if(balanceHistory.isEmpty())
            current = 0;
        else
            current = balanceHistory.peek().getBalanceAmount();
    
        this.balanceAmount = amountChanged + current;
        
        // balanceHistory.add(this);
    }
    
    public Balance()
    {
        this(0);
    }
    
    public void updateBalance(Balance newBalance)
    {
        getBalanceHistory().add(newBalance);
    }
    
    public static Balance transferTo(Transaction transaction)
    {
        double changeToBalBy = transaction.getTransactionAmount();
        Balance newToBal = new Balance(changeToBalBy);
        newToBal.setAssociatedTransaction(transaction);
        return newToBal;
    }
    
    public static Balance transferFrom(Transaction transaction)
    {
        double changeFromBalBy = - transaction.getTransactionAmount();
        Balance newFromBal = new Balance(changeFromBalBy);
        newFromBal.setAssociatedTransaction(transaction);
        return newFromBal;
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
        Balance.balanceHistory = balanceHistory;
    }
}
