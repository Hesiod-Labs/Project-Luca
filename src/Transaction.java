import java.time.*;
import java.time.temporal.ChronoUnit;

public class Transaction
{
    private User reqUser;
    private User adminUser;
    private double amount;
    private ZonedDateTime reqDate; // Set when the transaction is requested by a regular user
    private ZonedDateTime exeDate; // Set when the transaction is acted upon by an admin user
    /**
     * Status is first initialized by the regular or admin user, but only changed by the admin user
     * OPEN: transaction has been placed, but not acted upon (default)
     * DEPOSITED: amount has been added to the Bank (banking only)
     * WITHDRAWN: amount has been removed from the Bank (banking only)
     * EXECUTED: transaction has been placed and order has been completed (trading only)
     * // TODO Consider splitting EXECUTED into BUY, SELL, etc.
     * CANCELLED: the order was placed, but cancelled by the user
     * DENIED: the order has been placed, but was denied execution
     */
    private Status status;
    /**
     * BANKING: deposit or withdraw
     * TRADING: buy/sell or short/cover
     */
    private Type type;
    private int orderID;
    
    public Transaction(User requestedBy, double amount, String type)
    {
        this.reqUser = requestedBy;
        this.amount = amount;
        this.reqDate = ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS);
        this.status = Status.OPEN;
        this.type = Type.valueOf(type.toUpperCase());
        this.orderID = Integer.parseInt(reqDate.getYear() + "" + reqDate.getMonthValue() + "" + reqDate.getDayOfMonth() +
                "" + reqDate.getHour() + "" + reqDate.getMinute() + "" + reqDate.getSecond());
    }
    
    
    public enum Status
    {
        OPEN, DEPOSITED, WITHDRAWN, EXECUTED, CANCELLED, DENIED;
    }
    
    public enum Type
    {
        DEPOSIT, WITHDRAW, BUY, SELL, SHORT, COVER;
    }
    
    // Getter methods
    public User getReqUser()
    {
        return reqUser;
    }
    
    public User getAdminUser()
    {
        return adminUser;
    }
    
    public double getAmount()
    {
        return amount;
    }
    
    public ZonedDateTime getReqDate()
    {
        return reqDate;
    }
    
    public ZonedDateTime getExeDate()
    {
        return exeDate;
    }
    
    public Status getStatus()
    {
        return status;
    }
    
    public Type getType()
    {
        return type;
    }
    
    public int getOrderID()
    {
        return orderID;
    }
    
    // Setter methods
    public void setReqUser(User reqUser)
    {
        this.reqUser = reqUser;
    }
    
    public void setAdminUser(User adminUser)
    {
        this.adminUser = adminUser;
    }
    
    public void setAmount(double amount)
    {
        this.amount = amount;
    }
    
    public void setReqDate(ZonedDateTime reqDate)
    {
        this.reqDate = reqDate;
    }
    
    public void setExeDate(ZonedDateTime exeDate)
    {
        this.exeDate = exeDate;
    }
    
    public void setStatus(Status status)
    {
        this.status = status;
    }
    
    public void setType(Type type)
    {
        this.type = type;
    }
    
    public void setOrderID(int orderID)
    {
        this.orderID = orderID;
    }
    
}

