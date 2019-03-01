package BTA;

import ABP.*;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Stack;

/** Contains information about current and past values of an entity, the {@link Transaction} associated with each value,
 * and date and time in which the value was current. The {@link LucaMember}, {@link Portfolio}, and {@link Bank} all have
 * separate balances that are updated in accordance with the transaction(s) that are requested and subsequently
 * resolved. All balance statements are stored in a {@link Stack} such that the current balance statement is on top.
 */
public class Balance
{
    /**
     * Contains all previous balances of an entity. The current balance statement is the top entry.
     */
    private Stack<Balance> balanceHistory = new Stack<>();
    
    /**
     * Amount associated with a specific balance statement in time.
     */
    private double balanceAmount;
    
    /**
     * Date and time in which an entity's balance is updated to include a new balance.
     */
    private ZonedDateTime balanceTimeStamp;
    
    /**
     * For a trading transaction, the associated asset.
     */
    private Transaction associatedTransaction;
    
    /**
     * Creates a time-specific balance statement with an associated transaction.
     * @param transaction Transaction associated with the new balance statement.
     */
    public Balance(Transaction transaction)
    {
        this.balanceTimeStamp = ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS);
        this.associatedTransaction = transaction;
        this.balanceAmount = transaction.getTransactionAmount();
    }
    
    /**
     * Creates a time-specific balance statement of an entity with only an amount.
     * @param amountToChange Amount input into the balance. Can be positive or negative.
     */
    public Balance(double amountToChange)
    {
        this.balanceTimeStamp = ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS);
        this.balanceAmount = amountToChange;
    }
    
    /**
     * Creates a zero balance statement with a timestamp.
     */
    public Balance()
    {
        this(0);
    }
    
    /**
     * Correctly sets the current balance statement amount when being added to the {@link #balanceHistory}. If there
     * are no previous balance statements, the amount associated with the new balance statement is kept the same.
     * In the case of previous balance statements, the most recent balance statement value is used in tandem with
     * the new balance statement to calculate the correct value for the balance. Also uses
     * {@link #transferTo(Transaction)} and {@link #transferFrom(Transaction)} to handle when a balance value should be
     * decreased or increased, based on the associated transaction.
     * @param newBalance Balance to update the current balance value. Stored in the {@link #balanceHistory}.
     */
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
    
    /**
     * Used if a balance value is to be increased, the amount of which is determined by the transaction parameter.
     * The new balance statement amount is then set to the transaction amount and returned to be added to the
     * {@link #balanceHistory}.
     * @param transaction Transaction associated with updating the balance value.
     * @return Balance object to be added to the {@link #balanceHistory}.
     */
    public static Balance transferTo(Transaction transaction)
    {
        double changeToBalBy = transaction.getTransactionAmount();
        Balance newToBal = new Balance(changeToBalBy);
        newToBal.setAssociatedTransaction(transaction);
        return newToBal;
    }
    
    /**
     * Used if a balance value is to be decreased, the amount of which is determined by the transaction parameter.
     * The new balance statement amount is then set to the transaction amount and returned to be subtracted from the
     * {@link #balanceHistory}.
     * @param transaction Transaction associated with updating the balance value.
     * @return Balance object to be added to the {@link #balanceHistory}.
     */
    public static Balance transferFrom(Transaction transaction)
    {
        double changeFromBalBy = - transaction.getTransactionAmount();
        Balance newFromBal = new Balance(changeFromBalBy);
        newFromBal.setAssociatedTransaction(transaction);
        return newFromBal;
    }
    
    /**
     * Returns the value of the most recent balance statement that represents the overall balance value.
     * @return Current balance value.
     */
    public double getCurrentValue()
    {
        if(getBalanceHistory().isEmpty())
            return 0;
        else
            return getBalanceHistory().peek().getBalanceAmount();
    }
    
    /**
     * Dollar amount associated with a specific balance statement value.
     * @return Dollar amount associated with a specific balance statement value.
     */
    public double getBalanceAmount()
    {
        return balanceAmount;
    }
    
    /**
     * Date and time in which a balance statement was recorded, specifically when resolving transactions.
     * @return Date and time in which a balance statement was recorded, specifically when resolving transactions.
     */
    public ZonedDateTime getBalanceTimeStamp()
    {
        return balanceTimeStamp;
    }
    
    /**
     * For investing transactions, the transaction involved when updating the balance value.
     * @return For investing transactions, the transaction involved when updating the balance value.
     */
    public Transaction getAssociatedTransaction()
    {
        return associatedTransaction;
    }
    
    /**
     * All past balance statements with associated amounts, timestamps, and likely associated transactions.
     * @return All past balance statements with associated amounts, timestamps, and likely associated transactions.
     */
    public Stack<Balance> getBalanceHistory()
    {
        return balanceHistory;
    }
    
    /**
     * Sets the dollar amount associated with a specific balance statement, though is likely influenced by previous
     * balance statements.
     * @param balanceAmount Dollar amount tied to a specific balance statement.
     */
    private void setBalanceAmount(double balanceAmount)
    {
        this.balanceAmount = balanceAmount;
    }
    
    /**
     * Sets the date and time in which a balance statement was recorded.
     * @param balanceTimeStamp Date and time of a balance statement. Timezone set to America/New York.
     * @see ZonedDateTime
     */
    public void setBalanceTimeStamp(ZonedDateTime balanceTimeStamp)
    {
        this.balanceTimeStamp = balanceTimeStamp;
    }
    
    /**
     * If a balance statement is an effect of a trading transaction, then the balance has that transaction tied to it.
     * @param associatedTransaction Transaction used when updating the balance statement.
     */
    private void setAssociatedTransaction(Transaction associatedTransaction)
    {
        this.associatedTransaction = associatedTransaction;
    }
    
    /**
     * Sets the current and previous balance statements.
     * WARNING: DO NOT USE UNLESS THE BALANCE HISTORY HAS NOT BEEN INSTANTIATED. OTHERWISE BALANCE HISTORY DATA WILL
     * BE OVERWRITTEN.
     * @param balanceHistory Contains the current and all previous balance statements.
     */
    public void setBalanceHistory(Stack<Balance> balanceHistory)
    {
        this.balanceHistory = balanceHistory;
    }
}
