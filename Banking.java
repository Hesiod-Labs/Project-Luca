import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Banking extends Transaction
{
    private ArrayList<Transaction> bankingOrders;
    
    public Banking(String id, double amount, String requestType)
    {
        super(id, amount, requestType);
    }
    
    public void deposit(double amount)
    {
        updateTotalFunds(getTotalFunds() + amount);
        updateWithdrawableFunds(getWithdrawableFunds() + amount);
        setExeDate(LocalDateTime.now(ZoneId.of("America/New_York")));
        setStatus("DEPOSITED");
    }
    
    public void withdraw(double amount)
    {
        updateTotalFunds(getTotalFunds() - amount);
        updateWithdrawableFunds(getWithdrawableFunds() - amount);
        setExeDate(LocalDateTime.now(ZoneId.of("America/New_York")));
        setStatus("WITHDRAWN");
    }
    
    public String printSummary()
    {
    return "User ID: " + getUserID() + "\n" +
            "Order ID: " + getOrderID() + "\n" +
            "Order Type: " + getType() + "\n" +
            "Amount: " + "$" + getAmount() + "\n" +
            "Status: " + getStatus() + "\n" +
            "Date Created: " + getReqDate().truncatedTo(ChronoUnit.SECONDS) + "\n" +
            "Date Managed: " + getExeDate().truncatedTo(ChronoUnit.SECONDS);
    }
    
}

