import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

/** A transaction processed within Luca. */
public class Transaction
{
    /** A list of transactions that have been requested, but have not yet been resolved. */
    private static LinkedList<Transaction> transactionRequests = new LinkedList<>();
   
    /** Transactions that have been requested and resolved. */
    private static Stack<Transaction> transactionHistory = new Stack<>();
    
    /** The user requesting a transaction. Can be either a REGULAR or ADMIN user. */
    private User requestUser;
    
    /** The user acting upon a transaction. Can only be an ADMIN user. */
    private User resolveUser;
    
    /** The amount of funds associated with the transaction. */
    private double transactionAmount;
    
    /** Set when the transaction request is executed by the requestUser. */
    private ZonedDateTime requestDate;
    
    /** Set when the transaction request is acted upon (resolved) by the resolvedUser (ADMIN). */
    private ZonedDateTime resolveDate;
    /**
     * Status is first initialized by the requestUser, but only changed by the resolveUser.
     * GENERAL STATUSES:
     * --> OPEN: transaction has been placed, but not acted upon (default).
     * --> CANCELLED: transaction was requested, but cancelled by the user.
     * --> DENIED: order was placed, but was denied by the resolveUser.
     * BANKING STATUSES:
     * --> DEPOSITED: funds have been added to the Bank.
     * --> WITHDRAWN: funds have been removed from the Bank and transferred to the user(s).
     * TRADING STATUSES:
     * --> BOUGHT: buy-order transaction was requested and assets have been bought and added to the Portfolio.
     * --> SOLD: sell-order transaction was requested and assets have been sold and removed from the Portfolio.
     * --> SHORTED: short-order transaction was requested and assets have been shorted and added to the Portfolio.
     * --> COVERED: cover-order transaction was requested and assets have been removed from the Portfolio.
     */
    private Status transactionStatus;
    
    /**
     * BANKING: deposit or withdraw
     * TRADING: buy/sell or short/cover
     */
    private Type transactionType;
    
    /** If the transaction Type is TRADING, then there exists an associated asset with the transaction. */
    private Asset transactionAsset;
    
    /**
     * Number ID associated with each transaction.
     * Format: <YEAR><MONTH><DAY><HOUR><MINUTE><SECOND>
     * Example: January 1, 2019 @ 12:34:56 --> 201911123456
     */
    private String transactionID;
    
    /**
     * Constructor for BANKING transactions.
     * @param requestedBy The user requesting the transaction.
     * @param amount The amount requested. ALWAYS POSITIVE
     * @param type DEPOSIT, WITHDRAW, BUY, SELL, SHORT, or COVER.
     */
    public Transaction(User requestedBy, double amount, String type)
    {
        this.requestUser = requestedBy;
        this.transactionAmount = amount;
        this.requestDate = ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS);
        this.transactionStatus = Status.OPEN;
        this.transactionType = Type.valueOf(type.toUpperCase());
        this.transactionID = requestDate.getYear() + "" + requestDate.getMonthValue() + "" + requestDate.getDayOfMonth() +
                "" + requestDate.getHour() + "" + requestDate.getMinute() + "" + requestDate.getSecond();
        transactionRequests.add(this);
    }
    
    /**
     * Constructor for TRADING transactions that have an associated asset.
     * @param requestedBy The user requesting the transaction.
     * @param type BUY, SELL, SHORT, or COVER.
     * @param transactionAsset The asset of interest.
     */
    public Transaction(User requestedBy, String type, Asset transactionAsset)
    {
        this(requestedBy, (transactionAsset.getOriginalPrice()*transactionAsset.getVolume()), type);
        this.transactionAsset = transactionAsset;
    }
    
    //TODO Copied from User class
    public String formatTime(ZonedDateTime time)
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
    
    /** Default is OPEN and is updated by the resolveUser once the transaction is resolved. */
    public enum Status
    {
        BOUGHT, CANCELLED, COVERED, DENIED, DEPOSITED, OPEN, SHORTED, SOLD, WITHDRAWN
    }
    
    /**
     * Set the user requesting the transaction.
     * Used for the purpose of eliminating the possibility of withdrawing a DEPOSIT transaction, etc.
     */
    public enum Type
    {
        BUY, COVER, DEPOSIT, SELL, SHORT, WITHDRAW
    }
    
    /**
     * Adds a new transaction request to the list of requested transactions. */
    public void addTransactionRequest()
    {
        transactionRequests.add(this);
    }
    
    /**
     * // TODO
     * Deletes a requested transaction from the list of requested transactions.
     * Should only be used when an transaction has been requested, but is later CANCELLED.
     */
    public void removeTransactionRequest()
    {
        transactionRequests.remove(this);
    }
    
    /** Adds a resolved transaction to the history of previously resolved transaction. */
    public void addToTransactionHistory()
    {
        transactionHistory.add(this);
    }
    
    //******************************** GETTER METHODS ********************************//
    public User getRequestUser()
    {
        return requestUser;
    }
    
    public User getResolveUser()
    {
        return resolveUser;
    }
    
    public double getTransactionAmount()
    {
        return transactionAmount;
    }
    
    public ZonedDateTime getRequestDate()
    {
        return requestDate;
    }
    
    public ZonedDateTime getResolveDate()
    {
        return resolveDate;
    }
    
    public Status getTransactionStatus()
    {
        return transactionStatus;
    }
    
    public Type getTransactionType()
    {
        return transactionType;
    }
    
    public String getTransactionID()
    {
        return transactionID;
    }
    
    public Asset getTransactionAsset()
    {
        return transactionAsset;
    }
    
    public static LinkedList<Transaction> getTransactionRequests()
    {
        return transactionRequests;
    }
    
    public static Stack<Transaction> getTransactionHistory()
    {
        return transactionHistory;
    }
    
    //******************************** SETTER METHODS ********************************//
    public void setReqUser(User reqUser)
    {
        this.resolveUser = reqUser;
    }
    
    public void setResolveUser(User adminUser)
    {
        this.resolveUser = adminUser;
    }
    
    public void setTransactionAmount(double amount)
    {
        this.transactionAmount = amount;
    }
    
    public void setRequestDate(ZonedDateTime requestDate)
    {
        this.requestDate = requestDate;
    }
    
    public void setResolveDate(ZonedDateTime resolveDate)
    {
        this.resolveDate = resolveDate;
    }
    
    public void setTransactionStatus(Status status)
    {
        this.transactionStatus = status;
    }
    
    public void setTransactionType(Type type)
    {
        this.transactionType = type;
    }
    
    public void setTransactionID(String ID)
    {
        this.transactionID = ID;
    }
    
}

