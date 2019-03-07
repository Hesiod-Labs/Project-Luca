package BTA;

import ABP.*;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * A stock (option functionality to be implemented in future versions) that is owned the {@link Portfolio}. Has basic
 * identification information, such as the name and stock symbol of the company, the sector (based on Hesiod Financial),
 * volume of shares, order type, and the associated transaction to acquire and/or liquidate the asset holding;
 * in addition to returns-based information, such as start/end price, net returns. At the time of acquisition
 * (buy/short), the following information is established:
 * <ul>
 *     <li><code>assetName</code></li>
 *     <li><code>assetSymbol</code></li>
 *     <li><code>sector</code></li>
 *     <li><code>volume</code></li>
 *     <li><code>startPrice</code></li>
 *     <li><code>startDate</code></li>
 *     <li><code>own</code> (set to <code>true</code>)</li>
 *     <li><code>orderType</code></li>
 *     <li><code>acquisitionTransaction</code></li>
 * </ul>
 * At the time of liquidation (sell/cover), the remaining data fields are assigned:
 * <ul>
 *     <li><code>endPrice</code></li>
 *     <li><code>timeHeld</code></li>
 *     <li><code>own</code> (set to <code>false</code>)</li>
 *     <li><code>liquidationTransaction</code></li>
 * </ul>
 * @author hLabs
 * @since 1.0
 */
public class Asset
{
    /**
     * Full name of the asset. Example: Chipotle Mexican Grill.
     */
    private String assetName;
    
    /**
     * Stock symbol in accordance with New York Stock Exchange. Example: CMG.
     */
    private String assetSymbol;
    
    /**
     * Sector of the Hesiod Financial portfolio to which the asset belongs:
     * <ul>
     *     <li>Cryptocurrency (CRYPTO)</li>
     *     <li>Communication Services and Information Technology (CS-IT)</li>
     *     <li>Real Estate and Financials (REIT_FIG)</li>
     *     <li>Foreign Exchange (FOREX)</li>
     *     <li>Materials, Energy, Industrials (MEI)</li>
     *     <li>Consumer D&S and Healthcare (CONSUMER_HEALTHCARE)</li>
     *     <li>Options (OPTIONS)</li>
     * </ul>
     */
    private Sector sector;
    
    /**
     * Number of shares of the asset.
     */
    private int volume;
    
    /**
     * Price at which the asset is acquired and added to the {@link Portfolio}.
     */
    private double startPrice;
    
    /**
     * Price at which the asset is removed from the {@link Portfolio}.
     */
    private double endPrice;
    
    /**
     * Difference between {@link #startPrice} and {@link #endPrice}.
     */
    private double returns;
    
    /**
     * Date and time of when the asset is acquired and added to the {@link Portfolio}.
     */
    private ZonedDateTime startDate;
    
    /**
     * Date and time of when the asset is liquidated from the {@link Portfolio}.
     */
    private ZonedDateTime endDate;
    
    /**
     * Difference between {@link #startDate} and {@link #endDate} in seconds.
     */
    private double timeHeld;
    
    /**
     * <code>true</code> if the asset is owned and <code>false</code> if not.
     */
    private boolean own;
    
    /** Different types of asset orders that determine when and at what price to acquire or liquidate an asset:
     * <ul>
     *     <li>Limit</li>
     *     <li>Market</li>
     *     <li>Stop Limit</li>
     *     <li>Stop Loss</li>
     *     <li>Trailing Stop Limit</li>
     *     <li>Trailing Stop Loss</li>
     * </ul>
     */
    private OrderType orderType;
    
    /**
     * The transaction tied to the acquisition of the asset.
     */
    private Transaction acquisitionTransaction;
    
    /**
     * The transaction tied to the liquidation of the asset.
     */
    private Transaction liquidationTransaction;
    
    /**
     * Creates an asset to be added to the {@link Portfolio}.
     * @param nameOfAsset Full name of the asset.
     * @param sym Asset symbol in accordance with NYSE.
     * @param sect Sector to which the asset belongs.
     * @param vol Number of shares of the asset.
     * @param orgPrice Price at which the asset is first acquired.
     * @param orderType Determines at what price and when the asset is acquired or liquidated ({@link #orderType}).
     */
    public Asset(String nameOfAsset, String sym, Sector sect, int vol, double orgPrice, OrderType orderType)
    {
        this.assetName = nameOfAsset;
        this.assetSymbol = sym;
        this.sector = sect;
        this.volume = vol;
        this.startPrice = orgPrice;
        this.own = false;
        this.orderType = orderType;
    }
    
    /**
     * Sells an asset, recording the time in which the sell action is performed, calculates the duration and returns
     * of owning the asset, and finally removes the asset from the {@link Account} portfolio.
     */
    public void sellAsset()
    {
        this.setEndDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
        this.setOwn(false);
        this.setReturns(calculateReturns());
        // TODO Portfolio.getPortfolioReturns().updateBalance(Balance.transferTo());
        this.calculateTimeHeld();
        Portfolio.getPortfolio().remove(this);
    }
    
    /**
     * Covers an asset, recording the time in which the cover action is performed, calculates the duration and returns
     * of owning the asset, and finally removes the asset from the {@link Account} portfolio.
     */
    public void coverAsset()
    {
        this.setEndDate(ZonedDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.SECONDS));
        this.setOwn(false);
        this.setReturns(-calculateReturns());
        this.calculateTimeHeld();
        Portfolio.getPortfolio().remove(this);
    }
    
    /**
     * Calculates the returns on an owned asset by subtracting the price at acquisition ({@link #startPrice}) from
     * the price at liquidation({@link #endPrice}). Can also be used to calculate hypothetical returns given any
     * start and finish prices ({@link #calculateHypotheticalReturns}).
     * @return Dollar amount earned or lossed by investing the asset.
     */
    private double calculateReturns()
    {
        double returnsPerShare = this.getEndPrice() - this.getStartPrice();
        return (returnsPerShare*this.getVolume());
    }
    
    /**
     * Calculates the returns of an asset given any acquisition price ({@link #startPrice}) and liquidation price
     * ({@link #endPrice}).
     * @param startPrice Share price of asset at time of acquisition.
     * @param endPrice Share price of asset at time of liquidation.
     * @return Hypothetical returns of owning the asset.
     */
    private double calculateHypotheticalReturns(double startPrice, double endPrice)
    {
        Asset hypothetical = this;
        hypothetical.setStartPrice(startPrice);
        hypothetical.setEndPrice(endPrice);
        return hypothetical.calculateReturns();
    }
    
    /**
     * Calculates the amount of time between when the asset is acquired and liquidated.
     * @see Portfolio#sellOrder(Transaction)
     */
    private void calculateTimeHeld()
    {
        ZonedDateTime start = this.getStartDate();
        ZonedDateTime end = this.getEndDate();
        double duration = ChronoUnit.SECONDS.between(start, end);
        this.setTimeHeld(duration);
    }
    
    /** The different economic sectors to which an asset can belong. */
    public enum Sector
    {
        CONSUMER_HEALTHCARE, CRYPTO, CS_IT, FOREX, MEI, OPTIONS, REIT_FIG;
    }
    
    /**
     * Different ways in which an asset can be acquired or liquidated. Descriptions originally from Fidelity.com.
     * <ul>
     *     <li>Limit: The stock is eligible to be purchased at or below the limit price, but never above it. When a
     *     limit order is placed to sell, the stock is eligible to be sold at or above the limit price, but never below
     *     it. Although a limit order allows for a specified price limit, it does not guarantee that your order will be
     *     executed.</li>
     *     <li>Market: The stock is eligible to be purchased at the next available price. A market order remains in
     *     effect only for the day, and usually results in the prompt purchase or sale of all the shares in question,
     *     as long as the security is actively traded and market conditions permit.</li>
     *     <li>Stop Limit (stock): Automatically becomes a limit order when the stop price is reached. May be filled
     *     in whole, in part, or not at all, depending on the number of shares available for sale or purchase at the
     *     time. </li>
     *     <li>Stop Limit (option): a stop limit order to buy automatically becomes a limit order when the bid price is
     *     at or above the stop price, or the option trades at or above the stop price. A stop limit order to sell
     *     automatically becomes a limit order when the ask price is at or below the stop price, or when the option
     *     trades at or below the stop price. The option stop election is based on the exchange's best bid or off (BBO)
     *     where the stop order resides.</li>
     *     <li>Trailing Stop Limit: Order becomes a limit order when the order is triggered.</li>
     *     <li>Trailing Stop Loss: Order adjusts in price with favorable market movement on the security. To sell, the
     *     stop price moves up as the price of the security moves up. To buy, the stop price moves down as the price of
     *     security goes down. If the price of the security is moving against the customer's order, the stop price does
     *     not adjust.</li>
     * </ul>
     * @see <a href = "https://scs.fidelity.com/webxpress/help/topics/help_definition_t.shtml#trailingstoplimit">
     *     https://scs.fidelity.com/</a>
     */
    public enum OrderType
    {
        LIMIT, MARKET, STOP_LIMIT, STOP_LOSS, TRAILING_STOP_LIMIT, TRAILING_STOP_LOSS
    }
    
    /**
     * The full name of the asset.
     * @return Full name of the asset.
     */
    public String getAssetName()
    {
        return assetName;
    }
    
    /**
     * The symbol of the asset.
     * @return Symbol of the asset, per the New York Stock Exchange.
     */
    public String getSymbol()
    {
        return assetSymbol;
    }
    
    /**
     * The sector to which the asset belongs, according to those existing in Hesiod Financial
     * @return Economic sector to which the asset belongs.
     * @see Sector
     */
    public Sector getSector()
    {
        return sector;
    }
    
    /**
     * The number of shares of the asset.
     * @return Number of shares of the asset.
     */
    public int getVolume()
    {
        return volume;
    }
    
    /**
     * The dollar price at which the asset is acquired in the {@link Portfolio}.
     * @return Dollar price at which the asset is acquired in the {@link Portfolio}.
     */
    public double getStartPrice()
    {
        return startPrice;
    }
    
    /**
     * The dollar price at which the asset is liquidated from the {@link Portfolio}.
     * @return Dollar price at which the asset is liquidated from the {@link Portfolio}.
     */
    public double getEndPrice()
    {
        return endPrice;
    }
    
    /**
     * The net dollar amount returned by owning an asset. Known only after the {@link #startPrice} and
     * {@link #endPrice} are set.
     * @return Net dollar amount gained by owning an asset.
     */
    public double getReturns()
    {
        return returns;
    }
    
    /**
     * The date and time in which the asset is added to the {@link Portfolio}.
     * @return Date and time information.
     */
    public ZonedDateTime getStartDate()
    {
        return startDate;
    }
    
    /**
     * The date and time in which the asset is removed from the {@link Portfolio}.
     * @return Date and time information.
     */
    public ZonedDateTime getEndDate()
    {
        return endDate;
    }
    
    /**
     * The number of seconds between {@link #startDate} and {@link #endDate}.
     * @return Number of seconds.
     */
    public double getTimeHeld()
    {
        return timeHeld;
    }
    
    /**
     * <code>true</code> of the asset exists in the {@link Portfolio} and <code>false</code> if not.
     * @return <code>true</code> if owned and <code>false</code> if not.
     */
    public boolean isOwned()
    {
        return own;
    }
    
    /**
     * The order type that determines when and what price the asset should be acquired or liquidated.
     * @return Order type
     * @see OrderType
     */
    public OrderType getOrderType()
    {
        return orderType;
    }
    
    /**
     * The transaction associated with acquiring the asset.
     * @return Associated transaction.
     */
    public Transaction getAcquisitionTransaction()
    {
        return acquisitionTransaction;
    }
    
    /**
     * The transaction associated with liquidating the asset.
     * @return Associated transaction.
     */
    public Transaction getLiquidationTransaction()
    {
        return liquidationTransaction;
    }
    
    /**
     * WARNING: DO NOT CHANGE UNLESS THE CURRENT START PRICE IS INCORRECT.
     * @param startPrice Dollar price at which the asset is acquired.
     */
    public void setStartPrice(double startPrice)
    {
        this.startPrice = startPrice;
    }
    
    /**
     * WARNING: DO NOT CHANGE UNLESS THE CURRENT END PRICE IS INCORRECT.
     * @param endPrice Dollar price at which the asset is liquidated.
     */
    public void setEndPrice(double endPrice)
    {
        this.endPrice = endPrice;
    }
    
    /**
     * WARNING: DO NOT CHANGE UNLESS THE AMOUNT CALCULATED AFTER LIQUIDATION IS INCORRECT.
     * @param returns Dollar amount earned or lossed from trading an asset.
     */
    public void setReturns(double returns)
    {
        this.returns = returns;
    }
    
    /**
     * WARNING: DO NOT CHANGE UNLESS THE CURRENT START DATE IS INCORRECT.
     * @param startDate Date and time in which the asset is acquired.
     */
    public void setStartDate(ZonedDateTime startDate)
    {
        this.startDate = startDate;
    }
    
    /**
     * WARNING: DO NOT CHANGE UNLESS THE CURRENT END DATE IS INCORRECT.
     * @param endDate Date and time in which the asset is liquidated.
     */
    public void setEndDate(ZonedDateTime endDate)
    {
        this.endDate = endDate;
    }
    
    /**
     * WARNING: DO NOT CHANGE UNLESS THE CURRENT END DATE IS INCORRECT.
     * @param timeHeld Duration, in seconds, between the {@link #startDate} and {@link #endDate}.
     */
    public void setTimeHeld(double timeHeld)
    {
        this.timeHeld = timeHeld;
    }
    
    /**
     * <code>true</code> if the asset exists in the {@link Portfolio} and <code>false</code> if not.
     * @param own <code>true</code> if the asset exists in the {@link Portfolio} and <code>false</code> if not.
     */
    public void setOwn(boolean own)
    {
        this.own = own;
    }
    
    /**
     * Sets the transaction associated with acquiring the asset.
     * @param acquisitionTransaction Associated transaction.
     */
    public void setAcquisitionTransaction(Transaction acquisitionTransaction)
    {
        this.acquisitionTransaction = acquisitionTransaction;
    }
    
    /**
     * Sets the transaction associated with liquidating the asset.
     * @param liquidationTransaction Associated transaction.
     */
    public void setLiquidationTransaction(Transaction liquidationTransaction)
    {
        this.liquidationTransaction = liquidationTransaction;
    }
}
