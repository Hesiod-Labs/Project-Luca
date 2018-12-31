import javax.xml.crypto.dsig.TransformService;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class Transaction implements Banking, Trading
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
    private String status;
    /**
     * BANKING: deposit or withdraw
     * TRADING: buy/sell or short/cover
     */
    private String type;
    private int orderID;
    
    public Transaction(User requestedBy, double amount, String type)
    {
        this.reqUser = requestedBy;
        this.amount = amount;
        this.reqDate = ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS);
        this.status = "Open"; // TODO How does this interact with the enum Status?
        this.type = type;
        this.orderID = Integer.parseInt(reqDate.getYear() + "" + reqDate.getMonthValue() + "" + reqDate.getDayOfMonth() +
                "" + reqDate.getHour() + "" + reqDate.getMinute() + "" + reqDate.getSecond());
    }
    
    // TODO Remember that each transaction type will be used in the requestTransaction and makeTransaction methods. So write these as the ACTIONS (assuming privileges have already been checked)
    // Deposits money into the bank and updates the bankBalance
    // TODO Figure out how to connect Bank and Portfolio to allow transactions between them
    public void deposit(Transaction transaction) // TODO Make into try/catch to consider negative amounts
    {
    
    }
    
    public void withdraw(double amount)
    {
    
    }
    
    public void buyOrder(double amount)
    {
    
    }
    
    public void sellOrder(double amount)
    {
    
    }
    
    public void shortOrder(double amount)
    {
    
    }
    
    public void coverOrder(double amount)
    {
    
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
    
    public String getStatus()
    {
        return status;
    }
    
    public String getType()
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
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public void setOrderID(int orderID)
    {
        this.orderID = orderID;
    }
    
}

