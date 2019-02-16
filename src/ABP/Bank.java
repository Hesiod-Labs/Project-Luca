package ABP;

import BTA.*;
import LucaMember.User;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * The repository for which inactive trading funds are kept. The bank can be assigned a name. Otherwise, it is default
 * called "Unnamed Bank." The bank has a {@link Balance} that contains both the current value of the bank, as well as
 * past values. Each time the bank's balance is updated, the time in which it is updated, the amount, and the
 * associated transaction are all recorded as part of the new balance. Users can {@link #deposit(Transaction)} funds
 * from their own balance or from some external source. Depositing funds adds value to the bank, but does not affect
 * the {@link Portfolio} balance. Users can also {@link #withdraw(Transaction)} funds, which removes funds from the
 * bank is transferred to whomever requested the withdrawal.
 * @author hLabs
 * @since 0.1a
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
     * //TODO The transaction can be negative, so should print some kind of warning beforehand.
     */
    public Bank(String name, Transaction firstTransaction)
    {
        //TODO This should work, in terms of updating the balance history, since it's being set to the bank balance.
        this(name, new Balance(firstTransaction));
    }
    
    /**
     * Creates a bank with a non-default name and some initial value, but no associated transaction.
     * While it is important to have all historical balances be tied to the actual transaction, this constructor is
     * useful when used in tandem with the {@link Balance} constructor that only takes a <code>double</code> initial amount,
     * since multiple balance objects can be created to update both the bank and portfolio while maintaining the
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
    }
    
    /**
     * Deposits money into the bank. Called by {@link User#resolveTransaction(Transaction, String)} after a transaction
     * of type "DEPOSIT" ({@link Transaction.Type}) has been requested. Once the deposit transaction is resolved, the
     * timestamp of when it is resolved is set, the transaction status is updated to "DEPOSITED"
     * ({@link Transaction.Status}), and the bank's balance is updated to account for the newly added funds.
     * A transaction only of type "DEPOSIT" can be resolved and added to the bank.
     * @see Balance
     * @param transaction Associated with the deposit.
     */
    public static void deposit(Transaction transaction)
    {
        transaction.setResolveDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
        transaction.setTransactionStatus(Transaction.Status.DEPOSITED);
        getBankBalance().updateBalance(Balance.transferTo(transaction));
        Account.getAccountBalance().updateBalance(Balance.transferTo(transaction)); //TODO Add to description
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
        transaction.setResolveDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
        transaction.setTransactionStatus(Transaction.Status.WITHDRAWN);
        User requestedBy = transaction.getRequestUser();
        requestedBy.getUserBalance().updateBalance(Balance.transferTo(transaction));
        getBankBalance().updateBalance(Balance.transferFrom(transaction));
        Account.getAccountBalance().updateBalance(Balance.transferFrom(transaction)); //TODO Add to description
    }
    
    /**
     * @return Entire balance repository whose most recent entry is the current value of the bank. Each entry before
     * the most recent entry are previous balances that all have timestamps and associated transactions.
     * @see Balance
     */
    public static Balance getBankBalance()
    {
        return bankBalance;
    }
    
    /**
     * @return Name designated to the bank.
     */
    public static String getBankName()
    {
        return bankName;
    }
    
    /**
     * Sets the bank's balance repository.
     * <p>WARNING: DO NOT USE UNLESS THE BANK'S BALANCE HAS NOT ALREADY BEEN SET. DOING SO WILL OVERWRITE THE
     * CURRENT BANK BALANCE AND ALL PAST BALANCES WILL BE LOST.</p>
     * @param bankBalance Includes both current and past bank values with timestamps and transactions.
     */
    public static void setBankBalance(Balance bankBalance)
    {
        Bank.bankBalance = bankBalance;
    }
    
    /**
     * Set's the bank's name.
     * <p>"WARNING: IT IS NOT RECOMMENDED TO SET THE BANK'S NAME AFTER IT HAS ALREADY BEEN SET.</p>
     * @param bankName Name designated to the bank.
     */
    public static void setBankName(String bankName)
    {
        Bank.bankName = bankName;
    }
}
