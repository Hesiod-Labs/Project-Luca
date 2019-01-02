import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Set;

public class Portfolio implements Trading
{
    private static Set<Asset> portfolio;
    private static String nameOfPortFolio;
    private static Balance portfolioBalance;
    private static ZonedDateTime timeCreated;
    
    public Portfolio(String name, Balance portfolioBalance)
    {
        Portfolio.nameOfPortFolio = name;
        Portfolio.portfolioBalance = portfolioBalance;
        Portfolio.timeCreated = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
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
        getPortfolio().add(asset);
    }
    
    public static Set<Asset> getPortfolio()
    {
        return portfolio;
    }
}
