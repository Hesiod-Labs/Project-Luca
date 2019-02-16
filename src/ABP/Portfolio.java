package ABP;

import BTA.*;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Portfolio
{
    /**
     * Contains all assets owned by the {@link Account}. {@link LinkedHashSet} is used here so as to preserve the order
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
        this(name, portfolioBalance, new LinkedList<Asset>());
    }
    
    /**
     * Creates a portfolio with a non-default name, a balance of zero, and an empty portfolio with no owned assets.
     * @param name Name designated to the portfolio.
     * @see Balance
     * @see Asset
     */
    public Portfolio(String name)
    {
        this(name, new Balance(), new LinkedList<Asset>());
    }
    
    /**
     * Creates a portfolio with the default name, "Unnamed Portfolio," a balance of zero, and an empty portfolio with
     * no owned assets.
     */
    public Portfolio()
    {
        this("Unnamed Portfolio", new Balance(), new LinkedList<Asset>());
    }
    
    /**
     * //TODO Add description about associated transaction with asset to all forms of resolutions
     * Acquires the asset associated with a "BUY" transaction request ({@link Transaction.Type}). If the request is not
     * denied during resolution, the start date and owned statuses of the asset belonging to the transaction are set.
     * The status of the transaction is set to "BOUGHT" ({@link Transaction.Status}). The amount associated with
     * buying the asset is transferred from the {@link Bank} to the portfolio. If the amount required to buy the asset
     * is greater than that which resides in the {@link Bank}, a message is printed and the transaction status is set
     * to "CANCELLED" ({@link Transaction.Status}).
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
     * //TODO Scenario 1: sell all of the asset. This is accounted for in what's written right now
     * //TODO Scenario 2: sell a portion of the asset.
     * //TODO Sell one transaction's worth of asset (as written now)
     * //TODO Sell more than one transaction's worth of asset, but not everything (maximize returns or manually)
     * Steps: getAsset...calculate returns based on how much is being sold...update asset remaining in the portfolio
     * @param transaction Contains the asset to be sold.
     */
    public static void sellOrder(Transaction transaction)
    {
        /* THE CODE COMMENTED OUT HERE IS PART OF THE CURRENT ATTEMPT TO DETECT ADVANCED CASES, SUCH AS WHEN THE AMOUNT
        OF STOCKS TO SELL IS NOT EQUAL OR TIED TO A UNIT ASSET, SO SHARES OF ASSETS BELONGING TO SEPARATE BUY
        TRANSACTIONS MUST BE SOLD IN ORDER TO MAXIMIZE RETURNS.
        
        // If the sell transaction has a specified unit asset, sell only that asset.
        // If the sell transaction has a specified non-unit asset, sell to maximize returns.
        String symbol = transaction.getTransactionAsset().getSymbol();
        
        int totalSharesToSell = 0;
        for(Asset a : getPortfolio())
        {
            if(a.getSymbol().equalsIgnoreCase(symbol) && a.getAcquisitionTransaction().getTransactionType().
                    equals(Transaction.Type.BUY))
                totalSharesToSell++;
        }
        
        if(totalSharesToSell > 0)
        {
            if(totalSharesToSell >= transaction.getTransactionAsset().getVolume())
            {
                Predicate<Asset> symbolOccur = s -> s.getSymbol().equalsIgnoreCase(symbol);
                Predicate<Asset> buyOnly = symbolOccur.and(a -> a.getAcquisitionTransaction().getTransactionType().equals(Transaction.Type.BUY));
                List<Asset> assetList = Portfolio.getPortfolio().stream().filter(buyOnly).collect(Collectors.toList());
                int numAssetsToSell = assetList.size();
                for(Asset nextAsset : assetList)
                {
                    nextAsset.getVolume();
                    
                    System.out.println("Symbol: " + nextAsset.getSymbol() + "\n" + "Associated acquisition transaction " +
                            "ID: " +
                            nextAsset.getAcquisitionTransaction().getTransactionID());
                }
                
            }
            else
            {
                System.out.println("Not enough shares in the portfolio to sell. Select a lower number of shares.");
            }
        }
        else
        {
            System.out.println("Asset cannot be sold since it does not exist in the portfolio.");
        }
        */
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
    //}
    
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
        double safetyFunds = Bank.getBankBalance().getCurrentValue();
        double safetyPerShare = (safetyFunds / numShares); // TODO This calculates correctly
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
    public static void addToPortfolio(Asset asset)
    {
        getPortfolio().add(asset);
    }
    
    /**
     * @return Portfolio containing the assets currently owned.
     */
    public static LinkedList<Asset> getPortfolio()
    {
        return portfolio;
    }
    
    /**
     * @return Name designated to the portfolio. If not specified a non-default name, returns "Unnamed Portfolio."
     */
    public static String getNameOfPortFolio()
    {
        return nameOfPortFolio;
    }
    
    /**
     * @return Current and past values of funds existing within the portfolio used for trading. Also contains
     * associated transaction and timestamp information for each balance statement.
     * @see Balance
     */
    public static Balance getPortfolioBalance()
    {
        return portfolioBalance;
    }
    
    /**
     * @return Date and time in which the portfolio was created. Timezone set to America/New York.
     */
    public static ZonedDateTime getTimeCreated()
    {
        return timeCreated;
    }
    
    /**
     * Sets the portfolio of owned assets.
     * WARNING: DO NOT USE UNLESS PORTFOLIO HAS NOT BEEN SET. OTHERWISE, ALL OWNED ASSETS WILL BE LOSSED.
     * @param portfolio Assets owned.
     */
    public static void setPortfolio(LinkedList<Asset> portfolio)
    {
        Portfolio.portfolio = portfolio;
    }
    
    /**
     * Sets the name designated to the portfolio.
     * WARNING: DO NOT USE UNLESS THE PORTFOLIO NAME HAS NOT BEEN SET.
     * @param nameOfPortFolio Name designated to the portfolio.
     */
    public static void setNameOfPortFolio(String nameOfPortFolio)
    {
        Portfolio.nameOfPortFolio = nameOfPortFolio;
    }
    
    /**
     * Sets the balance, current, and past, of the portfolio.
     * WARNING: DO NOT USE UNLESS THE PORTFOLIO BALANCE HAS NOT ALREADY BEEN SET. OTHERWISE, THE CURRENT AND PAST
     * BALANCE STATEMENTS WILL BE LOSSED.
     * @param portfolioBalance Current and past balance statements of the portfolio with associated amounts, timestamps,
     *                         and transactions. In this case, it specifically represents the amount transferred from
     *                         the {@link Bank} to trade. Once an asset is liquidated, the original amount transferred
     *                         is removed from the portfolio balance.
     */
    public static void setPortfolioBalance(Balance portfolioBalance)
    {
        Portfolio.portfolioBalance = portfolioBalance;
    }
    
    /**
     * Sets the time in which the portfolio is created.
     * WARNING: DO NOT USE UNLESS THE TIME CREATED IS INCORRECT.
     * @param timeCreated Date and time in which the portfolio is created.
     */
    public static void setTimeCreated(ZonedDateTime timeCreated)
    {
        Portfolio.timeCreated = timeCreated;
    }
}
