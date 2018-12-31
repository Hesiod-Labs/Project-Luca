import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Set;

public class Portfolio implements Trading
{
    private Set<Asset> portfolio;
    private String nameOfPortFolio;
    private Balance portfolioBalance;
    private ZonedDateTime timeCreated;
    
    public Portfolio(String name, Balance portfolioBalance)
    {
        this.nameOfPortFolio = name;
        this.portfolioBalance = portfolioBalance;
        this.timeCreated = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }
    
    // TODO Fix how updateBalance works and how it interacts between portfolioBalance and bankBalance
    public void buyOrder(Transaction transaction)
    {
        portfolioBalance.updateBalance(transaction);
        transaction.setExeDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
        transaction.setStatus(Transaction.Status.EXECUTED);
    }
    
    public void sellOrder(Transaction transaction)
    {
        portfolioBalance.updateBalance(transaction);
        transaction.setExeDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
        transaction.setStatus(Transaction.Status.EXECUTED);
    }
    
    public void shortOrder(Transaction transaction)
    {
        portfolioBalance.updateBalance(transaction);
        transaction.setExeDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
        transaction.setStatus(Transaction.Status.EXECUTED);
    }
    
    public void coverOrder(Transaction transaction)
    {
        portfolioBalance.updateBalance(transaction);
        transaction.setExeDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
        transaction.setStatus(Transaction.Status.EXECUTED);
    }
    
    public void addAsset(Asset asset)
    {
        portfolio.add(asset);
    }
    
}
