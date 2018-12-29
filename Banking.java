import java.time.*;

public class Banking extends Transaction
{
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
    
}

