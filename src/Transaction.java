import java.time.*;
import java.time.temporal.ChronoUnit;

public class Transaction
{
    private String userID;
    private double amount;
    private ZonedDateTime reqDate;
    private ZonedDateTime exeDate;
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
    private String orderID;
    
    // TODO change id to User user
    public Transaction(String id, double amount, String type)
    {
        super();
        this.userID = id;
        this.amount = amount;
        this.reqDate = ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS);
        this.exeDate = null;
        this.status = "Open";
        this.type = type;
        this.orderID = reqDate.getYear() + "" + reqDate.getMonthValue() + "" + reqDate.getDayOfMonth() + "" + reqDate.getHour() + "" + reqDate.getMinute() + "" + reqDate.getSecond();
    }
    
    
    public String getUserID()
    {
        return userID;
    }
    
    public double getAmount()
    {
        return amount;
    }
    
    public ZonedDateTime getExeDate()
    {
        return exeDate;
    }
    
    public ZonedDateTime getReqDate()
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
    
    public String getOrderID()
    {
        return orderID;
    }
    
    public void setUserID(String userID)
    {
        this.userID = userID;
    }
    
    public void setAmount(double amount)
    {
        this.amount = amount;
    }
    
    public void setExeDate(ZonedDateTime exeDate)
    {
        this.exeDate = exeDate;
    }
    
    public void setReqDate(ZonedDateTime reqDate)
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
    
    
    /*public ArrayList<Transaction> getOrderHistory()
    {
        return orderHistory;
    } */
    
}

