import java.time.*;
import java.time.temporal.ChronoUnit;

public class Transaction extends Account
{
    private String userID;
    private double amount;
    private LocalDateTime reqDate;
    private LocalDateTime exeDate;
    /**
     * OPEN: transaction has been placed, but not acted upon (default)
     * DEPOSITED: amount has been added to withdrawable funds (banking only)
     * WITHDRAWN: amount has been removed from withdrawable funds (banking only)
     * EXECUTED: transaction has been placed and order has been completed (trading only)
     * CANCELLED: the order was placed, but cancelled by the user (general)
     * DENIED: the order has been placed, but was denied execution (general)
     */
    private String status;
    /**
     * BANKING: deposit or withdrawal
     * TRADING: buy/sell or short/cover
     */
    private String type;
    
    public Transaction(String id, double amount, String type)
    {
        super();
        this.userID = id;
        this.amount = amount;
        this.reqDate = LocalDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS);
        this.exeDate = null;
        this.status = "Open";
        this.type = type;
    }
    
    public String getUserID()
    {
        return userID;
    }
    
    public double getAmount()
    {
        return amount;
    }
    
    public LocalDateTime getExeDate()
    {
        return exeDate;
    }
    
    public LocalDateTime getReqDate()
    {
        return reqDate;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setUserID(String userID)
    {
        this.userID = userID;
    }
    
    public void setAmount(double amount)
    {
        this.amount = amount;
    }
    
    public void setExeDate(LocalDateTime exeDate)
    {
        this.exeDate = exeDate;
    }
    
    public void setReqDate(LocalDateTime reqDate)
    {
        this.reqDate = reqDate;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
}

