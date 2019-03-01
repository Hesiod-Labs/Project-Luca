package ABP;

import BTA.*;
import LucaMember.User;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * The repository in which inactive trading funds are kept. The bank can be assigned a name. Otherwise, it is assigned
 * the default name "Unnamed Bank." The bank has a {@link Balance} that contains both the current value of the bank, as
 * well as past values. Each time the bank's balance is updated, the time in which it is updated, the amount, and the
 * associated transaction are all recorded as part of the new balance. Depositing funds ({@link #deposit(Transaction)})
 * adds value to the bank, but does not affect the {@link Portfolio} balance. Likewise, withdrawing funds
 * ({@link #withdraw(Transaction)}, removes funds from the bank and transfers said funds to whomever
 * requested the withdrawal.
 * @author hLabs
 * @since 1.0
 */
public class Bank
{
    /**
     * Name designated to the bank (optional). If the bank is left unnamed, the default name is "Unnamed Bank."
     */
    private static String bankName;
    
    /** Current and past values of the bank. Each balance is updated based on previous values.
     * @see Balance
     */
    private static Balance bankBalance = new Balance();
    
    /**
     * Creates a bank with a non-default name and an existing {@link Balance}.
     * @param name Name designated to the bank.
     * @param balance Contains current and past values, transactions, and timestamps.
     */
    public Bank(String name, Balance balance)
    {
        bankName = name;
        bankBalance = balance;
    }
    
    /**
     * Creates a bank with a non-default name and a {@link Transaction}. The {@link #bankBalance} is updated based on
     * the amount associated with the transaction.
     * @param name Name designated to the bank.
     * @param firstTransaction A transaction that sets the bank.
     */
    public Bank(String name, Transaction firstTransaction)
    {
        this(name, new Balance(firstTransaction));
    }
    
    /**
     * Creates a bank with a non-default name and some initial value, but no associated transaction.
     * While it is important to have all historical balances be tied to the actual transaction, this constructor is
     * useful when used in tandem with the {@link Balance} constructor that only takes a <code>double</code> initial
     * amount, since multiple balance objects can be created to update both the bank and portfolio while maintaining the
     * transaction with which the balance is associated.
     * @param name Name designated to the bank.
     * @param initialAmount Some dollar amount to be put in the bank, but is not recorded as a deposit.
     */
    public Bank(String name, double initialAmount)
    {
        this(name, new Balance(initialAmount));
    }
    
    /**
     * Creates a bank with only a non-default name, but no initial amount. Sets the bank balance to 0 with a timestamp
     * of when the bank is first established.
     * @param name Name designated to the bank.
     */
    public Bank(String name)
    {
        this(name, new Balance());
    }
    
    /**
     * Creates a bank with the default name, "Unnamed Bank," and has a balance of 0.
     */
    public Bank()
    {
        this("Unnamed Bank", new Balance());
        Balance createdAt = new Balance();
        createdAt.updateBalance(createdAt);
    }
    
    /**
     * Deposits money into the bank. Called by {@link User#resolveTransaction(Transaction, String)} after a transaction
     * of type <code>DEPOSIT</code> ({@link Transaction.Type}) has been requested. Once the deposit transaction is
     * resolved by an admin {@link User}, the timestamp of when it is resolved is set, the transaction status is
     * updated to <code>DEPOSITED</code> ({@link Transaction.Status}), and the bank's balance is updated to account for
     * the newly added funds. A transaction only of type <code></code> can be resolved and added to the bank.
     * @see Balance
     * @param transaction Associated with the deposit.
     */
    public static void deposit(Transaction transaction)
    {
        if(transaction.getTransactionType().equals(Transaction.Type.DEPOSIT))
        {
            transaction.setResolveDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
            transaction.setTransactionStatus(Transaction.Status.DEPOSITED);
            getBankBalance().updateBalance(Balance.transferTo(transaction));
            Account.getAccountBalance().updateBalance(Balance.transferTo(transaction)); //TODO Add to description
        }
        else
        {
            throw new IllegalArgumentException("Only transactions of type DEPOSIT are allowed to be deposited.");
        }
    }
    
    /**
     * Withdraws money from the bank and is transferred to the request user's balance, if not denied. Like
     * {@link #deposit(Transaction)}, withdrawing funds sets the timestamp of resolution at the moment the transaction
     * request is resolved, sets the transaction status to "WITHDRAWN" ({@link Transaction.Status}), updates the balance
     * of the request user by adding the amount associated with the transaction, and correspondingly updates the
     * {@link Bank} balance by subtracting the same amount. A transaction only of type "DEPOSIT" can be resolved and
     * added to the bank.
     * @param transaction Associated with the withdraw.
     */
    public static void withdraw(Transaction transaction)
    {
        if(transaction.getTransactionType().equals(Transaction.Type.WITHDRAW))
        {
            transaction.setResolveDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
            transaction.setTransactionStatus(Transaction.Status.WITHDRAWN);
            User requestedBy = transaction.getRequestUser();
            requestedBy.getUserBalance().updateBalance(Balance.transferTo(transaction));
            getBankBalance().updateBalance(Balance.transferFrom(transaction));
            Account.getAccountBalance().updateBalance(Balance.transferFrom(transaction));
        }
        else
        {
            throw new IllegalArgumentException("Only transactions of type WITHDRAW are allowed to be withdrawn.");
        }
    }
    
    /**
     * Entire balance repository whose most recent entry is the current value of the bank. Each entry before
     * the most recent entry are previous balances that all have timestamps and associated transactions.
     * @return All balance information of the bank.
     * @see Balance
     */
    public static Balance getBankBalance()
    {
        return bankBalance;
    }
    
    /**
     * The name designated to the bank. Set at the time of the {@link Account}'s creation.
     * @return Name designated to the bank.
     */
    public static String getBankName()
    {
        return bankName;
    }
}
