import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Set;

public class Portfolio
{
    private static Set<Asset> portfolio;
    private static String nameOfPortFolio;
    private static Balance portfolioBalance;
    private static ZonedDateTime timeCreated;
    
    public Portfolio(String name, Balance portfolioBalance)
    {
        Portfolio.timeCreated = ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS);
        Portfolio.nameOfPortFolio = name;
        Portfolio.portfolioBalance = portfolioBalance;
    }
    
    public Portfolio(String name)
    {
        Portfolio.timeCreated = ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS);
        Portfolio.nameOfPortFolio = name;
    }
    
    public static void buyOrder(Transaction transaction)
    {
        transaction.getTransactionAsset().setStartDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
        transaction.setTransactionStatus(Transaction.Status.BOUGHT);
        transaction.getTransactionAsset().setOwn(true);
        Portfolio.addToPortfolio(transaction.getTransactionAsset());
        getPortfolioBalance().updateBalance(Balance.transferTo(transaction));
        Bank.getBankBalance().updateBalance(Balance.transferFrom(transaction));
    }
    
    public static void sellOrder(Transaction transaction)
    {
        transaction.setTransactionStatus(Transaction.Status.SOLD);
        transaction.getTransactionAsset().sellAsset();
        /* Amount transferred out of the Portfolio that which was originally transferred in from the Bank. */
        getPortfolioBalance().updateBalance(Balance.transferFrom(transaction));
        /* To get original transaction amount: originalPrice*volume. */
        transaction.setTransactionAmount(transaction.getTransactionAsset().getReturns());
        /* Accounts for the net gain/loss by the selling of the asset. */
        Bank.getBankBalance().updateBalance(Balance.transferTo(transaction));
    }
    
    /*
    public void shortOrder(Transaction transaction)
    {
        transaction.setResolveDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
        transaction.setTransactionStatus(Transaction.Status.SHORTED);
    }
    
    public void coverOrder(Transaction transaction)
    {
        //portfolioBalance.updateBalance(transaction);
        transaction.setResolveDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
        transaction.setTransactionStatus(Transaction.Status.COVERED);
    }
    */
    public static void addToPortfolio(Asset asset)
    {
        getPortfolio().add(asset);
    }
    
    //******************************** GETTER METHODS ********************************//
    
    public static Set<Asset> getPortfolio()
    {
        return portfolio;
    }
    
    public static String getNameOfPortFolio()
    {
        return nameOfPortFolio;
    }
    
    public static Balance getPortfolioBalance()
    {
        return portfolioBalance;
    }
    
    public static ZonedDateTime getTimeCreated()
    {
        return timeCreated;
    }
    
    //******************************** SETTER METHODS ********************************//
    
    public static void setPortfolio(Set<Asset> portfolio)
    {
        Portfolio.portfolio = portfolio;
    }
    
    public static void setNameOfPortFolio(String nameOfPortFolio)
    {
        Portfolio.nameOfPortFolio = nameOfPortFolio;
    }
    
    public static void setPortfolioBalance(Balance portfolioBalance)
    {
        Portfolio.portfolioBalance = portfolioBalance;
    }
    
    public static void setTimeCreated(ZonedDateTime timeCreated)
    {
        Portfolio.timeCreated = timeCreated;
    }
}
