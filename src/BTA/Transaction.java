package BTA;

import ABP.*;
import LucaMember.User;

import java.security.*;
import java.time.ZonedDateTime;
import java.util.Scanner;

/**
 * Essentially, a movement of funds of funds between any two account entities, whether that is between the user and the
 * bank, or the bank and the portfolio. Every {@link LucaMember} must first request a transaction, before the funds are
 * reallocated. An admin {@link LucaMember} is able to resolve the transaction such that the funds or asset associated with
 * the transaction is either approved or denied. Also, an admin {@link LucaMember} who requests a transaction is able to
 * resolve the transaction immediately. In either case, to request or to resolve, the {@link LucaMember} is asked to confirm
 * the decision to help minimize accidental {@link LucaMember} action, particularly when resolving a transaction.
 * <p>When a transaction is first requested, the request {@link LucaMember}, the amount, the date and time requested,
 * the type of transaction, the status of the transaction, and the transaction ID are set. By default, the status
 * of a requested transaction is set to <code>OPEN</code>. If the {@link LucaMember} requesting the transaction is a
 * non-admin, the request is added to the list of requests ({@link Account}) to be resolved.</p>
 * <p>When a transaction is resolved by an admin {@link LucaMember} the {@link #resolveUser}, {@link #resolveDate}, and the
 * {@link #transactionStatus} are updated. If the status is set to <code>DENIED</code>, the request is removed from the
 * list of transaction requests (if requested by a non-admin {@link LucaMember}) and added to the transaction history
 * ({@link Account}) without any funds being reallocated. If transaction is neither <code>DENIED</code> nor
 * <code>CANCELLED</code> (set when either the request or resolution is being made, the funds or {@link Asset}
 * associated with transaction are processed, and the appropriate {@link Balance}(s) are updated.</p>
 * @author hLabs
 * @since 0.1a
 */
public class Transaction
{
    /**
     * {@link LucaMember} requesting a transaction that can be be either an admin or non-admin
     */
    private User requestUser;
    
    /**
     * Admin {@link LucaMember} acting upon a requested transaction.
     */
    private User resolveUser;
    
    /**
     * Dollar amount associated with the transaction. If the transaction is type <code>BUY</code>, <code>SELL</code>,
     * <code>SHORT</code>, or <code>COVER</code>, the amount is however much is required to acquire the {@link Asset}.
     */
    private double transactionAmount;
    
    /**
     * Set when the transaction request is executed by the {@link #requestUser}.
     */
    private ZonedDateTime requestDate;
    
    /**
     * Set when the transaction request is acted upon (resolved) by the admin {@link #resolveUser}.
     */
    private ZonedDateTime resolveDate;
    /**
     * Status is default set to <code>OPEN</code> when the transaction is requested. When the admin {@link #resolveUser}
     * resolves the transaction to go through, the status is updated corresponding to the type of transaction
     * ({@link #getMatchingStatus()}. When either requesting or resolving the transaction, the {@link LucaMember} can decide
     * to abandon the transaction, in which case the status is set to <code>CANCELLED</code>. If the admin {@link LucaMember}
     * decides to decline the transaction, the status is set to <code>DECLINED</code>.
     * <ul>
     *     <li><code>OPEN</code>: transaction was requested, but not acted upon (default).</li>
     *     <li><code>CANCELLED</code>: {@link LucaMember} decides to not process the transaction. </li>
     *     <li><code>DENIED</code>: transaction was requested, but was not approved by the admin {@link LucaMember}.</li>
     *     <li><code>DEPOSITED</code>: funds have been added to the {@link Bank}.</li>
     *     <li><code>WITHDRAWN</code>: funds have been removed from the {@link Bank} and transferred to the
     *     {@link LucaMember}(s).</li>
     *     <li><code>BOUGHT</code>: buy-order transaction was requested, the {@link Asset} has been acquired, and added
     *     to the {@link Portfolio}.</li>
     *     <li><code>SOLD</code>: sell-order transaction was requested, the {@link Asset} has been liquidated, and
     *     removed from the {@link Portfolio}.</li>
     *     <li><code>SHORTED</code>: short-order transaction was requested, the {@link Asset} has been shorted, and
     *     added to the {@link Portfolio}.</li>
     *     <li><code>COVERED</code>: cover-order transaction was requested, the {@link Asset} has been removed from the
     *     {@link Portfolio}.</li>
     * </ul>
     */
    
    private Status transactionStatus;
    
    /**
     * Either banking- or trading-related. If associated with the {@link Bank}, the type can be either
     * <code>DEPOSIT</code> or <code>WITHDRAW</code>. If associated with the {@link Portfolio}, the type can
     * <code>BUY</code>, <code>SELL</code>, <code>SHORT</code>, and <code>COVER</code>. The type is set when the
     * transaction is first requested.
     */
    private Type transactionType;
    
    /**
     * If the transaction is related to the {@link Portfolio}, then there exists an associated {@link Asset} with the
     * transaction.
     */
    private Asset transactionAsset;
    
    //TODO Connor: please add appropriate field description
    private byte[] signature;
    
    //TODO Connor: please add appropriate field description
    private long timestamp;
    
    //TODO Connor: please add appropriate field description
    private String transactionData;
    
    //TODO Connor: please add appropriate field description
    private PublicKey userPublicKey;
    
    //TODO Connor: please add appropriate field description
    private PrivateKey userPrivateKey;
    
    /**
     * Creates a transaction with no associated {@link Asset}.
     * @param type Determines if the transaction is related to the {@link Bank} and/or {@link Portfolio}.
     * @param amount Dollar amount requested. Always positive.
     */
    public Transaction(Type type, double amount) throws IllegalArgumentException
    {
        if(amount < 0)
        {
            throw new IllegalArgumentException("Amount must be nonnegative.");
        }
        else
        {
            this.transactionAmount = amount;
            this.transactionStatus = Status.OPEN;
            this.transactionType = type;
        }
    }
    
    /**
     * //TODO Prevent user from making the type DEPOSIT or WITHDRAW.
     * Creates a transaction with an associated {@link Asset}.
     * @param type Since there is an an associated {@link Asset}, will be <code>BUY</code>, <code>SELL</code>,
     *             <code>SHORT</code>, or <code>COVER</code>.
     * @param asset {@link Asset} associated with the transaction.
     */
    public Transaction(Type type, Asset asset)
    {
        if(type == Type.DEPOSIT || type == Type.WITHDRAW)
            throw new IllegalArgumentException("Transaction type must be either BUY/SELL/SHORT/COVER.");
        else
        {
            this.transactionAmount = transactionAsset.getStartPrice()*transactionAsset.getVolume();
            this.transactionAsset = asset;
            this.transactionStatus = Status.OPEN;
            this.transactionType = type;
        }
    }
    
    /**
     * Requires that the {@link LucaMember} confirms the action to either request or resolve a transaction.
     * @return <code>true</code> if response is "yes," and <code>false</code> if "no."
     */
    public boolean confirmAction()
    {
        Scanner reader = new Scanner(System.in);
        String response = reader.next();
        boolean confirmed = false;
        while(!confirmed)
        {
            System.out.println("Type \"yes\" or \"no\" to confirm this action: ");
            if(!response.trim().equalsIgnoreCase("yes") && !response.trim().equalsIgnoreCase("no"))
                System.out.println("Please try again. Make sure you type either \"yes\" or \"no\".");
            else
                confirmed = true;
        }
        reader.reset();
        return response.equalsIgnoreCase("yes");
    }
    
    /**
     * Default is <code>OPEN</code>. Updated when the admin {@link LucaMember} resolves the transaction.
     */
    public enum Status
    {
        BOUGHT, CANCELLED, COVERED, DENIED, DEPOSITED, OPEN, SHORTED, SOLD, WITHDRAWN
    }
    
    /**
     * Set by the user requesting the transaction.
     * <code>DEPOSIT</code> and <code>WITHDRAW</code> are reserved for {@link Bank}-only transactions, while
     * <code>BUY</code>, <code>SELL</code>, <code>SHORT</code>, <code>WITHDRAW</code> are reserved for
     * {@link Portfolio}-only transactions.
     */
    public enum Type
    {
        BUY, COVER, DEPOSIT, SELL, SHORT, WITHDRAW
    }
    
    /**
     * Used when an admin {@link LucaMember} is requesting a transaction. Since an admin {@link LucaMember} can request and resolve
     * transactions immediately, to is more efficient and less prone to {@link LucaMember} error if the status of the
     * transaction is updated to match the type of transaction as opposed to entering the updated status when resolving
     * it.
     * @return Status corresponding to the type of the transaction. Useful for automatically updating the status of
     * a transaction without having it as a method parameter.
     */
    public Status getMatchingStatus()
    {
        Transaction.Type transType = this.getTransactionType();
        switch(transType)
        {
            case DEPOSIT:   return Status.DEPOSITED;
            case WITHDRAW:  return Status.WITHDRAWN;
            case BUY:       return Status.BOUGHT;
            case SELL:      return Status.SOLD;
            case SHORT:     return Status.SHORTED;
            case COVER:     return Status.COVERED;
            default:        return Status.CANCELLED;
        }
    }
    
    /**
     * Adds a new transaction request to the list of requested transactions.
     */
    public void addTransactionRequest()
    {
       Account.getTransactionRequests().add(this);
    }
    
    /**
     * Deletes a requested transaction from the list of requested transactions.
     * Should only be used when an transaction has been requested and is either resolved or cancelled.
     */
    public void removeTransactionRequest()
    {
       Account.getTransactionRequests().remove(this);
    }
    
    /**
     * Adds a resolved transaction to the history of previously resolved transaction.
     */
    public void addToTransactionHistory()
    {
        Account.getTransactionHistory().add(this);
    }
    
    /**
     * @return {@link LucaMember} that requested the transaction.
     */
    public User getRequestUser()
    {
        return requestUser;
    }
    
    /**
     * Returns the the user who resolved the transaction. Admin members only.
     * @return {@link LucaMember} that resolves the requested transaction. Can only be admin users.
     */
    public User getResolveUser()
    {
        return resolveUser;
    }
    
    /**
     * Returns dollar amount associated with the transaction. If the transaction is trading-related, the transaction
     * amount is the amount required by to acquire the {@link Asset}.
     * @return Dollar amount associated with the transaction.
     */
    public double getTransactionAmount()
    {
        return transactionAmount;
    }
    
    /**
     * Date and time in which the transaction is requested. Timezone set to America/New York.
     * @return Date and time info.
     * @see ZonedDateTime
     */
    public ZonedDateTime getRequestDate()
    {
        return requestDate;
    }
    
    /**
     * Date and time in which the transaction is resolved. Timezone set to America/New York.
     * @return Date and time info.
     * @see ZonedDateTime
     */
    public ZonedDateTime getResolveDate()
    {
        return resolveDate;
    }
    
    /**
     * Current status of the transaction.
     * @return Status of transaction.
     * @see Status
     */
    public Status getTransactionStatus()
    {
        return transactionStatus;
    }
    
    /**
     * Describes what entities to which the transaction is related.
     * @return Describes what entities to which the transaction is related.
     * @see Type
     */
    public Type getTransactionType()
    {
        return transactionType;
    }
    
    /**
     * If the transaction is trading-related, the associated {@link Asset}.
     * @return Associated asset.
     */
    public Asset getTransactionAsset()
    {
        return transactionAsset;
    }
    
    //TODO Connor: please add appropriate description
    public PublicKey getUserPublicKey()
    {
        return userPublicKey;
    }
    
    //TODO Connor: please add appropriate description
    public PrivateKey getUserPrivateKey()
    {
        return userPrivateKey;
    }
    
    //TODO Connor: please add appropriate description
    public byte[] getSignature()
    {
        return signature;
    }
    
    //TODO Connor: please add appropriate description
    public long getTimestamp()
    {
        return timestamp;
    }
    
    //TODO Connor: please add appropriate description
    public String getTransactionData()
    {
        return transactionData;
    }
    
    /**
     * Sets the {@link LucaMember} who requests the transaction.
     * @param reqUser {@link LucaMember} who requests the transaction.
     */
    public void setRequestUser(User reqUser)
    {
        this.requestUser = reqUser;
    }
    
    /**
     * Sets the {@link LucaMember} who resolves the transaction. Can only be an admin {@link LucaMember}.
     * @param adminUser {@link LucaMember} who resolves the transaction.
     */
    public void setResolveUser(User adminUser)
    {
        this.resolveUser = adminUser;
    }
    
    /**
     * Sets the amount associated with the transaction.
     * @param amount Dollar amount associated with the transaction.
     */
    public void setTransactionAmount(double amount)
    {
        this.transactionAmount = amount;
    }
    
    /**
     * Sets the date and time in which the transaction is requested. Timezone set to America/New York.
     * @param requestDate Date and time of request.
     */
    public void setRequestDate(ZonedDateTime requestDate)
    {
        this.requestDate = requestDate;
    }
    
    /**
     * Sets the date and time in which the requested transaction is resolved. Timezone set to America/New York.
     * @param resolveDate Date and time resolved.
     */
    public void setResolveDate(ZonedDateTime resolveDate)
    {
        this.resolveDate = resolveDate;
    }
    
    /**
     * Sets the status of the transaction.
     * @param status If not <code>CANCELLED</code>, set to <code>OPEN</code> after request, and updated to either
     *               <code>DENIED</code> or the corresponding status, based on the type of transaction.
     */
    public void setTransactionStatus(Status status)
    {
        this.transactionStatus = status;
    }
    
    //TODO Connor: please add appropriate description
    public void setUserPublicKey(PublicKey userPublicKey)
    {
        this.userPublicKey = userPublicKey;
    }
    
    //TODO Connor: please add appropriate description
    public void setUserPrivateKey(PrivateKey userPrivateKey)
    {
        this.userPrivateKey = userPrivateKey;
    }
    
    //TODO Connor: please add appropriate description
    public void setSignature(byte[] signature)
    {
        this.signature = signature;
    }
    
    //TODO Connor: please add appropriate description
    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }
    
    //TODO Connor: please add appropriate description
    public void setTransactionData(String transactionData)
    {
        this.transactionData = transactionData;
    }

}

