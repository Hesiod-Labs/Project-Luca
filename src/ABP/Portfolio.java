package ABP;

import BTA.*;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;

/**
 * The repository in which active trading funds are kept. The portfolio can be assigned a name. Otherwise, it is assigned
 * the default name "Unnamed Portfolio." The portfolio has a {@link Balance} that contains both the current value of the
 * portfolio, as well as past values. Each time the portfolio's balance is updated, the time in which it is updated,
 * the amount, and the associated transaction are all recorded as part of the new balance. Note that the portfolio's
 * balance is <it>not</it> the returns of the portfolio; this information is stored in the
 * {@link Portfolio portfolioReturns} Balance. {@link Asset}s are acquired through the processes of buying
 * ({@link #buyOrder(Transaction)}) and shorting ({@link #shortOrder(Transaction)}). Conversely, {@link Asset}s are
 * liquidated through the processes of selling ({@link Asset#sellAsset()})and covering ({@link Asset#coverAsset()}).
 * //TODO Move buy/sell methods to Asset or short/cover to Portfolio
 * @author hLabs
 * @since 1.0
 */
public class Portfolio
{
    /**
     * Contains all assets owned by the {@link Account}. {@link LinkedList} is used here so as to preserve the order
     * in which the assets were acquired. This is useful in the case where two acquisition transactions of the same
     * asset are resolved at different times with different prices, so each can be handled uniquely.
     */
    private static LinkedList<Asset> portfolio;
    
    /**
     * Name designated to the portfolio. If unnamed, the portfolio is given the default name, "Unnamed Portfolio."
     */
    private static String nameOfPortFolio;
    
    /**
     * Contains information on both the current value and previous value(s) of the portfolio. Whenever an asset is
     * acquired or liquidated, the portfolio balance is updated.
     * @see Balance
     */
    private static Balance portfolioBalance;
    
    /**
     * Contains information on the net returns of the portfolio over time, such as date and time information, as well
     * as the transaction that resulted in a new value of returns.
     */
    private static Balance portfolioReturns;
    
    /**
     * Date and time in which the portfolio is created.
     */
    private static ZonedDateTime timeCreated;
    
    /**
     * Creates a portfolio with a non-default name, balance, and set
     * @param name Name designated to the portfolio.
     * @param portfolioBalance Contains current and previous dollar amounts inputted into the portfolio when assets
     *                         are initially acquired. Once assets are liquidated, the balance is decreased by the
     *                         initial amount when the asset was first acquired.
     * @param portfolio Contains all owned assets in the order in which they were acquired.
     * @see Balance
     * @see Asset
     */
    public Portfolio(String name, Balance portfolioBalance, LinkedList<Asset> portfolio)
    {
        Portfolio.timeCreated = ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS);
        Portfolio.nameOfPortFolio = name;
        Portfolio.portfolioBalance = portfolioBalance;
        Portfolio.portfolio = portfolio;
        portfolioReturns = new Balance();
    }
    
    /**
     * Creates a portfolio with a non-default name, a potentially nonzero balance, and an empty portfolio with no
     * owned assets.
     * @param name Name designated to the portfolio.
     * @param portfolioBalance Contains current and past values, associated transactions, and timestamps.
     * @see Balance
     * @see Asset
     */
    public Portfolio(String name, Balance portfolioBalance)
    {
        this(name, portfolioBalance, new LinkedList<>());
    }
    
    /**
     * Creates a portfolio with a non-default name, a balance of zero, and an empty portfolio with no owned assets.
     * @param name Name designated to the portfolio.
     * @see Balance
     * @see Asset
     */
    public Portfolio(String name)
    {
        this(name, new Balance(), new LinkedList<>());
    }
    
    /**
     * Creates a portfolio with the default name, "Unnamed Portfolio," a balance of zero, and an empty portfolio with
     * no owned assets.
     */
    public Portfolio()
    {
        this("Unnamed Portfolio", new Balance(), new LinkedList<>());
    }
    
    /**
     * Acquires the asset associated with a "BUY" transaction request ({@link Transaction.Type}). If the request is not
     * denied during resolution, the start date and owned statuses of the asset belonging to the transaction are set.
     * The status of the transaction is set to "BOUGHT" ({@link Transaction.Status}). The amount associated with
     * buying the asset is transferred from the {@link Bank} to the portfolio. The transaction associated with acquiring
     * the asset is also set. If the amount required to buy the asset is greater than that which resides in the
     * {@link Bank}, a message is printed and the transaction status is set to "CANCELLED" ({@link Transaction.Status}).
     * @param transaction Contains the asset to be bought.
     */
    public static void buyOrder(Transaction transaction)
    {
        if(transaction.getTransactionAmount() <= Bank.getBankBalance().getCurrentValue())
        {
            transaction.getTransactionAsset().setStartDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(
                    ChronoUnit.SECONDS));
            transaction.setTransactionStatus(Transaction.Status.BOUGHT);
            transaction.getTransactionAsset().setOwn(true);
            transaction.getTransactionAsset().setAcquisitionTransaction(transaction);
            Portfolio.addToPortfolio(transaction.getTransactionAsset());
            /* Transaction amount is transferred to the portfolio balance. */
            getPortfolioBalance().updateBalance(Balance.transferTo(transaction));
            /* Transaction amount is transferred from the bank balance. */
            Bank.getBankBalance().updateBalance(Balance.transferFrom(transaction));
        }
        else
        {
            System.out.println("The amount required to buy this asset is more than what exists in the bank.");
            transaction.setTransactionStatus(Transaction.Status.CANCELLED);
        }
    }
    
    /**
     * Liquidates a bought asset ({@link Transaction.Status}) if the asset currently exists in the portfolio.
     * @param transaction Contains the asset to be sold.
     */
    public static void sellOrder(Transaction transaction)
    {
        // Check that the asset associated with the transaction is in the portfolio.
        if(Portfolio.getPortfolio().contains(transaction.getTransactionAsset()))
        {
            transaction.setTransactionStatus(Transaction.Status.SOLD);
            transaction.getTransactionAsset().setLiquidationTransaction(transaction);
            transaction.getTransactionAsset().sellAsset();
            /* Amount transferred out of the Portfolio that which was originally transferred in from the Bank. */
            getPortfolioBalance().updateBalance(Balance.transferFrom(transaction));
            /* To get original transaction amount: originalPrice*volume. */
            transaction.setTransactionAmount(transaction.getTransactionAsset().getReturns());
            /* Accounts for the net gain/loss by the selling of the asset. */
            Bank.getBankBalance().updateBalance(Balance.transferTo(transaction));
            Account.getAccountBalance().updateBalance(Balance.transferTo(transaction));
        }
        else
            System.out.println("Asset cannot be sold since it does not exist in the portfolio.");
    }
    
    /**
     * //TODO Add more @links to this description
     * Shorts an asset associated with the transaction on the condition that there are enough funds in the {@link Bank}
     * to immediately cover all shorted shares of the asset. This is implemented for risk-prevention purposes. If the
     * amount associated with the transaction is less than what is in the bank, then the short order is processed such
     * that the resolution date and time of the transaction is set, the asset start date is set to when the short order
     * is processed, the transaction status is set to "SHORTED," the asset is set to owned, and the portfolio balance
     * is updated. The bank balance is not updated since no returns have yet been gained or lossed.
     * @param transaction Contains the asset to be shorted.
     */
    public static void shortOrder(Transaction transaction)
    {
        double amountToShort = transaction.getTransactionAmount();
        double numShares = transaction.getTransactionAsset().getVolume();
        double pricePerShare = transaction.getTransactionAsset().getStartPrice();
        double safetyFunds = Bank.getBankBalance().getCurrentValue() - numShares*pricePerShare;
        
        System.out.println("Safety funds: " + safetyFunds);
        
        double safetyPerShare = (safetyFunds / numShares); // TODO This calculates correctly
    
        System.out.println("Safety Per Share: " + safetyPerShare);
        
        double safetyPercent = (safetyPerShare / pricePerShare)*100.0;
        if(amountToShort < safetyFunds)
        {
            transaction.setResolveDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
            transaction.getTransactionAsset().setStartDate(ZonedDateTime.now(
                    ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
            transaction.setTransactionStatus(Transaction.Status.SHORTED);
            transaction.getTransactionAsset().setOwn(true);
            transaction.getTransactionAsset().setAcquisitionTransaction(transaction);
            Portfolio.addToPortfolio(transaction.getTransactionAsset());
            getPortfolioBalance().updateBalance(Balance.transferTo(transaction));
            System.out.println("CAUTION: " + "You can afford a $" + safetyPerShare + " (" + safetyPercent + "%/share)" +
                    " increase before you must cover your order due to insufficient bank funds.");
        }
        else
        {
            System.out.println("You cannot afford to short this amount due to insufficient bank funds to cover losses.");
        }
    }
    
    /**
     * //TODO Add more @links in the description.
     * // TODO Fix negative bug
     * Covers a shorted asset based on the condition that the asset is currently being shorted in the portfolio. The
     * resolution date is set to when the asset is covered. The transaction status is set to "COVERED." Asset returns
     * and duration of owning the asset are calculated. The asset is set to not owned and is removed from the portfolio.
     * The portfolio and bank balances are updated accordingly. Because of the nature of shorting and covering stocks,
     * the amount set to update the bank balance is opposite that which was returned.
     * @param transaction Contains the asset to be covered.
     */
    public static void coverOrder(Transaction transaction)
    {
        if(Portfolio.getPortfolio().contains(transaction.getTransactionAsset()))
        {
            transaction.setResolveDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
            transaction.setTransactionStatus(Transaction.Status.COVERED);
            transaction.getTransactionAsset().setLiquidationTransaction(transaction);
            transaction.getTransactionAsset().coverAsset();
            getPortfolioBalance().updateBalance(Balance.transferFrom(transaction));
            transaction.setTransactionAmount(-transaction.getTransactionAsset().getReturns());
            Bank.getBankBalance().updateBalance(Balance.transferTo(transaction));
            Account.getAccountBalance().updateBalance(Balance.transferTo(transaction));
        }
        else
        {
            System.out.println("Asset cannot be covered since it does not exist in the portfolio.");
        }
        
    }
    
    /**
     * Adds an asset to the portfolio of owned assets. Used during {@link #buyOrder(Transaction)} and
     * {@link #shortOrder(Transaction)} methods.
     * @param asset Asset to be added to the portfolio.
     */
    private static void addToPortfolio(Asset asset)
    {
        getPortfolio().add(asset);
    }
    
    /**
     * Returns the portfolio, which contains all of the assets.
     * @return Portfolio containing the assets currently owned.
     */
    public static LinkedList<Asset> getPortfolio()
    {
        return portfolio;
    }
    
    /**
     * Returns the name of the portfolio.
     * @return Name designated to the portfolio. If not specified a non-default name, returns "Unnamed Portfolio."
     */
    public static String getNameOfPortFolio()
    {
        return nameOfPortFolio;
    }
    
    /**
     * Returns the balance of the portfolio, which contains the dollar amount and time/date info being utilized for
     * investing.
     * @return Current and past values of funds existing within the portfolio used for trading. Also contains
     * associated transaction and timestamp information for each balance statement.
     * @see Balance
     */
    public static Balance getPortfolioBalance()
    {
        return portfolioBalance;
    }
    
    /**
     * Returns the net returns of the portfolio. The most recent balance is the most current returns value.
     * @return Time/date and dollar amount info of net returns after liquidating each asset.
     */
    public static Balance getPortfolioReturns()
    {
        return portfolioReturns;
    }
}
