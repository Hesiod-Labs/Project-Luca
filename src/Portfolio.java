import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Portfolio
{
    private static ArrayList<Asset> portfolio; // TODO rename to assets or something
    private static String nameOfPortFolio;
    private static Balance portfolioBalance;
    private static ZonedDateTime timeCreated;
    
    public Portfolio(String name, Balance portfolioBalance)
    {
        Portfolio.timeCreated = ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS);
        Portfolio.nameOfPortFolio = name;
        Portfolio.portfolioBalance = portfolioBalance;
        Portfolio.portfolio = new ArrayList<>();
    }
    
    public Portfolio(String name)
    {
        Portfolio.timeCreated = ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS);
        Portfolio.nameOfPortFolio = name;
        Portfolio.portfolio = new ArrayList<>();
        Portfolio.portfolioBalance = new Balance();
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
    
    //TODO Copied from Transaction class
    
    public static String formatTime(ZonedDateTime time)
    {
        String month = time.getMonth().toString();
        String day = Integer.toString(time.getDayOfMonth());
        String year = Integer.toString(time.getYear());
        String hour = Integer.toString(time.getHour());
        String minute = Integer.toString(time.getMinute());
        String second = Integer.toString(time.getSecond());
        String timeZone = time.getZone().toString();
    
        return month + " " + day + ", " + year + " (" + hour + ":" + minute + ":" + second + " " + timeZone + ")";
    }
    
    //******************************** GETTER METHODS ********************************//
    
    public static ArrayList<Asset> getPortfolio()
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
    
    public static void setPortfolio(ArrayList<Asset> portfolio)
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
